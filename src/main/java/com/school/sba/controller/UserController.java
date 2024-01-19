package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

@RestController
public class UserController
{
	@Autowired
	private UserService userservice;
	
	@PostMapping(value="/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> register(@RequestBody UserRequest userrequest)
	{
		return userservice.register(userrequest);
	}
	
	@GetMapping(value="/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(@PathVariable int userId)
	{
		return userservice.findUserById(userId);
	}
	
	@DeleteMapping(value="/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> delete(@PathVariable int userId)
	{
		return userservice.delete(userId);
	}
	
	@GetMapping(value="/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> getRegisterUser(UserRequest userrequest)
	{
		return userservice.getRegisterUser(userrequest);
	}
}
	







	
