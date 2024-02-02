package com.school.sba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.enums.Userrole;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.serviceimplementation.AcademicProgramServiceImpl;
import com.school.sba.serviceimplementation.SchoolServiceImpl;
import com.school.sba.serviceimplementation.UserServiceImpl;

@Component
public class ScheduledJobs 
{
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl;
	
	@Autowired
	private SchoolServiceImpl schoolServiceImpl;
	
	@Scheduled(fixedDelay = 1000l)
	public void test()
	{
		userServiceImpl.deleteSoftDeletedData();
		academicProgramServiceImpl.deleteSoftDeletedData();
		schoolServiceImpl.deleteSoftDeletedData();
	}

	
}
