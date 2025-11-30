package com.lablee.admin.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.common.constant.ConstantUtil;

public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try {
			InputStream inputStream = multipartFile.getInputStream();
			if (fileName != null) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);	
			}
		} catch (IOException e) {
			LOGGER.error("Could not save file: " + fileName);
			throw new IOException("Could not save file: " + fileName, e);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						LOGGER.error("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}
	
	public static boolean isValidFileSize(MultipartFile multipartFile) {
		return multipartFile.getSize() < ConstantUtil.MAX_FILE_SIZE;
	}
	
	public static boolean isValidExtraFileSize(MultipartFile[] extraImageMultiparts) {
		for (MultipartFile multipartFile : extraImageMultiparts) {
			if (multipartFile.getSize() > ConstantUtil.MAX_FILE_SIZE) {
				return false;
			}
		}
		return true;
	}
	
	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + dir);
		}
	}
}
