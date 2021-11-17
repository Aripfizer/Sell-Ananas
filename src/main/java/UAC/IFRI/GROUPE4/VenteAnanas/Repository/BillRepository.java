package UAC.IFRI.GROUPE4.VenteAnanas.Repository;

import UAC.IFRI.GROUPE4.VenteAnanas.Models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
