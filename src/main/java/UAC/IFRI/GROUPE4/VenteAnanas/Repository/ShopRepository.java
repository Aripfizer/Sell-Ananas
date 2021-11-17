package UAC.IFRI.GROUPE4.VenteAnanas.Repository;


import UAC.IFRI.GROUPE4.VenteAnanas.Models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByEmailAndState(String email, boolean state);
}
