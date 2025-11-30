package com.lablee.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lablee.admin.dto.RoleDTO;
import com.lablee.admin.mapper.RoleMapper;
import com.lablee.admin.repository.RoleRepository;
import com.lablee.common.entity.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	private final RoleRepository roleRepository;
	private final RoleMapper roleMapper;
	
	
	public List<RoleDTO> getListRoleDTOS() {
		List<RoleDTO> listRoleDTOs = new ArrayList<>();
		List<Role> listRoles = roleRepository.findAllByOrderByIdAsc();
		
		listRoleDTOs = roleMapper.toDTOList(listRoles);
		
		return listRoleDTOs;
	}
	
}
