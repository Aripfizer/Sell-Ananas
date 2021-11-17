package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Exception.ResourceNotFoundException;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.OrderItemResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ProductResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.OrderItem;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Price;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Product;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Shop;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class ShopController {
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PriceRepository priceRepository;
    @Autowired
    CategorieRepository categorieRepository;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/api/user/{email}/shop")
    public ResponseEntity<List<OrderItemResponse>> findShopByUserEmail(@PathVariable String email)
    {
        Optional<Shop> shop = shopRepository.findByEmailAndState(email, false);
        List<OrderItemResponse> ListeOrderItemResponse = new ArrayList<>();
        if(shop.isPresent())
        {
            List<OrderItem> ListeOI = shop.get().getOrderItems();
            if(ListeOI.isEmpty())
            {
                return new ResponseEntity<List<OrderItemResponse>>(ListeOrderItemResponse, HttpStatus.OK);
            }

            for(OrderItem orderItem : ListeOI)
            {
                Product p = orderItem.getPrice().getProduct();
                Price price = priceRepository.findByProductAndActive(p,true).get();

                ProductResponse productResponse = new ProductResponse();
                productResponse.setName(p.getName());
                productResponse.setId(p.getId());
                productResponse.setMontant(price.getAmont());
                productResponse.setDescription(p.getDescription());


                OrderItemResponse orderItemResponse = new OrderItemResponse();
                orderItemResponse.setId(orderItem.getId());
                orderItemResponse.setQuantity(orderItem.getQuantity());
                orderItemResponse.setMontant(orderItem.getAmont());
                orderItemResponse.setProduct(productResponse);

                ListeOrderItemResponse.add(orderItemResponse);
            }

            return new ResponseEntity<List<OrderItemResponse>>(ListeOrderItemResponse, HttpStatus.OK);
        }else
        {
            Shop shopUser = new Shop();
            shopUser.setEmail(email);
            shopUser.setState(false);
            shopUser = shopRepository.save(shopUser);
            return new ResponseEntity<List<OrderItemResponse>>(ListeOrderItemResponse, HttpStatus.OK);
        }

    }


}
