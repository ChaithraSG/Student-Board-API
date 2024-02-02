package com.school.sba.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Subject 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int subjectId;
	private String subjectName;
	
	@ManyToMany
	private List<AcademicProgram> academiclist= new ArrayList<AcademicProgram>();
	
	@OneToMany(mappedBy = "subject")
	private List<ClassHour> classHours;
	
	@OneToMany(mappedBy = "subject")
	private List<User> users;
	
}
