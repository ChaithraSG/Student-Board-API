package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

@RestController
public class SchoolController
{
	@Autowired
	private SchoolService schoolService;
	
	@PostMapping(value="users/{userId}/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(@RequestBody SchoolRequest schoolRequest, @PathVariable int userId)
	{
		return schoolService.saveSchool(schoolRequest, userId);
		
	}

}
































//	
//	@PostMapping(value = "/save")
//	public ResponseEntity<ResponseStructure<School>> saveStudent(@RequestBody School school) {
//		return schoolService.saveSchool(school);
//	}
//	
//	@GetMapping(value = "/getAll")
//	public ResponseEntity<ResponseStructure<List<School>>> getAllStudent() {
//		return schoolService.getAllSchool();
//	}
//
//	@PostMapping(value = "/delete")
//	public ResponseEntity<ResponseStructure<School>> deleteStudent(@RequestBody School school) {
//		return schoolService.deleteSchoolById(school);
//	}
//	
//	@PutMapping(value = "/update/{id}")
//	public ResponseEntity<ResponseStructure<School>> updateStudent(@PathVariable int id,@RequestBody School school) {
//		return schoolService.updateSchool(id,school);
//	}
//
//	@GetMapping(value = "/getById")
//	public ResponseEntity<ResponseStructure<School>> getStudentById(@RequestBody School school) {
//		return schoolService.getSchoolById(school);
//
//	}
//


//receive request and send response