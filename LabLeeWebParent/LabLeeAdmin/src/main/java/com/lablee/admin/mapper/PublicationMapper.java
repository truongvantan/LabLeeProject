package com.lablee.admin.mapper;

import org.mapstruct.Mapper;

import com.lablee.admin.dto.PublicationFormAddDTO;
import com.lablee.admin.dto.PublicationFormEditDTO;
import com.lablee.common.entity.Publication;

@Mapper(componentModel = "spring")
public interface PublicationMapper {
	
	// form add -> entity
	Publication formAddDTOToEntity(PublicationFormAddDTO publicationFormAddDTO);
	
	// form edit -> entity
	Publication formEditDTOToEntity(PublicationFormEditDTO publicationFormEditDTO);
	
	// entity -> form edit
	PublicationFormEditDTO entityToFormEditDTO(Publication publication);
}
