package com.lablee.admin.dto;

import java.util.Set;

import com.lablee.common.constant.ConstantUtil;
import com.lablee.common.entity.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserFormEditDTO {
	private Integer id;
	
	@Pattern(regexp = ConstantUtil.REGEX_EMAIL, message = ConstantUtil.MESSAGE_VALIDATION_EMAIL_FAIL)
	private String email;
	
	@NotBlank(message = ConstantUtil.MESSAGE_VALIDATION_BLANK_INPUT_TEXT_FAIL)
	@Size(max = 128, message = ConstantUtil.MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_128_FAIL)
	private String fullName;
	
	private String password;
	
	private String photo;
	private boolean enabled;
	
	private Set<Role> setRoles;
	
	
	public String getPhotoImagePath() {
		if (this.id == null || this.photo == null) {
			return "/images/default-user.png";
		}
		
		return new StringBuffer(ConstantUtil.PATH_USER_PHOTO_STORED_DEFAULT).append(this.id).append("/").append(this.photo).toString();
	}

}
