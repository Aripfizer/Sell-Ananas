package UAC.IFRI.GROUPE4.VenteAnanas.Repository;


import UAC.IFRI.GROUPE4.VenteAnanas.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
