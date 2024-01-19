package com.school.sba.serviceimplementation;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.exception.ScheduleIsPresentException;
import com.school.sba.exception.SchoolNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.ScheduleRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.util.ResponseStructure;

@Service
public class ScheduleServiceImpl implements ScheduleService
{
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private SchoolRepository schoolRepository;
	
	@Autowired
	private ResponseStructure<ScheduleResponse> responseStructure;
	
	private Schedule mapToScheduleRequest(ScheduleRequest request) {
		return Schedule.builder()
				.opensAt(request.getOpensAt())
				.closesAt(request.getClosesAt())
				.classHourLength(Duration.ofMinutes(request.getClassHourLengthInMins()))
				.lunchTime(request.getLunchTime())
				.lunchLength(Duration.ofMinutes(request.getLunchLengthInMins()))
				.breakTime(request.getBreakTime())
				.breakLength(Duration.ofMinutes(request.getBreakLengthInMins()))
				.classHoursPerDay(request.getClassHoursPerDay())
				.build();
	}
	private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .opensAt(schedule.getOpensAt())
                .closesAt(schedule.getClosesAt())
                .classHoursPerDay(schedule.getClassHoursPerDay())
                .classHourLengthInMins((int) schedule.getClassHourLength().toMinutes())
                .breakTime(schedule.getBreakTime())
                .breakLengthInMins((int) schedule.getBreakLength().toMinutes())
                .lunchTime(schedule.getLunchTime())
                .lunchLengthInMins((int) schedule.getLunchLength().toMinutes())
                .build();
    }
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> createSchedule(ScheduleRequest request, int schoolId) {
		School school=schoolRepository.findById(schoolId).orElseThrow(()-> new SchoolNotFoundException("Can't find any school in the given ID"));
		if(school.getSchedule()==null) {
			Schedule schedule=scheduleRepository.save(mapToScheduleRequest(request));
			school.setSchedule(schedule);
			schoolRepository.save(school);
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("New Schedule is created");
			responseStructure.setData(mapToScheduleResponse(schedule));
		}
		else {
			throw new ScheduleIsPresentException("Schedule is already present for this school");
		}
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(responseStructure,HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int scheduleId)
	{
		Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new UserNotFoundByIdException("failed to find user"));
		responseStructure.setStatus(HttpStatus.FOUND.value());
		responseStructure.setMessage("User found Successfully!!!");
		responseStructure.setData(mapToScheduleResponse(schedule));
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(responseStructure, HttpStatus.FOUND);
		
	}
	
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest scheduleRequest)
	{
		Schedule updateschedule = scheduleRepository.findById(scheduleId).map(u->{return scheduleRepository.save(mapToScheduleRequest(scheduleRequest));
		}).orElseThrow(()-> new UserNotFoundByIdException("Invalid UserId"));
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("user data updates successfully");
		responseStructure.setData(mapToScheduleResponse(updateschedule));
		
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(responseStructure, HttpStatus.OK);
	}
	
	
	
	
}


















