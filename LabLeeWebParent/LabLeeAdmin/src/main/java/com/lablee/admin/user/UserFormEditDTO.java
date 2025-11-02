package com.lablee.admin.user;

import java.util.Set;

import com.lablee.admin.role.RoleDTO;
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
	@Size(max = 50, message = ConstantUtil.MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_50_FAIL)
	private String firstName;
	
	@NotBlank(message = ConstantUtil.MESSAGE_VALIDATION_BLANK_INPUT_TEXT_FAIL)
	@Size(max = 50, message = ConstantUtil.MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_50_FAIL)
	private String lastName;
	
//	@Pattern(regexp = ConstantUtil.REGEX_PASSWORD_20, message = ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL)
	private String password;
	private String photo;
	private boolean enabled;
	private Set<Role> setRoles;
	
	
	public String getPhotoImagePath() {
		if (this.id == null || this.photo == null) {
			return "/images/default-user.png";
		}
		
//		return "/" + ConstantUtil.PATH_USER_PHOTO_UPLOAD_DEFAULT + "/" + this.id + "/" + this.photo;
		return new StringBuffer("").append("/").append(ConstantUtil.PATH_USER_PHOTO_UPLOAD_DEFAULT).append("/").append(this.id).append("/").append(this.photo).toString();
	}

}
