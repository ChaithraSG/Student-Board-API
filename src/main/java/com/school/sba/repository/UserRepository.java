package com.school.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.User;
import com.school.sba.enums.Userrole;

public interface UserRepository extends JpaRepository<User, Integer> 
{

	boolean existsByUserrole(Userrole admin);


}
