package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

@RestController
public class UserController
{
	@Autowired
	private UserService userservice;
	
	@Autowired
	private AcademicProgramService academicProgramService;
	
	@PostMapping(value="/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> registeruser(@RequestBody UserRequest userrequest)
	{
		return userservice.registeruser(userrequest);
	}
	
	@GetMapping(value="/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(@PathVariable int userId)
	{
		return userservice.findUserById(userId);
	}
	
	@PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('STUDENT')") 
	@DeleteMapping(value="/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> delete(@PathVariable int userId)
	{
		return userservice.delete(userId);
	}
	
	@GetMapping(value="/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> getRegisterUser(@RequestBody UserRequest userrequest)
	{
		return userservice.getRegisterUser(userrequest);
	}
	
	@PostMapping(value="/user/register")
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@RequestBody UserRequest userrequest)
	{
		return userservice.registerAdmin(userrequest);
		
	}
	
	@PostMapping("/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUsers(@RequestBody UserRequest userrequest)
	{
		return userservice.addOtherUsers(userrequest);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/subjects/{subjectId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>>addSubjectToTheTeacher(@PathVariable int subjectId,@PathVariable int userId)
	{
		return userservice.addSubjectToTheTeacher(subjectId,userId);
	}
	
	@GetMapping("/academic-programs/{programId}/user-roles/{role}/users")
	public ResponseEntity<ResponseStructure<List<UserResponse>>> getUsersByProgramIdAndRole(@PathVariable int programId, @PathVariable Userrole userRole) 
	{
        return userservice.getUsersByProgramIdandRole(programId, userRole);
        
	}
	
}
	







	
