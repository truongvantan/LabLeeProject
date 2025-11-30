package com.lablee.admin.dto;

import java.util.Set;

import com.lablee.common.constant.ConstantUtil;

import lombok.Data;

@Data
public class UserDTO {
	private Integer id;
	private String fullName;
	private String email;
	private String photo;
	private boolean enabled;
	private Set<RoleDTO> setRoleDTOs;
	
	
	public String getPhotoImagePath() {
		if (this.id == null || this.photo == null) {
			return "/images/default-user.png";
		}
		
		return new StringBuffer(ConstantUtil.PATH_USER_PHOTO_STORED_DEFAULT).append(this.id).append("/").append(this.photo).toString();
	}
	
}
