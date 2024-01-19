package com.school.sba.requestdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRequest 
{
	@NotBlank(message="School name is required")
	private String schoolName;
	private long contectNo;
	private String emailId;
	@NotBlank(message="School address is required")
	private String Address;
}
