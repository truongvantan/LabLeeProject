package com.lablee.admin.mapper;

import org.mapstruct.Mapper;

import com.lablee.admin.dto.MemberLabProfileFormAddDTO;
import com.lablee.admin.dto.MemberLabProfileFormEditDTO;
import com.lablee.common.entity.MemberLabProfile;

@Mapper(componentModel = "spring")
public interface MemberLabProfileMapper {
	
	// form add -> entity
	MemberLabProfile formAddDTOToEntity(MemberLabProfileFormAddDTO memberLabProfileFormAddDTO);
	
	// form edit -> entity
	MemberLabProfile formEditDTOToEntity(MemberLabProfileFormEditDTO memberLabProfileFormEditDTO);
	
	// entity -> form edit
	MemberLabProfileFormEditDTO entityToFormEditDTO(MemberLabProfile memberLabProfile);
}
