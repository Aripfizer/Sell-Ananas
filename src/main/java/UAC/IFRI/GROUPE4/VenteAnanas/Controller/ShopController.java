package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.OrderItemResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ProductResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ShopResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.OrderItem;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Price;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Product;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Shop;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.*;
import UAC.IFRI.GROUPE4.VenteAnanas.Security.CurrentUser;
import UAC.IFRI.GROUPE4.VenteAnanas.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/api/user/shop")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShopResponse> findShopByUserEmail(@CurrentUser UserPrincipal currentUser)
    {
        ShopResponse shopResponse = new ShopResponse();

        String email  = currentUser.getEmail();
        Optional<Shop> shop = shopRepository.findByEmailAndState(email, false);
        List<OrderItemResponse> ListeOrderItemResponse = new ArrayList<>();
        Double Amont = 0.0;
        shopResponse.setEmail(email);
        shopResponse.setEmail(email);
        shopResponse.setOrderItems(ListeOrderItemResponse);

        if(shop.isPresent())
        {
            List<OrderItem> ListeOI = shop.get().getOrderItems();
            if(ListeOI.isEmpty())
            {
                return new ResponseEntity<>(shopResponse, HttpStatus.OK);
            }

            for(OrderItem orderItem : ListeOI)
            {
                Amont += orderItem.getAmont();
                Product p = orderItem.getPrice().getProduct();
                Price price = priceRepository.findByProductAndActive(p,true).get();

                ProductResponse productResponse = new ProductResponse();
                productResponse.setName(p.getName());
                productResponse.setId(p.getId());
                productResponse.setMontant(price.getAmont());
                productResponse.setCategorie(price.getCategorie().getName());
                productResponse.setDescription(p.getDescription());


                OrderItemResponse orderItemResponse = new OrderItemResponse();
                orderItemResponse.setId(orderItem.getId());
                orderItemResponse.setQuantity(orderItem.getQuantity());
                orderItemResponse.setMontant(orderItem.getAmont());
                orderItemResponse.setProduct(productResponse);

                ListeOrderItemResponse.add(orderItemResponse);
            }


            Shop sh = shop.get();
            sh.setAmont(Amont);
            shopRepository.save(sh);

            
            shopResponse.setOrderItems(ListeOrderItemResponse);
            shopResponse.setAmont(Amont);

            return new ResponseEntity<>(shopResponse, HttpStatus.OK);
        }else
        {
            Shop shopUser = new Shop();
            shopUser.setEmail(email);
            shopUser.setState(false);
            shopUser = shopRepository.save(shopUser);
            return new ResponseEntity<>(shopResponse, HttpStatus.OK);
        }

    }


}
