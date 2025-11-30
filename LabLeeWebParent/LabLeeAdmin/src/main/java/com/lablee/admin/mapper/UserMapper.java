package com.lablee.admin.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lablee.admin.dto.UserAccountFormEditDTO;
import com.lablee.admin.dto.UserDTO;
import com.lablee.admin.dto.UserFormAddDTO;
import com.lablee.admin.dto.UserFormEditDTO;
import com.lablee.admin.dto.UserLoginDTO;
import com.lablee.common.entity.Role;
import com.lablee.common.entity.User;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
	
	// entity -> Form Account Edit DTO
	@Mapping(target = "newPassword", ignore = true)
	@Mapping(target = "confirmPassword", ignore = true)
	UserAccountFormEditDTO toAccountFormEditDTO(User user);
	
	// Form Account Edit DTO -> entity
	@Mapping(target = "password", source = "newPassword")
	@Mapping(target = "enabled", ignore = true)
	User formAccountEditDTOToEntity(UserAccountFormEditDTO userAccountFormEditDTO);
	
	// entity -> Login DTO
	UserLoginDTO toLoginDTO(User user);
	
	// entity -> DTO
	@Mapping(target = "setRoleDTOs", source = "setRoles")
	UserDTO toDTO(User userEntity);
	
	// DTO -> entity
	@Mapping(target = "setRoles", source = "setRoleDTOs")
	@Mapping(target = "password", ignore = true)
	User toEntity(UserDTO userDTO);
	
	// Form Add DTO -> entity
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "setRoles", source = "setRoleDTOIds")
	User formAddDTOToEntity(UserFormAddDTO userFormAddDTO);
	
	// entity -> form edit DTO
	UserFormEditDTO toFormEditDTO(User user);
	
	// form edit DTO -> entity
	User formEditDTOToEntity(UserFormEditDTO userFormEditDTO);
    
    default Set<Role> mapRoleIdsToRoles(Set<Integer> roleIds) {
        if (roleIds == null) {
        	return null;
        }
        
        return roleIds.stream()
            .map(id -> {
                Role role = new Role();
                role.setId(id);
                return role;
            })
            .collect(Collectors.toSet());
    }
    
    List<UserDTO> toDTOList(List<User> listUsers);
    
    List<User> toEntityList(List<UserDTO> listUserDTOs);

	
}
