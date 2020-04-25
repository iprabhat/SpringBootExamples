package com.myapp.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myapp.user.data.AlbumServiceClient;
import com.myapp.user.data.UserEntity;
import com.myapp.user.data.UserRepository;
import com.myapp.user.model.AlbumResponseModel;
import com.myapp.user.shared.UserDto;

import feign.FeignException;
@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	RestTemplate restTemplate;
	Environment env;
	AlbumServiceClient albumServiceClient; 
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, 
			RestTemplate restTemplate, Environment env, AlbumServiceClient albumServiceClient) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.restTemplate = restTemplate;
		this.env = env;
		this.albumServiceClient = albumServiceClient;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		// TODO Auto-generated method stub
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);

		userRepository.save(userEntity);

		return mapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if(userEntity == null) throw new UsernameNotFoundException(username);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
				true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);		
		if(userEntity == null) throw new UsernameNotFoundException(email);

		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserDetailsByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		String albumUrl = String.format(env.getProperty("albums.url"), userId);
		/*
		ResponseEntity<List<AlbumResponseModel>> albumResponse = restTemplate.exchange(albumUrl, 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
			});
		List<AlbumResponseModel> albumList = albumResponse.getBody();
		 */
		//Using Feign Client
		List<AlbumResponseModel> albumList = null;
		/*
		try {
		} catch (FeignException e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		 */	
		albumList = albumServiceClient.getAlbums(userId);

		userDto.setAlbumList(albumList);
		return userDto;
	}




}
