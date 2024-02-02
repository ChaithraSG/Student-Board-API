package com.school.sba.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.responsedto.UserResponse;

@Component
public class ResponseStructure<T>
{
	private int status;
	private String message;
	private T data;
	private AcademicProgram Data;
	
	
	public void setData(AcademicProgram data) {
		Data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
		
		
	}
	

