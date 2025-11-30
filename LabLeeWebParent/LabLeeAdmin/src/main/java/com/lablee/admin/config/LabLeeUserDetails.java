package com.lablee.admin.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lablee.admin.dto.UserLoginDTO;
import com.lablee.common.entity.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LabLeeUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 1L;
//	private final User user;
	private final UserLoginDTO userLoginDTO;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> setRoles = userLoginDTO.getSetRoles();
		List<SimpleGrantedAuthority> authories = new ArrayList<>();
		
		for (Role role : setRoles) {
			authories.add(new SimpleGrantedAuthority(role.getName()));
			
		}
		
		return authories;
	}

	@Override
	public String getPassword() {
		return userLoginDTO.getPassword();
	}

	@Override
	public String getUsername() {
		return userLoginDTO.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return userLoginDTO.isEnabled();
	}
	
	public String getFullName() {
		return userLoginDTO.getFullName();
	}
	
	public void setFullName(String fullName) {
		this.userLoginDTO.setFullName(fullName);
	}

}
