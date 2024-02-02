package com.school.sba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;

public interface UserRepository extends JpaRepository<User, Integer> 
{

	boolean existsByUserrole(Userrole admin);

	Optional<User> findByUserName(String username);

	User findByUserrole(Userrole admin);

	List<User> findByacademiclist_ProgramIdAndUserrole(int programId, Userrole userRole);

	List<User> findBySchool(School school);

	List<User> findAllByIsDeleted(boolean b);


}
