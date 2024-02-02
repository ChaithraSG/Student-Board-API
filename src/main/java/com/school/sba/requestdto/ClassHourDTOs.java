package com.school.sba.requestdto;

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
public class ClassHourDTOs 
{
	private int schoolId;
	private int userId;// consider as teacher id
	private int classHourId;
	private int classRoomNumber;
	public Object getDayOfWeek() {
		// TODO Auto-generated method stub
		return null;
	}
}
