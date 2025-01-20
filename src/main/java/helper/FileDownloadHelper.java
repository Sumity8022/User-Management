package helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class FileDownloadHelper {
	
	private Path foundFile;
	
	
	public Resource getFileResource(String downloadDir,String FileName) throws IOException
	{
		//myfiles/24
		
		
		//convert string download directory into path object
		Path path = Paths.get(downloadDir);
		
		//find that particular file
		//list all file in the folder specifid by path
		
		
		
		Files.list(path).forEach(file->{
			
			
			
			if(file.getFileName().toString().startsWith(FileName))
			{
				foundFile=file;
			}
			
		});
		
		if(foundFile!=null)
		{
			return new UrlResource(foundFile.toUri());
		}
		
		
		return null;
	}
	
	
	
	
	
	

}
