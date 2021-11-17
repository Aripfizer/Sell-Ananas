package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

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
public class BillController {

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

/*
    @GetMapping("/api/restaurant/{ifu}/user/{email}/bills")
    public ResponseEntity<BillResponse> saveRoil(@PathVariable String ifu, @PathVariable String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Restaurant> restaurant = restaurantRepository.findByIfu(ifu);

        if (user.isPresent() && restaurant.isPresent())
        {
            Optional<Shop> shop = shopRepository.findByIfuAndEmailAndState(ifu, email, false);
            if (shop.isPresent() && !shop.get().getOrderItems().isEmpty())
            {
                Commande commande = new Commande();
                CodeG codeGenerated = new CodeG(6);

                String codeCommande = codeGenerated.codeGenerator();
                while (orderRepository.existsByCode(codeCommande)) {
                    codeCommande = codeGenerated.codeGenerator();
                }
                commande.setCode(codeCommande);
                commande.setUser(userRepository.findByEmail(shop.get().getEmail()).get());
                commande.setState(false);
                commande = orderRepository.save(commande);

                List<OrderItem> ListeOrderItem = shop.get().getOrderItems();

                for (int i = 0; i < ListeOrderItem.size(); i++) {
                    OrderItem oi = ListeOrderItem.get(i);
                    oi.setCommande(commande);
                    oi.setState(true);
                    orderItemRepository.save(oi);
                }
                Shop sp = shop.get();
                sp.setState(true);
                shopRepository.save(sp);

                double montant = 0.0;

                List<OrderItem> orderItems = shop.get().getOrderItems();
                for (int i = 0; i < orderItems.size(); i++) {
                    montant += orderItems.get(i).getPrice().getAmont() * orderItems.get(i).getQuantity();
                }
                codeGenerated = new CodeG(6);
                String codeFacture = codeGenerated.codeGenerator();

                while (billRepository.existsByCode(codeFacture)) {
                    codeFacture = codeGenerated.codeGenerator();
                }

                Bill devis = new Bill();
                devis.setCode(codeFacture);
                devis.setCout(montant);
                devis.setCommande(commande);


                //Update commande State
                commande.setState(true);
                commande = orderRepository.save(commande);

                devis = billRepository.save(devis);

                //---------------------------------BillResponse----------------------------------
                //List<BillResponse> billResponses = new ArrayList<>();
                List<OrderItem> ListeOrderItems = shop.get().getOrderItems();
                BillResponse billResponse = new BillResponse();

                billResponse.setUser(user.get());
                billResponse.setRestaurant(restaurant.get());
                billResponse.setBill(devis);
                List<ProductBill> productBills = new ArrayList<>();


                for(int i = 0; i < ListeOrderItems.size(); i++)
                {
                    ProductBill productBill = new ProductBill();

                    Product p = shop.get().getOrderItems().get(i).getPrice().getProduct();
                    Price price = priceRepository.findByProductAndRestaurantAndActive(p, restaurant.get(), true).get();




                    ProductResponse pr = new ProductResponse();

                    pr.setRestaurant(restaurant.get());
                    pr.setCategorie(price.getProduct().getCategorie());
                    pr.setName(price.getProduct().getName());
                    pr.setMontant(price.getAmont());
                    pr.setCode(price.getProduct().getCode());
                    pr.setDescription(price.getProduct().getDescription());

                    productBill.setProductResponse(pr);
                    productBill.setQuantity(ListeOrderItems.get(i).getQuantity());
                    productBill.setMontant(price.getAmont() * ListeOrderItems.get(i).getQuantity());
                    productBills.add(productBill);
                }
                billResponse.setProductBills(productBills);
                return new ResponseEntity<BillResponse>(billResponse, HttpStatus.OK);
            }else
            {
                throw new ResourceNotFoundException("Le panier est vide");
            }

        }else
        {
            throw new ResourceNotFoundException("Le User ou Le Restaurant rechercher n'existe pas XXX");
        }
    }

 */
}
