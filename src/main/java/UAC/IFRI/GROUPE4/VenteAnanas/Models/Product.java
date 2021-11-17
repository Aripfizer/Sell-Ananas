package UAC.IFRI.GROUPE4.VenteAnanas.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.javafx.geom.transform.Identity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Length(min=3, max=20, message="Le nom du produit doit compris etre entre 3 et 20 Caracteres")
    @NotEmpty(message = "Le nom du Product est requis")
    private String name;

    @Length(min=3, max=255, message="La description du produit doit compris etre entre 3 et 255 Caracteres")
    @NotEmpty(message = "La description du Product est requise")
    private String description;

    private boolean deleteAt = false;

    //OneToMany Price

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    @JsonIgnore
    private List<Price> prices = new ArrayList<>();



}
