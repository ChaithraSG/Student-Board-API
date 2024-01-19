package com.school.sba.responsedto;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.school.sba.enums.ProgramType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AcademicProgramResponse
{
	private int programId;
	private String programName;
	private LocalTime beginsAt;
	private LocalTime endsAt;
	private ProgramType programType;
}
