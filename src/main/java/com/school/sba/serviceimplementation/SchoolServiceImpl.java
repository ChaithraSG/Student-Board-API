package com.school.sba.serviceimplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.enums.Userrole;
import com.school.sba.exception.UnauthorisedException;
import com.school.sba.exception.UserNotFoundByIdException;
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
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(@Valid SchoolRequest schoolRequest, int userId) {
		return userRepository.findById(userId)
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


}

	































































































	//	public ResponseEntity<ResponseStructure<List<School>>> getAllSchool()
	//	{
	//		List<School> findAll = schoolRepository.findAll();
	//		if (!findAll.isEmpty()) 
	//		{
	//			listResponseStructure.setStatus(HttpStatus.OK.value());
	//			listResponseStructure.setMessage("School data Found!!!");
	//			listResponseStructure.setData(findAll);
	//			return new ResponseEntity<ResponseStructure<List<School>>>(listResponseStructure, HttpStatus.OK);
	//		} else {
	//			listResponseStructure.setStatus(HttpStatus.NOT_FOUND.value());
	//			listResponseStructure.setMessage("School data Not Found !!!");
	//			return new ResponseEntity<ResponseStructure<List<School>>>(listResponseStructure, HttpStatus.NOT_FOUND);
	//		}
	//	}
	//
	//	public ResponseEntity<ResponseStructure<School>> saveSchool(School school) 
	//	{
	//		School sch = schoolRepository.save(school);
	//		if ((sch != null))
	//		{
	//			responseStructure.setStatus(HttpStatus.FOUND.value());
	//			responseStructure.setMessage("School data saved !!!");
	//			responseStructure.setData(sch);
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.FOUND);
	//		} else
	//			return null;
	//	}
	//
	//	public ResponseEntity<ResponseStructure<School>> getSchoolById(School school)
	//	{
	//		Optional<School> findById = schoolRepository.findById(school.getSchoolId());
	//		if (findById.isPresent()) 
	//		{
	//			responseStructure.setStatus(HttpStatus.OK.value());
	//			responseStructure.setMessage("School data Found!!!");
	//			responseStructure.setData(findById.get());
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.OK);
	//		} else {
	//			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
	//			responseStructure.setMessage("School data Not Found !!!");
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.NOT_FOUND);
	//		}
	//	}
	//
	//	public ResponseEntity<ResponseStructure<School>> updateSchool(int id, School updateschool)
	//	{
	//		Optional<School> findById = schoolRepository.findById(id);
	//		if (findById.isPresent()) 
	//		{
	//			School exStd = findById.get();
	//			exStd = mapBySchool(exStd, updateschool);
	//			schoolRepository.save(exStd);
	//			responseStructure.setStatus(HttpStatus.OK.value());
	//			responseStructure.setMessage("School data Updated !!!");
	//			responseStructure.setData(exStd);
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.OK);
	//		} else {
	//			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
	//			responseStructure.setMessage("School data Not Found !!!");
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.NOT_FOUND);
	//		}
	//	}
	//
	//	private School mapBySchool(School exStd, School updateSchool)
	//	{
	//		exStd.setSchoolName(updateSchool.getSchoolName());
	//		exStd.setEmailId(updateSchool.getEmailId());
	//		exStd.setSchedule(updateSchool.getSchedule());
	//		exStd.setContectNo(updateSchool.getContectNo());
	//		exStd.setAddress(updateSchool.getAddress());
	//		return exStd;
	//	}
	//
	//	public ResponseEntity<ResponseStructure<School>> deleteSchoolById(School school) 
	//	{
	//		Optional<School> findById = schoolRepository.findById(school.getSchoolId());
	//		if (findById.isPresent())
	//		{
	//			responseStructure.setStatus(HttpStatus.GONE.value());
	//			responseStructure.setMessage("School data Deleted !!!");
	//			responseStructure.setData(findById.get());
	//			schoolRepository.deleteById(school.getSchoolId());
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.GONE);
	//		} else {
	//			responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
	//			responseStructure.setMessage("School data Not Found !!!");
	//			return new ResponseEntity<ResponseStructure<School>>(responseStructure, HttpStatus.NOT_FOUND);
	//		}
	//	}


