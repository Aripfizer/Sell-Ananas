package UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponse
{
    private String email;
    List<OrderItemResponse> orderItems = new ArrayList<>();
    private Double Amont;
}
