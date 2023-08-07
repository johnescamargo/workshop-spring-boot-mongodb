package com.imav.whatsapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.imav.whatsapp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
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

		http
				.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers(
						  "/"
						, "/login"
						, "/css/**"
						, "/js/**"
						, "/img/**"
						, "/api/**"
						, "/txt/**"
						, "/privacy-and-policy"
						, "/topic/**"
						, "/home/john/img/**"
						, "/process-message"
						, "/process-messages"
						, "/customers"
						, "/outside-message"
						, "/websocket-server"
						, "/webjars/**"
						, "/www.web.login.imav.com.br:5000/api/webhook"
						, "/www.web.login.imav.com.br:5000/websocket-server"
						, "/www.web.login.imav.com.br:5000/websocket-server/topic/message"
						, "/www.web.login.imav.com.br:5000/websocket-server/topic/customers"
						, "/www.web.login.imav.com.br:5000/websocket-server/topic/customers"
						, "/www.web.login.imav.com.br:5000/websocket-server/topic/messages-customers"
						, "/www.web.login.imav.com.br:5000/websocket-server/app/process-message"
						, "/www.web.login.imav.com.br:5000/websocket-server/app/process-messages-customers"
						).permitAll()
				.and()
				.headers().frameOptions().sameOrigin()
				.and()
				.authorizeHttpRequests()
				.requestMatchers(
						  "/home"
						, "/send"
						, "/chat"
						, "/config"
						, "/test"
						, "/confirmation"
						).hasAnyAuthority("USER", "ADMIN")
				.requestMatchers("/register", "/settings").hasAuthority("ADMIN").anyRequest().authenticated()
				.and()
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/home")
						.loginProcessingUrl("/login")
						.failureUrl("/login?error=true").permitAll())
				.logout((logout) -> logout.permitAll());

		return http.build();
	}


	public AuthenticationManager authenticationManagerBuilder(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	
}