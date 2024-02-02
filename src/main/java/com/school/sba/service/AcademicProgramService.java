package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.util.ResponseStructure;

public interface AcademicProgramService 
{
	ResponseEntity<ResponseStructure<AcademicProgramResponse>> createAcademicProgram(AcademicProgramRequest academicProgramRequest, int schoolId);

	ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(int schoolId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> softdelete(int schoolId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteAcademicProgram(int programId);

}
