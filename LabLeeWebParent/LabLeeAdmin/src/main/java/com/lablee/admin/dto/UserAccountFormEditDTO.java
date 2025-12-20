package com.lablee.admin.dto;

import java.util.HashSet;
import java.util.Set;

import com.lablee.common.constant.ConstantUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserAccountFormEditDTO {
	private Integer id;
	
	@Pattern(regexp = ConstantUtil.REGEX_EMAIL, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_EMAIL)
	private String email;

	@NotBlank(message = ConstantUtil.MESSAGE_FAIL_VALIDATION_BLANK_INPUT_TEXT)
	@Size(max = 128, message = ConstantUtil.MESSAGE_FAIL_VALIDATION_MAX_SIZE_INPUT_TEXT_128)
	private String fullName;
	
	private String newPassword;
	private String confirmPassword;
	private String photo;
	
	private Set<RoleDTO> setRoles = new HashSet<>();
	
	public String getPhotoImagePath() {
		if (this.id == null || this.photo == null) {
			return "/images/default-user.png";
		}
		
		return new StringBuffer(ConstantUtil.PATH_USER_PHOTO_STORED_DEFAULT).append(this.id).append("/").append(this.photo).toString();
	}
}
