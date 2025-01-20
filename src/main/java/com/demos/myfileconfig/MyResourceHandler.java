package com.demos.myfileconfig;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyResourceHandler  implements WebMvcConfigurer {
	
	//foldername where all images/pdf will stores
	private static final String UPLOAD_DIR="myfiles";
	
	//myfiles/......

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		Path path = Paths.get(UPLOAD_DIR);
		
		
		//this line will use myfiles folder and also detects myfile folder location in project 
		registry.addResourceHandler("/"+UPLOAD_DIR+"/**").addResourceLocations("file:"+path.toAbsolutePath()+"/");
		
		System.out.println("...."  +path.toAbsolutePath());
		
	}
	
	
	
	

}
