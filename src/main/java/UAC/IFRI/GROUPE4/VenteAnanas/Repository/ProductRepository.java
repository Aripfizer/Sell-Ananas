package UAC.IFRI.GROUPE4.VenteAnanas.Repository;

import UAC.IFRI.GROUPE4.VenteAnanas.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeleteAt(Long id, boolean deleteAt);

    List<Product> findAllByDeleteAt(boolean deleteAt);
}
