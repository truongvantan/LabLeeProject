package com.lablee.admin.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsFormEditDTO {
	private Integer id;
	
	@NotBlank(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_BLANK_INPUT_TEXT)
	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String title;
	
	@Size(max = 512, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_512)
	private String shortDescription;
	private String content;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime createAt;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime updateAt = LocalDateTime.now();
	
	private String thumbnail;
	private boolean enabled;
	private User user;
	
	public String getThumbnailPath() {
		if (this.id == null || this.thumbnail == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_NEWS_THUMBNAIL_STORED_DEFAULT).append(this.id).append("/")
				.append(this.thumbnail).toString();
	}
}
