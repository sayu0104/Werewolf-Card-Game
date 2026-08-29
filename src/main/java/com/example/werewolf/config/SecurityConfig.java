package com.example.werewolf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/admin/**").authenticated()
				.anyRequest().permitAll())
			.formLogin(form -> form
				.loginPage("/login")
				.defaultSuccessUrl("/admin/roles", true)
				.permitAll())
			.logout(logout -> logout.permitAll());
		return http.build();
	}
}
