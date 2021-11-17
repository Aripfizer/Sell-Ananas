package UAC.IFRI.GROUPE4.VenteAnanas.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItem extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Min(value =  1, message="Vous devez selectionner au moin un produit")
    @NotNull(message = "Le champs quantite est requis")
    private int quantity;

    @Min(value =  100, message="Verifier le prix du product")
    private double amont;

    private boolean state = false;


    //ManyToOne SHOP

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "shop_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Shop shop;

    //ManyToOne Price

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "price_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Price price;

}
