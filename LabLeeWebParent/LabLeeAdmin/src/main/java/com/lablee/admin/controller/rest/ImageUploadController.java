package com.lablee.admin.controller.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	@PostMapping("/publication-image")
	public ResponseEntity<Map<String, Object>> uploadPublicationImage(@RequestParam("upload") MultipartFile file) {
		System.err.println("ImageUploadController");
		Map<String, Object> response = new HashMap<>();
		try {
			if (file == null || file.isEmpty()) {
				response.put("error", Map.of("message", "File is empty"));
				System.err.println("ImageUploadController: File is empty");
				return ResponseEntity.badRequest().body(response);
			}

			String uploadDir = ConstantUtil.PATH_PUBLICATION_CONTENT_UPLOAD_DIR_DEFAULT;
			String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

			try {
				if (file != null && !file.isEmpty()) {
					FileUploadUtil.saveFile(uploadDir, fileName, file);

					// URL truy cập từ trình duyệt (phải trùng ResourceHandler)
					String fileUrl = ConstantUtil.PATH_PUBLICATION_CONTENT_STORED_DEFAULT + fileName;
					response.put("url", fileUrl.substring(1));
					System.out.println("url: " + fileUrl);
					
					System.out.println("Saved file to: " + fileUrl);
				}
			} catch (IOException e) {
				e.printStackTrace();
				response.put("error", Map.of("message", "Cannot upload file: " + e.getMessage()));
				System.err.println("ImageUploadController: " + "Cannot upload file: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("error", Map.of("message", "Cannot upload file: " + e.getMessage()));
			System.err.println("ImageUploadController: " + "Cannot upload file: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return ResponseEntity.ok(response);
	}
}
