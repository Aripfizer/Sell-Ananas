package UAC.IFRI.GROUPE4.VenteAnanas;

import UAC.IFRI.GROUPE4.VenteAnanas.Controller.AuthController;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Role;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.User;
import UAC.IFRI.GROUPE4.VenteAnanas.Payload.SignUpRequest;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.RoleRepository;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		VenteAnanasApplication.class,
		Jsr310JpaConverters.class
})
public class VenteAnanasApplication {
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(VenteAnanasApplication.class, args);
	}


	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository) {
		return args -> {
			Role role1 = roleRepository.save(new Role("ROLE_USER"));
			Role role2 = roleRepository.save(new Role("ROLE_ADMIN"));


			User user = userRepository.save(new User("Dossou", "Ariel", "AriKing", "arieldossou00@gmail.com","ariel", "67180009"));
			User user2 = userRepository.save(new User("LOKONON", "Arnaud", "Admin", "admin@admin.com","admin@Arnaud", "68947612"));

			user.getRoles().add(role1);
			user.getRoles().add(role2);
			user2.getRoles().add(role1);
			user2.getRoles().add(role2);

		};
	}


}
