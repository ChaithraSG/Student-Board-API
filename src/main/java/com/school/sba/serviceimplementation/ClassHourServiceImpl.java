package com.school.sba.serviceimplementation;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.ClassStatus;
import com.school.sba.enums.Userrole;
import com.school.sba.exception.AcademicProgramNotFoundByException;
import com.school.sba.exception.ClassHourNotFoundByIdException;
import com.school.sba.exception.DuplicateClassHourException;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.InvalidAacdemicProgramException;
import com.school.sba.exception.InvalidClassHourException;
import com.school.sba.exception.InvalidUserRoleException;
import com.school.sba.exception.ScheduleNotFoundBySchoolException;
import com.school.sba.exception.SubjectNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestdto.ClassHourDTOs;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService{

	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	
	@Autowired
	private ClassHourRepository classHourRepository;
	
	@Autowired
	private ResponseStructure<ClassHourResponse> responseStructure;
	
	@Autowired
	private UserRepository userRepository;
	
	public ClassHourResponse mapToClassHourResponse(ClassHour response)
	{
		return ClassHourResponse.builder()
				.classHourId(response.getClassHourId())
				.beginsAt(response.getBeginsAt())
				.endsAt(response.getEndsAt())
				.roomNo(response.getRoomNo())
				.classStatus(response.getClassStatus())
				.build();
	}
	
	private boolean isBreakTime(LocalDateTime beginsAt , LocalDateTime endsAt, Schedule schedule)
	{
		LocalTime breakTimeStart = schedule.getBreakTime();	
		
		return ((breakTimeStart.isAfter(beginsAt.toLocalTime()) && breakTimeStart.isBefore(endsAt.toLocalTime())) || breakTimeStart.equals(beginsAt.toLocalTime()));


	}
	
	private boolean isLunchTime(LocalDateTime beginsAt , LocalDateTime endsAt, Schedule schedule)
	{
		LocalTime lunchTimeStart = schedule.getLunchTime();
		
		return ((lunchTimeStart.isAfter(beginsAt.toLocalTime()) && lunchTimeStart.isBefore(endsAt.toLocalTime())) || lunchTimeStart.equals(beginsAt.toLocalTime()));

	}
	

	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHourForAcademicProgram(int programId) 
	{
		return academicProgramRepository.findById(programId)
				.map(academicProgram -> {
					if(academicProgram.isDeleted())
						throw new AcademicProgramNotFoundByException("Invalid program Id");
					
					School school = academicProgram.getSchool();
					Schedule schedule = school.getSchedule();
					if(schedule!=null)
					{
					    int classHoursPerDay = schedule.getClassHoursPerDay();
						int classHourLength = (int) schedule.getClassHourLengthinMinutes().toMinutes();
						
						LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());
						
						LocalDateTime LunchTimeStart = LocalDateTime .now().with(schedule.getLunchTime());
						LocalDateTime LunchTimeEnd = LunchTimeStart.plusMinutes(schedule.getLunchLengthinMinutes().toMinutes());
						LocalDateTime breakTimeStarts = LocalDateTime.now().with(schedule.getBreakTime());
						LocalDateTime breakTimeEnd = breakTimeStarts.plusMinutes(schedule.getBreakLengthinMinutes().toMinutes());
						
						int currentDayofWeek = currentTime.getDayOfWeek().getValue();
						for(int day=1;day<=((7-currentDayofWeek)+7);day++)
						{
							   if(currentTime.getDayOfWeek()!= DayOfWeek.SUNDAY) {
							   for(int hour =1;hour<=classHoursPerDay+2; hour++)
							   {
								ClassHour classhour = new ClassHour();
								LocalDateTime beginsAt = currentTime;
								LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);
								DayOfWeek dayofweek = beginsAt.getDayOfWeek();
								
								if(!isLunchTime(beginsAt,endsAt,schedule) && !isBreakTime(beginsAt, endsAt, schedule))
								{
									classhour.setBeginsAt(beginsAt);
									classhour.setEndsAt(endsAt);
									classhour.setClassStatus(ClassStatus.NOT_SCHEDULED);
									
									currentTime = endsAt;
								} 
								else if(!isBreakTime(beginsAt,endsAt,schedule))
									{
										classhour.setBeginsAt(breakTimeStarts);
										classhour.setEndsAt(breakTimeEnd);
										classhour.setClassStatus(ClassStatus.BREAK_TIME);
										currentTime = endsAt;
									}
								else
								{
									classhour.setBeginsAt(LunchTimeStart);
									classhour.setEndsAt(LunchTimeEnd);
									classhour.setClassStatus(ClassStatus.LUNCH_TIME);
									currentTime = endsAt;
								}
								classhour.setAcademiclist(academicProgram);
								classHourRepository.save(classhour);
							}
						 }
						currentTime = LocalDateTime.of(currentTime.toLocalDate().plusDays(1), schedule.getOpensAt());
						LunchTimeStart = LunchTimeStart.plusDays(1);
						LunchTimeEnd = LunchTimeEnd.plusDays(1);
						breakTimeStarts=breakTimeStarts.plusDays(1);
						breakTimeEnd = breakTimeEnd.plusDays(1);
							}
						}
				else
				{
					throw new ScheduleNotFoundBySchoolException("The school does not contain any schedule");
				}
					return new ResponseEntity<ResponseStructure<ClassHourResponse>>(responseStructure,HttpStatus.CREATED);
						
				}).orElseThrow(() -> new AcademicProgramNotFoundByException("Invalid Program Id"));
	    }
	
	
		@Override
		public ResponseEntity<String> updateClassHour(List<ClassHourDTOs> listofclassHourDTOs)
		{
			
			for(ClassHourDTOs classHourDTO : listofclassHourDTOs)
			{

				
				ClassHour classHour = classHourRepository.findById(classHourDTO.getClassHourId())
							.orElseThrow(()-> new ClassHourNotFoundByIdException("Invalid class Hour"));
						
				User user = userRepository
						.findById(classHourDTO.getUserId())
						.orElseThrow(()-> new UserNotFoundByIdException("user not found with id:"+ classHourDTO.getUserId()));
				
				if(user.getUserrole().equals(Userrole.TEACHER))
				{
					if(user.getAcademiclist().contains(classHour.getAcademiclist()))
					{
						LocalDateTime beginsAt = classHour.getBeginsAt();
						LocalDateTime endsAt = classHour.getEndsAt();
						int roomNo = classHourDTO.getClassRoomNumber();

						boolean isExist = classHourRepository.existsByRoomNoAndBeginsAtAndEndsAt( roomNo, beginsAt, endsAt);

						if(!isExist)
						{
							classHour.setSubject(user.getSubject());
							classHour.setUser(user);
							classHour.setRoomNo(roomNo);
							classHourRepository.save(classHour);
						}
						else 
							throw new DuplicateClassHourException("Another Class Hour already allotted for the same date and time in the given room.");

					}else
						throw new InvalidAacdemicProgramException("The user's Academic Program is not same as the Class Hour's Academic Program.");
					
				}
				else 
					throw new InvalidUserRoleException("Only Teachers can be alloted to a Class Hour");

			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}

			
}



