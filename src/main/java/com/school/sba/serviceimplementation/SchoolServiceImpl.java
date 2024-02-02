package com.school.sba.serviceimplementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.UnauthorisedException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ScheduleRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

import jakarta.validation.Valid;

@Service
public class SchoolServiceImpl implements SchoolService
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	
	@Autowired
	ResponseStructure<SchoolResponse> responseStructure;

	private School mapToSchool(SchoolRequest schoolRequest)
	{
		return School.builder()
				.schoolName(schoolRequest.getSchoolName())
				.contectNo(schoolRequest.getContectNo())
				.emailId(schoolRequest.getEmailId())
				.Address(schoolRequest.getAddress())
				.build();
	}

	private SchoolResponse mapToSchoolResponse(School school)
	{
		return SchoolResponse.builder()
				.schoolId(school.getSchoolId())
				.schoolName(school.getSchoolName())
				.contectNo(school.getContectNo())
				.emailId(school.getEmailId())
				.Address(school.getAddress())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(@Valid SchoolRequest schoolRequest) {
		String username= SecurityContextHolder.getContext().getAuthentication().getName();//accessing user name

		return userRepository.findByUserName(username)
				.map(u -> {
					if(u.getUserrole().equals(Userrole.ADMIN))
					{
						if(u.getSchool() == null)
						{
							School school=mapToSchool(schoolRequest);
							school=schoolRepository.save(school);
							u.setSchool(school);
							userRepository.save(u);
							responseStructure.setStatus(HttpStatus.CREATED.value());
							responseStructure.setMessage("School saved successfully!!!");
							responseStructure.setData(mapToSchoolResponse(school));
							return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.CREATED);
						}
						else {
							throw new IllegalArgumentException("Cannot create more than one school");
						}
					}
					else {
						throw new UnauthorisedException("Only admin can create school");
					}
				}).orElseThrow(() -> new UserNotFoundByIdException("Faild to create School"));
	}


	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(Integer schoolId, SchoolRequest schoolRequest)
			throws SchoolNotFoundByIdException {

		return schoolRepository.findById(schoolId)
				.map( school -> {
					school = mapToSchool(schoolRequest);
					school.setSchoolId(schoolId);
					school = schoolRepository.save(school);

					responseStructure.setStatus(HttpStatus.OK.value());
					responseStructure.setMessage("School data updated successfully in database");
					responseStructure.setData(mapToSchoolResponse(school));

					return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.OK);
				})
				.orElseThrow(() -> new SchoolNotFoundByIdException("school object cannot be updated due to absence of technical problems"));

	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId) {

		School s=schoolRepository.findById(schoolId).orElseThrow(()-> new SchoolNotFoundByIdException("School not found in the given id"));
		responseStructure.setStatus(HttpStatus.FOUND.value());
		responseStructure.setMessage("School found successfully!!!");
		responseStructure.setData(mapToSchoolResponse(s));
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		School school=schoolRepository.findById(schoolId)
				.orElseThrow(()-> new  SchoolNotFoundByIdException("Failed to delete the school by id"));
		school.setDeleted(true);
		schoolRepository.save(school);
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("School deleted successfully");
		responseStructure.setData(mapToSchoolResponse(school));

		return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.OK);
	}

	public void deleteSoftDeletedData()
	{
		schoolRepository.findAllByIsDeleted(true)
		.forEach(school ->
		{
			if(!school.getAcademiclist().isEmpty())
				academicProgramRepository.deleteAll(school.getAcademiclist());

			List<User> usersToDelete = school.getUsers().stream()
					.peek(user -> {
						if(user.getUserrole().equals(Userrole.ADMIN))
						{
							user.setSchool(null);
							userRepository.save(user);
						}
					})
					.filter(user -> !user.getUserrole().equals(Userrole.ADMIN))
					.collect(Collectors.toList());

			userRepository.deleteAll(usersToDelete);

			schoolRepository.delete(school);
		});
	}
		
	}
