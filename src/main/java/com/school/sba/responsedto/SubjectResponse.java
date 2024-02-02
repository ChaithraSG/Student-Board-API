package com.school.sba.responsedto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SubjectResponse 
{
	private int subjectId;
	private String subjectName;
}
