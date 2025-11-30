package com.lablee.admin.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.lablee.admin.dto.RoleDTO;
import com.lablee.common.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	RoleDTO toDTO(Role roleEntity);
	
	Role toEntity(RoleDTO roleDTO);
	
	List<RoleDTO> toDTOList(List<Role> listRoles);
	
	List<Role> toEntityList(List<RoleDTO> listRoleDTOs);
	
	Set<RoleDTO> toSetDTO(Set<Role> setRoles); 
}
