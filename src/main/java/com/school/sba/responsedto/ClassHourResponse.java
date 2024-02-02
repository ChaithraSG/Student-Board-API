package com.school.sba.responsedto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.school.sba.enums.ClassStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ClassHourResponse 
{
	private int classHourId;
	private LocalDateTime beginsAt;
	private LocalDateTime endsAt;
	private int roomNo;
	@Enumerated(EnumType.STRING)
	private ClassStatus classStatus;
}
