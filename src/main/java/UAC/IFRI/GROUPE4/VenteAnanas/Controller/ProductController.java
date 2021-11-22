package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Exception.ResourceNotFoundException;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Categorie;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest.ProductRequest;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelResponse.ProductResponse;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Price;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Product;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.CategorieRepository;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.PriceRepository;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.ProductRepository;
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
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PriceRepository priceRepository;
    @Autowired
    CategorieRepository categorieRepository;

/*
    @GetMapping("/api/categories/products")
    public ResponseEntity<List<CategoriesProducts>> findAllCategorieAndProducts()
    {
        List<CategoriesProducts> categoriesProductsList = new ArrayList<>();
        List<Categorie> categorieList = categorieRepository.findAllByDeleteAt(false);



        for(Categorie categorie : categorieList)
        {
            List<ProductResponse> productResponseList = new ArrayList<>();
            List<Price> ListPrice = priceRepository.findDistinctByCategorieAndCategorieDeleteAtAndProductDeleteAtAndActive(categorie, false, false, true);
            for (Price price : ListPrice)
            {
                Product product = price.getProduct();


                ProductResponse productResponse = new ProductResponse();
                productResponse.setMontant(price.getAmont());
                productResponse.setName(product.getName());
                productResponse.setId(product.getId());
                productResponse.setCategorie(price.getCategorie().getName());
                productResponse.setDescription(product.getDescription());


                productResponseList.add(productResponse);
            }
            CategoriesProducts categoriesProducts = new CategoriesProducts();

            categoriesProducts.setCategorie(categorie);
            categoriesProducts.setProductList(productResponseList);

            categoriesProductsList.add(categoriesProducts);
        }
        return new ResponseEntity<>(categoriesProductsList, HttpStatus.OK);
    }
*/


    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> findAllProducts()
    {
        List<ProductResponse> productResponseList = new ArrayList<>();
        List<Product> productList = productRepository.findAllByDeleteAt(false);
        if(!productList.isEmpty())
        {
            for (Product product : productList)
            {
                Price price = priceRepository.findByProductAndActive(product,true).get();
                //ProductResponse
                ProductResponse productResponse = new ProductResponse();
                productResponse.setId(product.getId());
                productResponse.setMontant(price.getAmont());
                productResponse.setName(product.getName());
                productResponse.setCategorie(price.getCategorie().getName());
                productResponse.setDescription(product.getDescription());

                productResponseList.add(productResponse);
            }
        }

        return new ResponseEntity<>(productResponseList, HttpStatus.OK);
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<ProductResponse>  getProduct(@PathVariable Long id)
    {
       Optional<Product> product = productRepository.findByIdAndDeleteAt(id, false);
        if (product.isPresent())
        {
            Price price = priceRepository.findByProductAndActive(product.get(),true).get();
            //ProductResponse
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.get().getId());
            productResponse.setMontant(price.getAmont());
            productResponse.setName(product.get().getName());
            productResponse.setCategorie(price.getCategorie().getName());
            productResponse.setDescription(product.get().getDescription());

            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        }else
        {
            throw new ResourceNotFoundException("Product", "id", "Le produit  n'existe pas");
        }
    }

    @GetMapping("/api/categorie/{id}/products")
    public ResponseEntity<List<ProductResponse>> getAllProductByCategorie(@PathVariable Long id)
    {
            List<ProductResponse> ListProductResponse = new ArrayList<>();

        Optional<Categorie> cat = categorieRepository.findByIdAndDeleteAt(id, false);
        if(cat.isPresent())
        {
            List<Price> ListPrice = priceRepository.findDistinctByCategorieAndCategorieDeleteAtAndProductDeleteAtAndActive(cat.get(), false, false, true);
            for (Price price : ListPrice)
            {
                Product product = price.getProduct();


                //Price price = priceRepository.findByProductAndRestaurantAndActive(product, restaurant.get(), true).get();
                //ProductResponse
                ProductResponse productResponse = new ProductResponse();
                productResponse.setMontant(price.getAmont());
                productResponse.setName(product.getName());
                productResponse.setId(product.getId());
                productResponse.setCategorie(price.getCategorie().getName());
                productResponse.setDescription(product.getDescription());


                ListProductResponse.add(productResponse);
            }
            return new ResponseEntity<>(ListProductResponse, HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Product", "Categorie", "La categorie n'existe pas OU a été supprimer ! ");
    }

    @PostMapping("/api/product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> addNewProduct(@Valid @RequestBody ProductRequest productRequest)
    {
        Optional<Categorie> cat = categorieRepository.findByIdAndDeleteAt(productRequest.getCategorie(), false);
        if(cat.isPresent())
        {
            ProductResponse productResponse = new ProductResponse();
            if(priceRepository.existsByProductNameAndCategorieName(productRequest.getName(), cat.get().getName()))
            {
                throw new ResourceNotFoundException("Product", "Name", "Le produit existe deja dans la categorie");

            } else
            {
                //Produit

                Product product = new Product();
                product.setName(productRequest.getName());
                product.setDescription(productRequest.getDescription());
                product.setDeleteAt(false);
                product = productRepository.save(product);


                //Price
                Price price = new Price();

                price.setAmont(productRequest.getMontant());
                price.setProduct(product);
                price.setCategorie(cat.get());
                price.setActive(true);

                price = priceRepository.save(price);
                product = price.getProduct();

                productResponse.setId(product.getId());
                productResponse.setName(product.getName());
                productResponse.setMontant(price.getAmont());
                productResponse.setCategorie(price.getCategorie().getName());
                productResponse.setDescription(product.getDescription());
                return new ResponseEntity<>(productResponse, HttpStatus.CREATED) ;
            }
        }
        throw new ResourceNotFoundException("Product", "Cetegorie", "La categorie n'existe pas ou a été supprimer");
    }


    @PutMapping("/api/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse editProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest)
    {
        Optional<Product> product = productRepository.findByIdAndDeleteAt(id, false);
        Optional<Categorie> cat = categorieRepository.findByIdAndDeleteAt(productRequest.getCategorie(), false);
        Price priceAvant = priceRepository.findByProductAndActive(product.get(),true).get();
        if(product.isPresent() && cat.isPresent())
        {
            Product produit = product.get();
            produit.setDescription(productRequest.getDescription());
            produit.setName(productRequest.getName());
            produit = productRepository.save(produit);


            int compteur = 0;
            List<Price> ListPrices= produit.getPrices();
            Price price = new Price();

            if(!ListPrices.isEmpty())
            {
                for(Price p : ListPrices)
                {
                    p.setActive(false);
                    priceRepository.save(p);

                    if(p.getAmont() == productRequest.getMontant())
                    {
                        p.setActive(true);
                        price = priceRepository.save(p);
                        compteur++;
                    }
                }
            }
            if(compteur == 0)
            {
                //Price

                price.setAmont(productRequest.getMontant());
                price.setProduct(produit);
                price.setActive(true);
            }
            Optional<Product> pro = productRepository.findByIdAndDeleteAt(id, false);

            ListPrices = pro.get().getPrices();

            Categorie categorie = priceAvant.getCategorie();
            if(categorie.equals(cat.get()))
            {
                price.setCategorie(categorie);
                priceRepository.save(price);
            }
            else {
                price.setCategorie(cat.get());
                priceRepository.save(price);
            }

            ProductResponse productResponse = new ProductResponse();

            productResponse.setName(produit.getName());
            productResponse.setId(produit.getId());
            productResponse.setMontant(price.getAmont());
            productResponse.setCategorie(price.getCategorie().getName());
            productResponse.setDescription(produit.getDescription());

            return productResponse;
        }
        else
        {
            throw new ResourceNotFoundException("Product", "id", "La produit ou la Catégorie n'existe pas ou a été supprimer");
        }
    }

    @DeleteMapping(path = "/api/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduce(@PathVariable Long id)
    {
        Optional<Product> product = productRepository.findByIdAndDeleteAt(id, false);

        if(product.isPresent())
        {
            Product produit = product.get();
            produit.setDeleteAt(true);
            productRepository.save(produit);
            return ResponseEntity.ok().build();
        }else
        {
            throw new ResourceNotFoundException("Product", "id", "La produit n'existe pas ou a été supprimer");
        }
    }

}
