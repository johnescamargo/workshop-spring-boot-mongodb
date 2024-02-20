package com.imav.whatsapp.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.imav.whatsapp.dto.UserDto;
import com.imav.whatsapp.dto.UserSimpleDto;
import com.imav.whatsapp.entity.Role;
import com.imav.whatsapp.entity.UserEntity;
import com.imav.whatsapp.repository.RoleRepository;
import com.imav.whatsapp.repository.UserRepository;

@Service
public class DBUserResource {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	public static String passwordEncoder(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}


	public void saveUser(UserDto dto) {

		try {
			String encryptedPassword = passwordEncoder(dto.getPassword());
			
			// Save Roles first and then all the Users
			Role role = new Role();
			role.setName("ROLE_USER");

			Role createdRole = new Role();
			createdRole = roleRepository.save(role);

			UserEntity user = new UserEntity();
			user.setEmail(dto.getEmail());
			user.setPassword(encryptedPassword);
			user.setUsername(dto.getUsername());

			List<Role> roles = new ArrayList<>();
			roles.add(createdRole);

			user.setRoles(roles);

			userRepository.save(user);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<UserSimpleDto> loadUsers() {
		List<UserSimpleDto> dtos = new ArrayList<>();
		List<UserEntity> users = new ArrayList<>();
		List<String> roles = new ArrayList<>();

		try {
			users = userRepository.findAll();
			for (int i = 0; i < users.size(); i++) {
				roles.clear();
				UserSimpleDto a = new UserSimpleDto();
				a.setEmail(users.get(i).getEmail());
				a.setUsername(users.get(i).getUsername());

				for (int j = 0; j < users.get(i).getRole().size(); j++) {
					roles.add(users.get(i).getRole().get(j).getName());
				}
				a.setRoles(roles);
				dtos.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return dtos;
	}

}
