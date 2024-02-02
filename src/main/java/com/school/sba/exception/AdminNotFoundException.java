package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminNotFoundException extends RuntimeException {
	private String message; 
}
