package com.lablee.admin.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lablee.admin.dto.UserLoginDTO;
import com.lablee.admin.mapper.UserMapper;
import com.lablee.admin.repository.UserRepository;
import com.lablee.common.entity.User;


public class LabLeeUserDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LabLeeUserDetailsService.class);
	
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
		
		LOGGER.error("Could not find any user with email: {}", email);
		throw new UsernameNotFoundException("Could not find any user with email: " + email);
	}

}
