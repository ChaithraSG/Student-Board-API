package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;


@RestController
public class AcademicProgramController 
{
	@Autowired
	private AcademicProgramService academicProgramService;
	
	
	@PostMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> createAcademicProgram(@RequestBody AcademicProgramRequest academicProgramRequest,@PathVariable int schoolId)
	{
		return academicProgramService.createAcademicProgram(academicProgramRequest, schoolId);
		
	}
	
	@GetMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(@PathVariable int schoolId)
	{
		return academicProgramService.findAllAcademicPrograms(schoolId);
	}
	
	@DeleteMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> softdelete(@PathVariable int schoolId)
	{
		return academicProgramService.softdelete(schoolId);
	}
	
}
