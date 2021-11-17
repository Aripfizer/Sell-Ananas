package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductBill
{
    private ProductResponse productResponse;
    private int quantity;
    private double montant;

}
