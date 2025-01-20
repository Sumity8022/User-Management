package com.demos.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demos.entities.Role;
import java.util.List;


public interface RoleRepo  extends JpaRepository<Role, Integer>{
	
	public Role findByName(String name);

}
