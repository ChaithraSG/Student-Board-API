package com.school.sba.requestdto;

import com.school.sba.enums.Userrole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest
{
	private String username;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private String password;
	private Userrole userrole;
}
