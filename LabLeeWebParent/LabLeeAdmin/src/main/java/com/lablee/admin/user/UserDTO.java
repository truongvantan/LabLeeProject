package com.lablee.admin.user;

import java.util.Set;

import com.lablee.admin.role.RoleDTO;
import com.lablee.common.constant.ConstantUtil;

import lombok.Data;

@Data
public class UserDTO {
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String photo;
	private boolean enabled;
	private boolean deleted;
	private Set<RoleDTO> setRoleDTOs;
	
	
	public String getPhotoImagePath() {
		if (this.id == null || this.photo == null) {
			return "/images/default-user.png";
		}
		
//		return "/" + ConstantUtil.PATH_USER_PHOTO_UPLOAD_DEFAULT + "/" + this.id + "/" + this.photo;
		return new StringBuffer("").append("/").append(ConstantUtil.PATH_USER_PHOTO_UPLOAD_DEFAULT).append("/").append(this.id).append("/").append(this.photo).toString();
	}
}
