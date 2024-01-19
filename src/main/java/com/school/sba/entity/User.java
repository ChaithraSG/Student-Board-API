package com.school.sba.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.school.sba.enums.Userrole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Component
public class User 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userId;
	@Column(unique=true)
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	@Column(unique=true)
	private long contactNo;
	@Column(unique=true)
	private String email;
	private Userrole userrole;
	private Boolean isDelete;
	
	@ManyToOne
	private School school;
	
	@ManyToMany
	private List<AcademicProgram> academiclist = new ArrayList<AcademicProgram>();
	
}
