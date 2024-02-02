package com.school.sba.serviceimplementation;

import java.util.ArrayList;
import java.util.List;

import com.school.sba.exception.AcademicProgramNotFoundByException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.SubjectResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@Service
public class SubjectServiceImpl implements SubjectService 
{
	@Autowired
	private AcademicProgramRepository academicProgramRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private ResponseStructure<AcademicProgramResponse> responseStructure;

	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl;

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(int programId,
			SubjectRequest subjectRequest)
	{
		return academicProgramRepository.findById(programId).map(program->{ //found academic program
			List<Subject>subjects= (program.getSubjectlist()!= null)?program.getSubjectlist(): new ArrayList<Subject>();

			//to add new subjects that are specified by the client
			subjectRequest.getSubjectName().forEach(name->{
				boolean isPresent =false;
				for(Subject subject:subjects) {
					isPresent = (name.equalsIgnoreCase(subject.getSubjectName()))?true:false;
					if(isPresent)break;
				}
				if(!isPresent)subjects.add(subjectRepository.findBySubjectName(name)
						.orElseGet(()-> subjectRepository.save(Subject.builder().subjectName(name).build())));
			});
			//to remove the subjects that are not specified by the client
			List<Subject>toBeRemoved= new ArrayList<Subject>();
			subjects.forEach(subject->{
				boolean isPresent = false;
				for(String name:subjectRequest.getSubjectName()) {
					isPresent=(subject.getSubjectName().equalsIgnoreCase(name))?true :false;
					if(!isPresent)break;
				}
				if(!isPresent)toBeRemoved.add(subject);
			});
			subjects.removeAll(toBeRemoved);


			program.setSubjectlist(subjects);//set subjects list to the academic program
			academicProgramRepository.save(program);//saving updated program to the database
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("updated the subject list to academic program");
			responseStructure.setData(academicProgramServiceImpl.mapToAcademicProgramResponse(program));
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(responseStructure,HttpStatus.CREATED);
		}).orElseThrow(()-> new AcademicProgramNotFoundByException("AcademicProgram not found"));

	}
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubject(SubjectRequest subjectRequest,
	        int programId) {

	    return academicProgramRepository.findById(programId).map(program -> {
	        // Remove the existing subjects for the program
	        program.getSubjectlist().clear();
	        academicProgramRepository.save(program);

	       return updateSubject(subjectRequest, programId);

	    }).orElseThrow(() -> new AcademicProgramNotFoundByException("Academic program Not found for given id"));

	}

}
















