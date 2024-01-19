package com.school.sba.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.exception.AdminAlreadyExistsException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserRepository userrepository;

	@Autowired
	private ResponseStructure<UserResponse> responsestructure;


	private UserResponse mapToUserResponse(User user)
	{
		return UserResponse.builder()
				.userId(user.getUserId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.contactNo(user.getContactNo())
				.username(user.getUsername())
				.email(user.getEmail())
				.userrole(user.getUserrole())
				.build();
	}
	private User mapToUser(UserRequest userrequest)
	{
		return User.builder()
				.username(userrequest.getUsername())
				.firstName(userrequest.getFirstName())
				.lastName(userrequest.getLastName())
				.contactNo(userrequest.getContactNo())
				.email(userrequest.getEmail())
				.password(userrequest.getPassword())
				.userrole(userrequest.getUserrole())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> register(UserRequest userrequest) {
		User user = userrepository.save(mapToUser(userrequest));
		responsestructure.setStatus(HttpStatus.CREATED.value());
		responsestructure.setMessage("User Data registered successfully");
		responsestructure.setData(mapToUserResponse(user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure , HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId)
	{
		User user= userrepository.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("failed to find user"));
		responsestructure.setStatus(HttpStatus.FOUND.value());
		responsestructure.setMessage("User found Successfully!!!");
		responsestructure.setData(mapToUserResponse(user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure, HttpStatus.FOUND);
	}


	public boolean isPresent(Userrole userrole) throws AdminAlreadyExistsException  
	{
		boolean existsByUserRole = userrepository.existsByUserrole(Userrole.ADMIN);
		if(existsByUserRole==true) 
		{
			throw new AdminAlreadyExistsException("can have only one admin");
		}
		return true;

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> getRegisterUser(UserRequest userRequest)
	{
		try {
			if(isPresent(Userrole.ADMIN)==false)
			{
				User getregisteruser = userrepository.save(mapToUser(userRequest));
				responsestructure.setStatus(HttpStatus.OK.value());
				responsestructure.setMessage("User data registered sucessfully!!!"); 
				responsestructure.setData (mapToUserResponse(getregisteruser));
			}
			else
			{
				throw new AdminAlreadyExistsException("can have only one admin");
			}
		} catch (AdminAlreadyExistsException e) {

			e.printStackTrace();
		}
		return new ResponseEntity<ResponseStructure<UserResponse>> (responsestructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> delete(int userId)
	{
		User user= userrepository.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("failed to delete user"));
		user.setIsDelete(true);
		userrepository.delete(user);
		responsestructure.setStatus(HttpStatus.FOUND.value());
		responsestructure.setMessage("User deleted Successfully!!!");
		responsestructure.setData (mapToUserResponse (user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure, HttpStatus.FOUND);
	}

}
