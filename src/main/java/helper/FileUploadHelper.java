package helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadHelper {
	
	
	
	public static void saveFile(String uploaddir,String fileName,MultipartFile multipartFile) throws IOException
	{
	Path uploadedPAth = Paths.get(uploaddir);	//myfiles/24
	
	if(!Files.exists(uploadedPAth))
	{
		Files.createDirectories(uploadedPAth);//if myfiles/id folder is not exist then at runftime it will crreate folder
	}
		
	try(InputStream inputStream=multipartFile.getInputStream())
	{
		Path path = uploadedPAth.resolve(fileName);//myfiles/24/sumit.png
		
		System.out.println(path);
		
		Files.copy(inputStream, path,StandardCopyOption.REPLACE_EXISTING);
		
		
	}catch (Exception e) {
		// TODO: handle exception
		throw new IOException("file not get saved!!!!!");
	}
	
	
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
