package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest
{
    @NotNull(message = "Entrer l'id du produit")
    private Long id;
    @NotNull(message = "La quatité du produit est requis")
    @Min(value = 1, message = "Selectionner au moin 1 produit")
    private int quantityProduct;
}
