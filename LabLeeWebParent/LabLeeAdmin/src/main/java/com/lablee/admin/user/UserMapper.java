package com.lablee.admin.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lablee.admin.role.RoleMapper;
import com.lablee.common.entity.Role;
import com.lablee.common.entity.User;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
	
	// entity -> DTO
	@Mapping(target = "setRoleDTOs", source = "setRoles")
	UserDTO toDTO(User userEntity);
	
	// DTO -> entity
	@Mapping(target = "setRoles", source = "setRoleDTOs")
	User toEntity(UserDTO userDTO);
	
	// Form Add DTO -> entity
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "setRoles", source = "setRoleDTOIds")
	User formAddDTOToEntity(UserFormAddDTO userFormAddDTO);
	
	// entity -> form edit DTO
	UserFormEditDTO toFormEditDTO(User user);
	
	// form edit DTO -> entity
	@Mapping(target = "deleted", ignore = true)
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
