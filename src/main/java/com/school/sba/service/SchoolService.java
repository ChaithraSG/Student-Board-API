package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.util.ResponseStructure;

public interface SchoolService
{

	ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(SchoolRequest schoolRequest, int userId);

	
	
}













































//	ResponseEntity<ResponseStructure<List<School>>> getAllSchool() ;
//
//	ResponseEntity<ResponseStructure<School>> saveSchool(School school);
//
//	ResponseEntity<ResponseStructure<School>> getSchoolById(School school);
//
//	ResponseEntity<ResponseStructure<School>> deleteSchoolById(School school);
//
//	ResponseEntity<ResponseStructure<School>> updateSchool(int id,School updateschool);

