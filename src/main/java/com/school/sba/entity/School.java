package com.school.sba.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class School 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private long contectNo;
	private String emailId;
	private String Address;
	private boolean isDeleted;

	@OneToOne
	@JoinColumn(name = "scheduleId")
	private Schedule schedule;
	
	@OneToMany(mappedBy="school", fetch = FetchType.EAGER)
	private List<User> users;
	
	@OneToMany(mappedBy="school", fetch = FetchType.EAGER)
	private List<AcademicProgram> academiclist;
	
	
}