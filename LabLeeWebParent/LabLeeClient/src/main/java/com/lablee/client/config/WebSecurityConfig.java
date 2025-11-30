package com.lablee.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

//	@Bean
//	UserDetailsService userDetailsService() {
//		return new LabLeeUserDetailsService();
//	}
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//
//		return authProvider;
//	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
//		http.authenticationProvider(authenticationProvider());

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**",
				"/fontawesome/**", "/fonts/**", "/webfonts/**").permitAll()
				.anyRequest().permitAll());

		return http.build();
	}

}
