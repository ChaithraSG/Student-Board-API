package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.Schedule;
import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.util.ResponseStructure;

public interface ScheduleService {

	ResponseEntity<ResponseStructure<ScheduleResponse>> createSchedule(ScheduleRequest request, int schoolId);

	ResponseEntity<ResponseStructure<ScheduleResponse>> findSchedule(int scheduleId);

	ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest scheduleRequest);

	ResponseEntity<ResponseStructure<ScheduleResponse>> deleteSchedule(int scheduleId, Schedule schedule);

}
