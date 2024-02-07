package com.imav.whatsapp.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.imav.whatsapp.entity.CountryCode;
import com.imav.whatsapp.entity.Role;
import com.imav.whatsapp.entity.UserEntity;
import com.imav.whatsapp.entity.WeekTime;
import com.imav.whatsapp.repository.CountryCodeRepository;
import com.imav.whatsapp.repository.RoleRepository;
import com.imav.whatsapp.repository.UserRepository;
import com.imav.whatsapp.repository.WeekTimeRepository;

@Configuration
@Profile("admin")
public class InitConfig implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CountryCodeRepository countryCodeRepository;

	@Autowired
	private WeekTimeRepository weekRepository;

	public static String passwordEncoder(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	@Override
	public void run(String... args) throws Exception {
		setUserAdmin();
		setCountryCode();
		setWeekTime();
	}

	public void setUserAdmin() {

		boolean resp = userRepository.existsByUsername("admin");
		System.out.println("Is admin already set? | resp: " + resp);

		if (!resp) {
			String password = "76718244";
			String encryptedPassword = passwordEncoder(password);

			Role role = new Role();
			role.setName("ADMIN");

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

	public void setCountryCode() {

		try {

			File file = new File("/home/john/txt/countrycode.txt"); // creates a new file instance
			FileReader fr = new FileReader(file); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			String line;
			CountryCode country;
			String name = "";
			int code = 0;

			while ((line = br.readLine()) != null) {

				String parts[] = line.split("-");

				code = Integer.parseInt(parts[0].replaceAll("\\s", ""));

				name = parts[1];

				boolean resp = countryCodeRepository.existsByCode(code);

				if (!resp) {
					country = new CountryCode(name, code);
					countryCodeRepository.save(country);
					System.out.println("Saving: " + country);
				}

			}
			fr.close(); // closes the stream and release the resources

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setWeekTime() {

		try {

			File file = new File("/home/john/txt/weektime.txt"); // creates a new file instance
			FileReader fr = new FileReader(file); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			String line;
			WeekTime week;

			while ((line = br.readLine()) != null) {

				String parts[] = line.split(" ");

				int day = Integer.parseInt(parts[0]);

				boolean resp = weekRepository.existsByDay(day);

				if (!resp) {

					String dayInit = parts[1];
					String dayEnd = parts[2];
					boolean dayOff = Boolean.parseBoolean(parts[3]);
					String name = parts[4];
					// 1 00:00 00:00 false Domingo
					week = new WeekTime(day, dayInit, dayEnd, dayOff, name);
					weekRepository.save(week);
					System.out.println(week);
				}

			}
			fr.close(); // closes the stream and release the resources

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
