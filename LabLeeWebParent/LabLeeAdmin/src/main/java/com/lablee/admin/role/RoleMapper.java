package com.lablee.admin.role;

import java.util.List;

import org.mapstruct.Mapper;

import com.lablee.common.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	RoleDTO toDTO(Role roleEntity);
	
	Role toEntity(RoleDTO roleDTO);
	
	List<RoleDTO> toDTOList(List<Role> listRoles);
	
	List<Role> toEntityList(List<RoleDTO> listRoleDTOs);
}
