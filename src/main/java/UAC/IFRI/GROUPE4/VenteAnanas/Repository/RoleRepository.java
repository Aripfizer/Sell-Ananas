package UAC.IFRI.GROUPE4.VenteAnanas.Repository;

import UAC.IFRI.GROUPE4.VenteAnanas.Models.Role;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}