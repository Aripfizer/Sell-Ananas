package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Exception.ResourceNotFoundException;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.*;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest.OrderItemRequest;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.OrderItemResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ProductResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class OrderItemController {
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

    @PostMapping("/api/orderItem")
    public ResponseEntity<OrderItemResponse> saveOrderItem(@Valid @RequestBody OrderItemRequest orderItemRequest)
    {
        Optional<User> user = userRepository.findByEmail(orderItemRequest.getEmail());
        Optional<Product> product = productRepository.findByIdAndDeleteAt(orderItemRequest.getId(), false);
        if(user.isPresent() && product.isPresent())
        {
            Optional<Shop>  shop = shopRepository.findByEmailAndState(orderItemRequest.getEmail(), false);
            if (!shop.isPresent())
            {
                Shop shopUser = new Shop();
                //Shop Object
                shopUser.setEmail(orderItemRequest.getEmail());
                shopUser.setState(false);
                shopUser = shopRepository.save(shopUser);

                shop = shopRepository.findByEmailAndState(orderItemRequest.getEmail(), false);
            }
            OrderItem orderItem = new OrderItem();

            orderItem.setQuantity(orderItemRequest.getQuantityProduct());
            //getPrice
            Optional<Price> price = priceRepository.findByProductAndActive(product.get(),true);
            orderItem.setPrice(price.get());
            orderItem.setState(false);
            orderItem.setAmont(price.get().getAmont() * orderItemRequest.getQuantityProduct());
            orderItem.setShop(shop.get());

            orderItem = orderItemRepository.save(orderItem);

            ProductResponse productResponse = new ProductResponse();

            productResponse.setName(product.get().getName());
            productResponse.setId(product.get().getId());
            productResponse.setMontant(orderItem.getAmont());
            productResponse.setDescription(product.get().getDescription());


            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setId(orderItem.getId());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setMontant(orderItem.getAmont());
            orderItemResponse.setProduct(productResponse);

            return new ResponseEntity<>(orderItemResponse, HttpStatus.CREATED);
        }
        else
        {
            throw new ResourceNotFoundException("User", "Product", "Le User ou le Product n'existe pas");
        }

    }

    @DeleteMapping("/api/user/{email}/orderItem/{id}")
    public ResponseEntity<?> deleteOrderItemById(@PathVariable String email, @PathVariable Long id)
    {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        if(user.isPresent() && orderItem.isPresent())
        {
            orderItemRepository.delete(orderItem.get());
            return ResponseEntity.ok().build();
        }else
        {
            throw  new ResourceNotFoundException("User", "OrderItem", "Le User ou le orderItem n'existe pas!");
        }

    }

    @PutMapping("/api/orderItem/{id}")
    public ResponseEntity<OrderItemResponse> updateQuantity(@PathVariable Long id, @Valid @RequestBody int quantity)
    {
        return orderItemRepository.findById(id).map(orderItem -> {
            if(quantity > 0)
            {
                orderItem.setQuantity(quantity);
                orderItem.setAmont(orderItem.getPrice().getAmont() * quantity);
                orderItem = orderItemRepository.save(orderItem);

                Product product = orderItem.getPrice().getProduct();

                ProductResponse productResponse = new ProductResponse();

                Price price = priceRepository.findByProductAndActive(product,true).get();

                productResponse.setId(product.getId());
                productResponse.setName(product.getName());
                productResponse.setMontant(price.getAmont());
                productResponse.setDescription(product.getDescription());

                OrderItemResponse orderItemResponse = new OrderItemResponse();

                orderItemResponse.setId(orderItem.getId());
                orderItemResponse.setQuantity(orderItem.getQuantity());
                orderItemResponse.setMontant(orderItem.getAmont());
                orderItemResponse.setProduct(productResponse);

                return new ResponseEntity<>(orderItemResponse, HttpStatus.CREATED);
            }else
            {
                throw new ResourceNotFoundException("Produit", "OrderItem","La quantité doit etre superieur a zero");
            }

        }).orElseThrow(() -> new ResourceNotFoundException("Produit", "OrderItem", "L'Element de la commande n'existe pas !"));
    }

}
