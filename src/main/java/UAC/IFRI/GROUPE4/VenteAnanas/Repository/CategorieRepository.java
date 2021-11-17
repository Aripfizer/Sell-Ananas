package UAC.IFRI.GROUPE4.VenteAnanas.Repository;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Optional<Categorie> findByIdAndDeleteAt(Long id, boolean deleteAt);
    List<Categorie> findAllByDeleteAt(boolean deletAt);
}
