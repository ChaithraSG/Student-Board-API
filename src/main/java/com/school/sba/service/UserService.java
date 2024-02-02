package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.util.ResponseStructure;

public interface UserService 
{

	ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> delete(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> getRegisterUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> addOtherUsers(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> registeruser(UserRequest userrequest);

	ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTheTeacher(int subjectId, int userId);

//	ResponseEntity<ResponseStructure<List<User>>> getUsersByProgramIdAndRole(int programId, Userrole userRole);

	ResponseEntity<ResponseStructure<List<UserResponse>>> getUsersByProgramIdandRole(int programId, Userrole userRole);
}

