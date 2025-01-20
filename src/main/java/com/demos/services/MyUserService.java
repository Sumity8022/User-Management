package com.demos.services;

import java.io.IOException;
import java.util.List;

import com.demos.dto.MyUserDto;

import jakarta.servlet.http.HttpServletResponse;

public interface MyUserService {
	
	//to save myuser data
	public MyUserDto saveUser(MyUserDto dto);
	
	//find specific user based on email id
	public MyUserDto findUserByEmail(String email);
	
	//to get all list of users
	public List<MyUserDto> getAllUsers();
	
	
	//for excel export
	public void generateExcel(HttpServletResponse response) throws IOException;
	
	//for pdf export
	public void generatePdf(HttpServletResponse response) throws Exception;

}
