package com.lablee.admin.mapper;

import org.mapstruct.Mapper;

import com.lablee.admin.dto.NewsFormAddDTO;
import com.lablee.admin.dto.NewsFormEditDTO;
import com.lablee.common.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {
	// form add -> entity
	News formAddDTOToEntity(NewsFormAddDTO newsFormAddDTO);
	
	// form edit -> entity
	News formEditDTOToEntity(NewsFormEditDTO newsFormEditDTO);
	
	// entity -> form edit
	NewsFormEditDTO entityToFormEditDTO(News news);
}
