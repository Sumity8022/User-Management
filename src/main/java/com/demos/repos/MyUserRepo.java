package com.demos.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demos.entities.MyUser;

public interface MyUserRepo extends JpaRepository<MyUser, Integer> {
	
	
              public MyUser findByEmail(String email);

}
