package com.myapp.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.myapp.user.shared.UserDto;

public interface UserService extends UserDetailsService{
	public UserDto createUser(UserDto userDto);
	public UserDto getUserDetailsByEmail(String email);
}
