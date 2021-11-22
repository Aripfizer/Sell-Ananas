package UAC.IFRI.GROUPE4.VenteAnanas.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Shop extends DateAudit
{
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @NotNull(message = "L'email est requis")
    private String email;

    @Nullable
    private double amont;

    private boolean state = false;

    //OneToMany OrderItem
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "shop")
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    //OneToOne Bill

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "shop")
    @JsonIgnoreProperties("shop")
    private Bill bill;


}
