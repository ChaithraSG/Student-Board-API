package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserObjectNotFoundException extends RuntimeException
{
	private String message;
}
