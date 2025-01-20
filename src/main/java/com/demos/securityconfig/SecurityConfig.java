package com.demos.securityconfig;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	// create passwordEncoder bean

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// root heart of security

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(c -> c.disable());

		http.authorizeHttpRequests((requests) -> requests
				        .requestMatchers("/allusers").authenticated()
				        .requestMatchers("/customlogin/**", "/", "/save", "/register","/myfiles/**","/downloadpdf/**","/excel","/pdf").permitAll()
						.requestMatchers("/mydashboard").hasAuthority("admin"));

		// http.formLogin(withDefaults());//use default login page

		http.formLogin(
				mform -> mform.loginPage("/customlogin").loginProcessingUrl("/mylogin").defaultSuccessUrl("/allusers"));

		http.httpBasic(withDefaults());
		return http.build();
	}

}
