package com.school.sba.serviceimplementation;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.exception.AcademicProgramNotFoundByException;
import com.school.sba.exception.AdminAlreadyExistsException;
import com.school.sba.exception.AdminNotFoundException;
import com.school.sba.exception.InvalidRequestException;
import com.school.sba.exception.NoUserAssociated;
import com.school.sba.exception.OnlyTeacherCanBeAssignedToSubjectException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.exception.UserObjectNotFoundException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private ResponseStructure<UserResponse> responsestructure;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private AcademicProgramRepository academicProgramRepository;

	@Autowired
	private ClassHourRepository classHourRepository;

	@Autowired
	private ResponseStructure<List<UserResponse>> listStructure;


	private UserResponse mapToUserResponse(User user)
	{
		return UserResponse.builder()
				.userId(user.getUserId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.contactNo(user.getContactNo())
				.username(user.getUserName())
				.email(user.getEmail())
				.userrole(user.getUserrole())
				.build();
	}
	private User mapToUser(UserRequest userrequest)
	{
		return User.builder()
				.userName(userrequest.getUsername())
				.firstName(userrequest.getFirstName())
				.lastName(userrequest.getLastName())
				.contactNo(userrequest.getContactNo())
				.email(userrequest.getEmail())
				.password(passwordEncoder.encode(userrequest.getPassword()))
				.userrole(userrequest.getUserrole())
				.build();
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registeruser(UserRequest userrequest) {
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
		user.setDeleted(true);	
		userrepository.save(user);
		responsestructure.setStatus(HttpStatus.FOUND.value());
		responsestructure.setMessage("User deleted Successfully!!!");
		responsestructure.setData (mapToUserResponse (user));
		return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure, HttpStatus.FOUND);
	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest) 
	{
		if(userRequest.getUserrole().equals(Userrole.ADMIN))
		{
			if(userrepository.existsByUserrole(userRequest.getUserrole())==false)
			{
				User user = userrepository.save(mapToUser(userRequest));

				responsestructure.setStatus(HttpStatus.CREATED.value());
				responsestructure.setMessage("admine saved successfully");
				responsestructure.setData(mapToUserResponse(user));

				return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure,HttpStatus.CREATED);
			}
			else
			{
				throw new AdminAlreadyExistsException("Admin already existed");
			}
		}
		else
		{
			throw new AdminNotFoundException("admin not found");
		}

	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUsers(UserRequest userRequest) 
	{
		String username=SecurityContextHolder.getContext().getAuthentication().getName();



		if(userRequest.getUserrole().equals(Userrole.ADMIN))
		{
			throw new AdminNotFoundException("admin not allowed");
		}
		else
		{
			if(userrepository.existsByUserrole(Userrole.ADMIN)==true)
			{
				Optional<User> optional = userrepository.findByUserName(username);
				User user = optional.get();
				School school = user.getSchool();


				user = userrepository.save(mapToUser(userRequest));
				user.setSchool(school);
				User save = userrepository.save(user);



				responsestructure.setStatus(HttpStatus.CREATED.value());
				responsestructure.setMessage("users saved successfully");
				responsestructure.setData(mapToUserResponse(save));


				return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure,HttpStatus.CREATED);
			}
			else
			{
				throw new AdminNotFoundException("admin not found ");
			}
		}

	}


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTheTeacher(int subjectId, int userId) 
	{
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(()-> new SubjectNotFoundException("subject not found"));

		User user = userrepository.findById(userId)
				.orElseThrow(()-> new UserObjectNotFoundException("user not found"));

		if(user.getUserrole().equals(Userrole.TEACHER))
		{

			user.setSubject(subject);
			userrepository.save(user);

			responsestructure.setStatus(HttpStatus.OK.value());
			responsestructure.setMessage("subject added to the teacher successfully");
			responsestructure.setData(mapToUserResponse(user));

			return new ResponseEntity<ResponseStructure<UserResponse>>(responsestructure,HttpStatus.OK);

		}
		else
		{
			throw new OnlyTeacherCanBeAssignedToSubjectException("user is not a teacher");
		}
	}


	@Override
	public ResponseEntity<ResponseStructure<List<UserResponse>>> getUsersByProgramIdandRole(int programId, Userrole userRole)
	{
		Userrole role = userRole.valueOf(userRole.toUpperCase());
		if(role==userRole.ADMIN) {
			throw new InvalidRequestException("Admin role is not allowed");
		}
		List<User> users = userrepository.findByacademiclist_ProgramIdAndUserrole(programId, role);
		if(users.isEmpty())
		{
			throw new NoUserAssociated("No user associated with the provided programId and userrole");
		}
		List<UserResponse> userResponse = users.stream()
				.map(u -> mapToUserResponse(u))
				.collect(Collectors.toList());
		listStructure.setStatus(HttpStatus.FOUND.value());
		listStructure.setMessage("Teachers fetched successfully");
		listStructure.setData(userResponse);
		return new ResponseEntity<ResponseStructure<List<UserResponse>>>(listStructure, HttpStatus.ACCEPTED);
	}


	public void deleteSoftDeletedData() 
	{ 
		List<User> usersToDelete = new ArrayList<User>();
		
		userrepository.findAllByIsDeleted(true)
		.forEach(user ->
		{
			user.getClassHours().forEach(classHour -> classHour.setUser(null));
			classHourRepository.saveAll(user.getClassHours());
			
			user.getAcademiclist().forEach(academicProgram -> academicProgram.setUserlist(null));
			academicProgramRepository.saveAll(user.getAcademiclist());
			
			usersToDelete.add(user);
		});
		userrepository.deleteAll(usersToDelete);

	}


}
