package com.lablee.admin.user;

import java.util.Set;

import com.lablee.admin.role.RoleDTO;
import com.lablee.common.constant.ConstantUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserFormAddDTO {
	
	@Pattern(regexp = ConstantUtil.REGEX_EMAIL, message = ConstantUtil.MESSAGE_VALIDATION_EMAIL_FAIL)
	private String email;
	
	@NotBlank(message = ConstantUtil.MESSAGE_VALIDATION_BLANK_INPUT_TEXT_FAIL)
	@Size(max = 50, message = ConstantUtil.MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_50_FAIL)
	private String firstName;
	
	@NotBlank(message = ConstantUtil.MESSAGE_VALIDATION_BLANK_INPUT_TEXT_FAIL)
	@Size(max = 50, message = ConstantUtil.MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_50_FAIL)
	private String lastName;
	
	@Pattern(regexp = ConstantUtil.REGEX_PASSWORD_20, message = ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL)
	private String password;
	
	@Pattern(regexp = ConstantUtil.REGEX_PASSWORD_20, message = ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL)
	private String repassword;
	private String photo;
	private boolean enabled;
	private Set<Integer> setRoleDTOIds;
}
