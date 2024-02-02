package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AcademicProgramNotFoundByException  extends RuntimeException{
	private String message;
}
