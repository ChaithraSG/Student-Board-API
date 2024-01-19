package com.school.sba.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.school.sba.enums.ProgramType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademicProgram 
{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int programId;
	private String programName;
	private LocalTime beginsAt;
	private LocalTime endsAt;
	private ProgramType programType;
	
	@ManyToOne
	private School school;
	
//	@ManyToMany
//	private List<User> userlist = new ArrayList<User>();
	
	//list<subject>
	
}
