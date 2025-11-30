package com.lablee.admin.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lablee.admin.dto.UserLoginDTO;
import com.lablee.admin.mapper.UserMapper;
import com.lablee.admin.repository.UserRepository;
import com.lablee.common.entity.User;


public class LabLeeUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;

	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> oUser = userRepository.findByEmail(email);
		
		if (oUser.isPresent()) {
			UserLoginDTO userLoginDTO = userMapper.toLoginDTO(oUser.get());
			return new LabLeeUserDetails(userLoginDTO);
		}
		
		throw new UsernameNotFoundException("Could not find user with email: " + email);
	}

}
