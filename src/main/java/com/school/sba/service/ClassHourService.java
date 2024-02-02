package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ClassHourDTOs;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.util.ResponseStructure;

public interface ClassHourService 
{
	ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHourForAcademicProgram(int programId);

	ResponseEntity<String> updateClassHour(List<ClassHourDTOs> listofclassHourDTOs);
}
