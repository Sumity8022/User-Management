package com.demos.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demos.dto.MyUserDto;
import com.demos.services.MyUserService;

import helper.FileDownloadHelper;
import helper.FileUploadHelper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Controller
public class UserController {
	
	@Autowired
	private MyUserService service;
	
	//localhost:8080/
	//method to display home page
	
	@GetMapping("/")
	public String home(Model model)
	{
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegisterFormPage(Model model)
	{
		
		MyUserDto dto=new MyUserDto();
		
		model.addAttribute("user", dto);
		
		return "register";
		
		
	}
	
	//to save data
	
	@PostMapping("/save")
	public String saveNewUser(@Valid @ModelAttribute("user")  MyUserDto myUserDto,BindingResult result,Model model,
			@RequestParam("image") MultipartFile image,@RequestParam("pdf") MultipartFile pdf)
	{
		//get existing email from db and check that email is already exists or not
		MyUserDto existingUser = service.findUserByEmail(myUserDto.getEmail());
		
		
		//reject form value
		if(existingUser!=null && existingUser.getEmail()!=null && !existingUser.getEmail().isEmpty())
		{
			
		result.rejectValue("email", "email is already present !! try some other email!!!");	
			
			
		}
		

		
		//check form field 
		if(result.hasErrors())
		{
			model.addAttribute("user", myUserDto);
			return "redirect:/register?fail";
		}
		
		
		//file upload logic starts
		
		String imageName="";
		String pdfName="";
		
		
		//for image handling
		if(!image.getOriginalFilename().equals(""))
		{
			//first we will clean filename if there is any improper charcter is there
			
			imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
			myUserDto.setProfilePhoto(imageName);
			
		}
		
		//for pdf handling
		
		if(!pdf.getOriginalFilename().equals(""))
		{
			//first we will clean filename if there is any improper charcter is there
			
			pdfName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
			myUserDto.setMypdf(pdfName);
			
		}
		
		
		//file upload logic end
		//object is saved
		MyUserDto saveUser = service.saveUser(myUserDto);
		
		//now time to save image and pdf to myfiles folder
		try
		{
		
			String uploadedDirectory="myfiles/"+saveUser.getId();//   /myfiles/12/....
			
			if(!image.getOriginalFilename().equals(""))
			{
				
				FileUploadHelper.saveFile(uploadedDirectory, imageName, image);
			}
			
			if(!pdf.getOriginalFilename().equals(""))
			{
				
				FileUploadHelper.saveFile(uploadedDirectory, pdfName, pdf);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		
		if(saveUser!=null)
		{
			//redirect to same page with parameter
			return "redirect:/register?success";
		}else
		{
			//redirect to same page with parameter
			return "redirect:/register?fail";
		}
		
		
		
		
	}
	
	
	
	
	//to display all users
	@GetMapping("/allusers")
	public String getallUsers(Model model)
	{
		//call service method to get all data
		List<MyUserDto> users = service.getAllUsers();
		
		
		model.addAttribute("allusers", users);
		
		
		return "user";
	}
	
	
	
	
	//to display login
	@GetMapping("/customlogin")
  public String customLoginPage()
  {
		return "login";
  }
	
	
	
	
	@GetMapping("/mydashboard")
	public String myDahsboard() {
		return "dashboard";
	}
	
	
	
	
	//to download pdf file 
	@GetMapping("/downloadpdf")
	public ResponseEntity<?> downloadPdfFile(@RequestParam String fileName,@RequestParam String userId)
	{
		FileDownloadHelper fileDownloadHelper=new FileDownloadHelper();
		
		Resource resource=null;
		
		try
		{
			
			Path path = Paths.get("myfiles/"+userId);
			
			System.out.println(path.getNameCount());
			System.out.println(path.getFileName());
			System.out.println(path.getFileSystem());
			System.out.println(path.getParent());
			
			resource = fileDownloadHelper.getFileResource("myfiles/"+userId, fileName);
			
			
		}catch (Exception e) {
			return new ResponseEntity<>("file not found !!!",HttpStatus.NOT_FOUND);
		}
		
		
		String contentType="application/pdf";
		String headerValue="attachment; filename=\"+"+resource.getFilename()+"\"";
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
				.body(resource);
		
	}
	
	
	//for excel export
	@GetMapping("/excel")
	public void excelExport(HttpServletResponse response) throws IOException
	{
		
		response.setContentType("application/octet-stream");
		
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=myresume.xls";
		response.setHeader(headerKey, headerValue);
		
		service.generateExcel(response);
	}
	
	//for pdf export
	@GetMapping("/pdf")
	public void pdfExport(HttpServletResponse response) throws Exception
	{
		
		response.setContentType("application/octet-stream");
		
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=myresume.pdf";
		response.setHeader(headerKey, headerValue);
		
		service.generatePdf(response);
	}
	
	
	

}
