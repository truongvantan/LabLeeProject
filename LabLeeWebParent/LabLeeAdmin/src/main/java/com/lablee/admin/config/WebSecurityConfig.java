package com.lablee.admin.config;

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

	@Bean
	UserDetailsService userDetailsService() {
		return new LabLeeUserDetailsService();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**",
				"/fontawesome/**", "/fonts/**", "/webfonts/**").permitAll()
				.requestMatchers("/users/**", "/members/**", "/publications/**", "/api/**").hasAuthority("Root Admin")
				.anyRequest().authenticated())
				.formLogin(login -> login.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout.permitAll())
				.rememberMe(rem -> rem.key("AbcDefgHijKlmnOpqrs_1234567890").tokenValiditySeconds(7 * 24 * 60 * 60));
		
		http.headers(header -> header
				.frameOptions(option -> option.sameOrigin()));

		return http.build();
	}

}
