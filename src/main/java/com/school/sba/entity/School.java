package com.school.sba.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class School 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private long contectNo;
	private String emailId;
	private String Address;

	@OneToOne
	private Schedule schedule;
}