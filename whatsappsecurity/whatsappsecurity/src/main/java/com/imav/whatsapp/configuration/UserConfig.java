package com.imav.whatsapp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.imav.whatsapp.entity.Role;
import com.imav.whatsapp.entity.UserEntity;
import com.imav.whatsapp.repository.RoleRepository;
import com.imav.whatsapp.repository.UserRepository;

@Configuration
@Profile("admin")
public class UserConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	public static String passwordEncoder(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}


	@Override
	public void run(String... args) throws Exception {
		
		String password = "123";
		String encryptedPassword = passwordEncoder(password);

		Role role = new Role();
		role.setName("ROLE_ADMIN");

		Role createdRole = new Role();
		createdRole = roleRepository.save(role);

		UserEntity user = new UserEntity();
		user.setEmail("johnes@imav.com.br");
		user.setPassword(encryptedPassword);
		user.setUsername("admin");

		List<Role> roles = new ArrayList<>();
		roles.add(createdRole);

		user.setRoles(roles);

		userRepository.save(user);

	}

}
