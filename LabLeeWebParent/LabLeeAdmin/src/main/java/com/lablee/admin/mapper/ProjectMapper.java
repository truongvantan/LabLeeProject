package com.lablee.admin.mapper;

import org.mapstruct.Mapper;

import com.lablee.admin.dto.ProjectFormAddDTO;
import com.lablee.admin.dto.ProjectFormEditDTO;
import com.lablee.common.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
	
	// form add -> entity
	Project formAddDTOToEntity(ProjectFormAddDTO projectFormAddDTO);
	
	// form edit -> entity
	Project formEditDTOToEntity(ProjectFormEditDTO projectFormEditDTO);
	
	// entity -> form edit
	ProjectFormEditDTO entityToFormEditDTO(Project project);
}
