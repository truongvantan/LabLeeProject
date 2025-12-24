package com.lablee.admin.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.MemberLabProfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectFormAddDTO {
	private Integer id;
	
	@NotBlank(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_BLANK_INPUT_TEXT)
	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String title;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate endDate;
	
	@Size(max = 255, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_255)
	private String sponsor;

	private String thumbnail;
	private String projectAbstract;

	private String content;
	
	private double budget;

	private boolean enabled;

	private Set<MemberLabProfile> members = new HashSet<>();

	public String getThumbnailPath() {
		if (this.id == null || this.thumbnail == null) {
			return "/images/default-user.png";
		}

		return new StringBuffer(ConstantUtil.PATH_PROJECT_THUMBNAIL_STORED_DEFAULT).append(this.id).append("/")
				.append(this.thumbnail).toString();
	}

}
