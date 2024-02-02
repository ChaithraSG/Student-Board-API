package com.school.sba.serviceimplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;
import com.school.sba.exception.AcademicProgramNotFoundByException;
import com.school.sba.exception.InvalidRequestException;
import com.school.sba.exception.NoUserAssociated;
import com.school.sba.exception.ScheduleIsPresentException;
import com.school.sba.exception.SchoolNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

@Service
public class AcademicProgramServiceImpl  implements AcademicProgramService
{
	@Autowired
	private AcademicProgramRepository academicRepository;

	@Autowired
	private SchoolRepository schoolRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ClassHourRepository classHourRepository;

	@Autowired
	private ResponseStructure<AcademicProgramResponse> responseStructure;

	private AcademicProgram mapToAcademicProgramRequest(AcademicProgramRequest academicProgramRequest)
	{
		return AcademicProgram.builder()
				.programName(academicProgramRequest.getProgramName())
				.beginsAt(academicProgramRequest.getBeginsAt())
				.programType(academicProgramRequest.getProgramType())
				.endsAt(academicProgramRequest.getEndsAt())
				.build();
	}

	AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram academicProgram)
	{
		return AcademicProgramResponse.builder()
				.programId(academicProgram.getProgramId())
				.programName(academicProgram.getProgramName())
				.beginsAt(academicProgram.getBeginsAt())
				.endsAt(academicProgram.getEndsAt())
				.programType(academicProgram.getProgramType())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> createAcademicProgram(AcademicProgramRequest request, int schoolId)
	{	
		School school=schoolRepository.findById(schoolId).orElseThrow(()->new SchoolNotFoundException("School not found in the given Id"));
		AcademicProgram academicProgram =mapToAcademicProgramRequest(request);
		academicProgram.setSchool(school);
		academicRepository.save(academicProgram);
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Academic program created for school");
		responseStructure.setData(mapToAcademicProgramResponse(academicProgram));
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(responseStructure,HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(int schoolId) {
		
		return schoolRepository.findById(schoolId).map(school->{
			List<AcademicProgram> list=school.getAcademiclist();
			ResponseStructure<List<AcademicProgramResponse>> rs=new ResponseStructure<>();
			List<AcademicProgramResponse> l=new ArrayList<>();
			
			for(AcademicProgram obj:list) {
				l.add(mapToAcademicProgramResponse(obj));
			}
			rs.setStatus(HttpStatus.FOUND.value());
			rs.setMessage("Academic program's found");
			rs.setData(l);
			return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(rs,HttpStatus.FOUND);
		}).orElseThrow(()->new SchoolNotFoundException("School not found in the given Id"));
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> softdelete(int schoolId) {
		AcademicProgram academicProgram = academicRepository.findById(schoolId)
				.orElseThrow(() -> new AcademicProgramNotFoundByException("academic program not found"));
		academicProgram.setDeleted(true);
		academicRepository.save(academicProgram);
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("academic program deleted successfully");
		responseStructure.setData(mapToAcademicProgramResponse(academicProgram));
		return null;
	}
	

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteAcademicProgram(int programId)
	{
		return academicRepository.findById(programId).map(acedemicProgram ->{
			if(acedemicProgram.isDeleted())
				throw new AcademicProgramNotFoundByException("Invalid academic program");
			
			acedemicProgram.setDeleted(true);
			academicRepository.save(acedemicProgram);
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(HttpStatus.OK);
		}).orElseThrow(()-> new AcademicProgramNotFoundByException("invalid academic program"));			
				
	}

	public void deleteSoftDeletedData()
	{
		List<AcademicProgram> academicProgramsToDelete = academicRepository.findByIsDeleted(true);
		
		if(!academicProgramsToDelete.isEmpty())
			academicRepository.deleteAll(academicProgramsToDelete);
		
	}
	
}
	

