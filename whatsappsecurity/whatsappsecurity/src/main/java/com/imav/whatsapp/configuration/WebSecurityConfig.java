package com.imav.whatsapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.imav.whatsapp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private CustomUserDetailsService userDetailsService;

	@Autowired
	public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/h2-console/**", "/", "/login", "/css/**", "/js/**").permitAll()
			.and()
			.headers().frameOptions().sameOrigin()
			.and()
	        .authorizeHttpRequests()
			.requestMatchers("/home", "/chat", "/register").authenticated().anyRequest().permitAll()
			.and()
				.formLogin(form -> form.loginPage("/login")
						.defaultSuccessUrl("/home")
						.loginProcessingUrl("/login")
						.failureUrl("/login").permitAll())
				.logout((logout) -> logout.permitAll());

		return http.build();
	}

	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
}