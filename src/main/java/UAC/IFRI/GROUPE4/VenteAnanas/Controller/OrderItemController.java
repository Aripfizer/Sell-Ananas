package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Exception.BadRequestException;
import UAC.IFRI.GROUPE4.VenteAnanas.Exception.ResourceNotFoundException;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.*;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest.OrderItemRequest;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.OrderItemResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ProductResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.*;
import UAC.IFRI.GROUPE4.VenteAnanas.Security.CurrentUser;
import UAC.IFRI.GROUPE4.VenteAnanas.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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



    @GetMapping("/api/orderItems")
    public ResponseEntity<List<OrderItem>> findAllOrderItemss()
    {
        return new ResponseEntity<>(orderItemRepository.findAll(), HttpStatus.OK);
    }
    @GetMapping("/api/orderItem/{id}")
    public ResponseEntity<OrderItem> findOrderItem(@PathVariable Long id)
    {
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        if(orderItem.isPresent())
        {
            return new ResponseEntity<>(orderItem.get(), HttpStatus.OK);
        } else
        {
            throw new ResourceNotFoundException("User", "OrderItem", "Not Found");
        }

    }

    @PostMapping("/api/orderItem")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemResponse> saveOrderItem(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody OrderItemRequest orderItemRequest)
    {
        String email = currentUser.getEmail();
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Product> product = productRepository.findByIdAndDeleteAt(orderItemRequest.getId(), false);
        if(user.isPresent() && product.isPresent())
        {
            Optional<Shop>  shop = shopRepository.findByEmailAndState(email, false);
            if (!shop.isPresent())
            {
                Shop shopUser = new Shop();
                //Shop Object
                shopUser.setEmail(email);
                shopUser.setState(false);
                shopUser = shopRepository.save(shopUser);

                shop = shopRepository.findByEmailAndState(email, false);
            }
            List<OrderItem> orderItemList = shop.get().getOrderItems();
            boolean trouver = false;
            for(OrderItem orderItem : orderItemList)
            {
                if(orderItem.getPrice().getProduct().equals(product.get()))
                {
                    trouver = true;
                }
            }
            if(trouver)
            {
                throw new BadRequestException("Le produit existe deja dans le panier pas");
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
            productResponse.setCategorie(price.get().getCategorie().getName());
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

    @DeleteMapping("/api/orderItem/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOrderItemById(@CurrentUser UserPrincipal currentUser, @PathVariable Long id)
    {
        String email = currentUser.getEmail();
        Optional<User> user = userRepository.findByEmail(email);

        Optional<OrderItem> orderItem = orderItemRepository.findById(id);

        if(orderItem.isPresent() && user.isPresent())
        {
            orderItemRepository.delete(orderItem.get());
            return ResponseEntity.ok().build();
        } else
        {
            throw new ResourceNotFoundException("User", "OrderItem", "Le User ou le OrderItem N'existe pas");
        }


    }
    @PutMapping("/api/orderItem/{id}/quantity/{quantity}")
    public ResponseEntity<OrderItemResponse> updateQuantity(@PathVariable Long id, @PathVariable int quantity)
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
