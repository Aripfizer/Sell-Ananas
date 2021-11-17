package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategorieRequest {
    @Length(min=3, max=20, message="Le nom de la catégorie doit compris etre entre 3 et 20 Caracteres")
    @NotEmpty(message = "Le nom de la categorie est requis")
    private String name;

    @Length(min=3, max=255, message="La description de la catégorie doit compris etre entre 3 et 255 Caracteres")
    @NotEmpty(message = "La description de la catégorie est requise")
    private String description;

}
