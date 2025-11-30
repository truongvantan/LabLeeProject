package com.lablee.admin.dto;

import java.util.Set;

import com.lablee.common.entity.Role;

import lombok.Data;

@Data
public class UserLoginDTO {
	private Integer id;
	private String fullName;
	private String email;
	private String password;
	private boolean enabled;
	private Set<Role> setRoles;
	
}
