package com.lablee.admin.role;

import lombok.Data;

@Data
public class RoleDTO {
	private Integer id;
	private String name;
	private String description;
	
	
	public RoleDTO(Integer id) {
		this.id = id;
	}
	

	@Override
	public String toString() {
		return this.name;
	}



}
