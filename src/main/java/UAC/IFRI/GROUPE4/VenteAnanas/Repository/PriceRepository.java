package UAC.IFRI.GROUPE4.VenteAnanas.Repository;

import UAC.IFRI.GROUPE4.VenteAnanas.Models.Categorie;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Price;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findDistinctByActive(boolean active);
    Optional<Price> findByProductAndActive(Product product, boolean active);
    List<Price> findDistinctByCategorieAndCategorieDeleteAtAndProductDeleteAtAndActive(Categorie categorie, boolean categorieDeleteAt, boolean productDeleteAt, boolean active);
    boolean existsByProductNameAndCategorieName(String productName, String categorieName);
}
