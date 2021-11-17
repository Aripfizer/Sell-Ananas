package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;
    private double montant;
}
