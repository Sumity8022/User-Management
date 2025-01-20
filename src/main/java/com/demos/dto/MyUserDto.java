package com.demos.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class MyUserDto {
	
	private int id;
	
	
	@NotEmpty(message = "first name is mandatory!!!")
	private String firstName;
	
	@NotEmpty(message = "last name is mandatory!!!")
	private String lastName;
	
	@NotEmpty(message = "Email is mandatory!!!")
	@Email(message = "invalid email format!!")
	private String email;
	
	@NotEmpty(message = "password is mandatory!!!")
	private String password;
	
	private String profilePhoto;
	
	private String mypdf;
	

	
	
	//this method is used to return new dynamic folder name
	
    @Transient
	public String getMyPhoto()
	{
		
	if(profilePhoto==null)
	{
		return null;
	}
    return "/myfiles/"+id+"/"+profilePhoto;	
		
	}
	
	
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public String getMypdf() {
		return mypdf;
	}
	public void setMypdf(String mypdf) {
		this.mypdf = mypdf;
	}








	
	
	

}
