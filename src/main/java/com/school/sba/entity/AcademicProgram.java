package com.school.sba.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.school.sba.enums.ProgramType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private boolean isDeleted;
	
	@ManyToOne
	private School school;
	
	@ManyToMany(mappedBy = "academiclist", fetch = FetchType.EAGER)
	private List<User> userlist = new ArrayList<User>();
	
	@ManyToMany(mappedBy="academiclist")
	private List<Subject> subjectlist = new ArrayList<Subject>() ;
	
	@OneToMany(mappedBy = "academiclist", fetch = FetchType.EAGER)
	private List<ClassHour> classHours;
	
}
