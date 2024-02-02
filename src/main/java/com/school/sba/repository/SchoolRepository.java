package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
@Repository
public interface SchoolRepository extends JpaRepository<School,Integer>
{

	void save(AcademicProgram academicProgram);

	List<School> findAllByIsDeleted(boolean b);

}
