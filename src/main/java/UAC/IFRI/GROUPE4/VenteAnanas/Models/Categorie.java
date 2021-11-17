package UAC.IFRI.GROUPE4.VenteAnanas.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Categorie extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Length(min=3, max=20, message="Le nom de la catégorie doit compris etre entre 3 et 20 Caracteres")
    @NotEmpty(message = "Le nom de la categorie est requis")
    private String name;

    @Length(min=3, max=255, message="La description de la catégorie doit compris etre entre 3 et 255 Caracteres")
    @NotEmpty(message = "La description de la catégorie est requise")
    private String description;

    private boolean deleteAt = false;

    //OneToMany Price

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "categorie"
    )
    @JsonIgnore
    private List<Price> prices = new ArrayList<>();
}
