package com.myapp.user.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.user.model.CreateUserRequestModel;
import com.myapp.user.model.CreateUserResponseModel;
import com.myapp.user.service.UserService;
import com.myapp.user.shared.UserDto;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	Environment env;

	@Autowired
	UserService userService;
	
	@GetMapping("/status/check")
	public String status() {
		return "User service working at : " + env.getProperty("local.server.port") + " with Token: " + env.getProperty("token.secret");
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
				produces = 	{MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel request) {
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDetails = mapper.map(request, UserDto.class);
		
		UserDto createUser = userService.createUser(userDetails);
		CreateUserResponseModel response = mapper.map(createUser, CreateUserResponseModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
