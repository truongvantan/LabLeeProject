package com.lablee.admin.controller.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lablee.admin.util.FileUploadUtil;
import com.lablee.common.constant.ConstantUtil;

@RestController
@RequestMapping("/api/upload")
public class ImageUploadController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadController.class);
	
	@PostMapping("/publication-image")
	public ResponseEntity<Map<String, Object>> uploadPublicationImage(@RequestParam("upload") MultipartFile file) {
		return handleUploadImage(file, ConstantUtil.PATH_PUBLICATION_CONTENT_UPLOAD_DIR_DEFAULT,
				ConstantUtil.PATH_PUBLICATION_CONTENT_STORED_DEFAULT);
	}

	@PostMapping("/project-image")
	public ResponseEntity<Map<String, Object>> uploadProjectImage(@RequestParam("upload") MultipartFile file) {
		return handleUploadImage(file, ConstantUtil.PATH_PROJECT_CONTENT_UPLOAD_DIR_DEFAULT,
				ConstantUtil.PATH_PROJECT_CONTENT_STORED_DEFAULT);
	}

	@PostMapping("/member-bio-image")
	public ResponseEntity<Map<String, Object>> uploadMemberBioImage(@RequestParam("upload") MultipartFile file) {
		return handleUploadImage(file, ConstantUtil.PATH_MEMBER_LAB_BIO_UPLOAD_DIR_DEFAULT,
				ConstantUtil.PATH_MEMBER_LAB_BIO_STORED_DEFAULT);
	}

	@PostMapping("/news-image")
	public ResponseEntity<Map<String, Object>> uploadNewsImage(@RequestParam("upload") MultipartFile file) {
		return handleUploadImage(file, ConstantUtil.PATH_NEWS_CONTENT_UPLOAD_DIR_DEFAULT,
				ConstantUtil.PATH_NEWS_CONTENT_STORED_DEFAULT);
	}

	@PostMapping("/about-us-image")
	public ResponseEntity<Map<String, Object>> uploadAboutUsImage(@RequestParam("upload") MultipartFile file) {
		return handleUploadImage(file, ConstantUtil.PATH_ABOUT_US_CONTENT_UPLOAD_DIR_DEFAULT,
				ConstantUtil.PATH_ABOUT_US_CONTENT_STORED_DEFAULT);
	}

	private ResponseEntity<Map<String, Object>> handleUploadImage(MultipartFile file, String uploadDir,
			String uploadStoredPath) {
		// validation file size
		if (!FileUploadUtil.isValidFileSize(file)) {
			return ResponseEntity.badRequest()
					.body(Map.of("error", Map.of("message", "Dung lượng ảnh phải nhỏ hơn 1MB!")));
		}

		Map<String, Object> response = new HashMap<>();
		try {
			if (file == null || file.isEmpty()) {
				response.put("error", Map.of("message", "File is empty"));
				return ResponseEntity.badRequest().body(response);
			}

			String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

			try {
				if (file != null && !file.isEmpty()) {
					FileUploadUtil.saveFile(uploadDir, fileName, file);

					String fileUrl = uploadStoredPath + fileName;

					response.put("url", fileUrl);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				response.put("error", Map.of("message", "Cannot upload file: " + e.getMessage()));
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response.put("error", Map.of("message", "Cannot upload file: " + e.getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		return ResponseEntity.ok(response);
	}
}
