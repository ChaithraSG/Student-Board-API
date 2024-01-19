package com.school.sba.responsedto;

import com.school.sba.enums.Userrole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse 
{
	private int userId;
	private String username;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private Userrole userrole;
	private Boolean isDelete;
}
