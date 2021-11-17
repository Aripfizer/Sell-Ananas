package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest
{
    @NotNull(message = "Le nom du produit est requis")
    private String name;
    @DecimalMin(value = "100.0", inclusive = false)
    private double montant;
    @Nullable
    private String description;
    @Nullable
    private Long categorie;
}
