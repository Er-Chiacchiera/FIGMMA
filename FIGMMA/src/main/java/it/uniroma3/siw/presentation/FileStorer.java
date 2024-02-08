package it.uniroma3.siw.presentation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileStorer {

	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/images/";	
	
	private static String setupDirName(String type) {
		return uploadDirectory+type;
	}
	
	public static String store(MultipartFile file, String type, Long id) {
		String newFileName=id.toString() + ".jpg";
		Path fileNameAndPath  = Paths.get(setupDirName(type), newFileName);
		System.out.println(fileNameAndPath);
		try {
			Files.write(fileNameAndPath, file.getBytes());
			System.out.println("foto salvata\n\n\n\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileNameAndPath.getFileName().toString();
	}
	
	public static void removeImg(String type, String name) {
		Path fileNameAndPath  = Paths.get(setupDirName(type)+"/"+name);
		try {
			Files.delete(fileNameAndPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void dirRename(String oldName, String newName) {
		new File(setupDirName(oldName)).renameTo(new File(setupDirName(newName)));
	}
		
	
}
