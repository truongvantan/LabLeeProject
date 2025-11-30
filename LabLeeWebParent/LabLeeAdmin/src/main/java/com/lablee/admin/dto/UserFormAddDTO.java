package com.lablee.admin.dto;

import java.util.Set;

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
	@Size(max = 128, message = ConstantUtil.MESSAGE_VALIDATION_MAX_SIZE_INPUT_TEXT_128_FAIL)
	private String fullName;
	
	@Pattern(regexp = ConstantUtil.REGEX_PASSWORD_20, message = ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL)
	private String password;
	
	@Pattern(regexp = ConstantUtil.REGEX_PASSWORD_20, message = ConstantUtil.MESSAGE_VALIDATION_PASSWORD_20_FAIL)
	private String repassword;
	private String photo;
	private boolean enabled;
	private Set<Integer> setRoleDTOIds;
}
