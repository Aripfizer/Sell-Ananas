package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse;

import UAC.IFRI.GROUPE4.VenteAnanas.Models.Categorie;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesProducts {
    private Categorie categorie;
    List<ProductResponse> productList = new ArrayList<>();
}
