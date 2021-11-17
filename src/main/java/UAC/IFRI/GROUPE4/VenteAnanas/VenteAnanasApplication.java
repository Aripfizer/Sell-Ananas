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

/*
	@Bean
	CommandLineRunner run(AuthController authController) {

		return args -> {
			authController.registerUser(new SignUpRequest("Dossou", "Ariel", "Ariel", "arieldossou00@gmail.com","ariel", "67180009"));

		};
	}


 */

}
