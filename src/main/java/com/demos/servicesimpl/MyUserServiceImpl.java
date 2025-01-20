package com.demos.servicesimpl;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demos.dto.MyUserDto;
import com.demos.entities.MyUser;
import com.demos.entities.Role;
import com.demos.repos.MyUserRepo;
import com.demos.repos.RoleRepo;
import com.demos.services.MyUserService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MyUserServiceImpl implements MyUserService {
	
	@Autowired
	private MyUserRepo userRepo;
	
	@Autowired
	private RoleRepo rolRepo;
	
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public MyUserDto saveUser(MyUserDto dto) {
	
		//convert dto into entity to save in db
		MyUser myUserEntity = mapper.map(dto, MyUser.class);
		
		
		//assign normal roles for rest of the users in our app summit is admin 
		//fetching existing normal role object from db
		Role role = rolRepo.findByName("normal");//select * from role where name -'normal';
		
	
		
		//create list of role
		//List<Role> roleList = Arrays.asList(role);
		
		List<Role> rollist=new ArrayList<>();
		rollist.add(role);
		//now just set this object in MyUser
		myUserEntity.setRoles(rollist);
		
		//password encode...
		//1.get comming password from object
		//2.encode this password using passwordEncoder
		//3.again set this encoded password to sam eobject
		//4.then finally save whole objecy
		
		String encodedpassword = passwordEncoder.encode(dto.getPassword());
		
		myUserEntity.setPassword(encodedpassword);
		
		
		MyUser savedEntity = userRepo.save(myUserEntity);
		
		//again convert this saved entity into Dto
		MyUserDto dtouser = mapper.map(savedEntity, MyUserDto.class);
		
		
		return dtouser;
	}

	@Override
	public MyUserDto findUserByEmail(String username) {
	
		MyUser entity = userRepo.findByEmail(username);
		//convert saved entity into dto
		if(entity!=null)
		{
			MyUserDto myuserDto = mapper.map(entity, MyUserDto.class);	
			return myuserDto;
		}
		return null;
	}

	@Override
	public List<MyUserDto> getAllUsers() {
		
		List<MyUser> entityList = userRepo.findAll();
		
		List<MyUserDto> dtolist = entityList.stream().map(d->mapper.map(d, MyUserDto.class)).collect(Collectors.toList());
		
		return dtolist;
	}

	
	//for excel export
	@Override
	public void generateExcel(HttpServletResponse response) throws IOException {
	
		//first get all data from db
		List<MyUser> users = userRepo.findAll();
		
		//create workbok
		HSSFWorkbook workbook=new HSSFWorkbook();
		
		//create excel sheet
		HSSFSheet sheet = workbook.createSheet();
		
		//create first row in excel sheet
		HSSFRow headerFirstRow = sheet.createRow(0);
		headerFirstRow.createCell(0).setCellValue("S.No");
		headerFirstRow.createCell(1).setCellValue("First Name");
		headerFirstRow.createCell(2).setCellValue("Last Name");
		headerFirstRow.createCell(3).setCellValue("Email");
		
		int rowCount=1;//used for Sno column to display it from 1
		
		for(MyUser user:users)
		{
		//create row for data 
			
			HSSFRow dataRow = sheet.createRow(rowCount);
			dataRow.createCell(0).setCellValue(rowCount);
			dataRow.createCell(1).setCellValue(user.getFirstName());
			dataRow.createCell(2).setCellValue(user.getLastName());
			dataRow.createCell(3).setCellValue(user.getEmail());
			rowCount++;
			
		}
		
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		
	}

	
	//for pdf export
	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
	
		//first get all data from db
		List<MyUser> users = userRepo.findAll();
		
		//in pdf we have concept of docuement so we need to create document
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		
		document.open();
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		//to create table we use pdfTable
		
		PdfPTable table=new PdfPTable(4);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] {1.5f,1.5f,1.5f,3.5f});
		table.setSpacingBefore(10);
		
		pdfTableHeader(table);
		
		pdfTableBody(table,users);
		
		document.add(table);
		document.close();
		
	}
	
	//pdfTable header
	private void pdfTableHeader(PdfPTable table)
	{
		PdfPCell cell=new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("S.No",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First Name",font));
		table.addCell(cell);
		
		
		cell.setPhrase(new Phrase("Last Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		
		
	}
	
	
	private void pdfTableBody(PdfPTable table,List<MyUser> users)
	{
		int sno=0;
		
		for(MyUser user:users)
		{
			table.addCell(String.valueOf(sno));
			table.addCell(user.getFirstName());
			table.addCell(user.getLastName());
			table.addCell(user.getEmail());
			
			sno++;
		}
		
		
		
		
	}
	
	
	

}
