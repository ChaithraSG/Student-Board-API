package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@PostMapping(value="/users/{userId}/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(@RequestBody SchoolRequest schoolRequest, @PathVariable int userId)
	{
		return schoolService.saveSchool(schoolRequest);
		
	}
	
	@PutMapping(value="/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(@RequestBody SchoolRequest schoolRequest,@PathVariable int schoolId)
	{
		return schoolService.updateSchool(schoolId,schoolRequest);
	}
	@GetMapping(value="/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolByIdSchool(@PathVariable int schoolId)
	{
		return schoolService.findSchoolById(schoolId);
	}
	@DeleteMapping(value="/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(@PathVariable int schoolId)
	{
		return schoolService.deleteSchool(schoolId);
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