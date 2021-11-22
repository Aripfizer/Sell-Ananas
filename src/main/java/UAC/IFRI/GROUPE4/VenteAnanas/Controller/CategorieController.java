package UAC.IFRI.GROUPE4.VenteAnanas.Controller;

import UAC.IFRI.GROUPE4.VenteAnanas.Exception.ResourceNotFoundException;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.Categorie;
import UAC.IFRI.GROUPE4.VenteAnanas.Models.ModelRequest.CategorieRequest;
import UAC.IFRI.GROUPE4.VenteAnanas.Repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class CategorieController
{
    @Autowired
    private CategorieRepository categorieRepository;


    @GetMapping("/api/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Categorie>> findAllCategories()
    {
        return new ResponseEntity<>(categorieRepository.findAllByDeleteAt(false), HttpStatus.OK);
    }


    @PostMapping("/api/categorie")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> insertcategorie(@Valid @RequestBody CategorieRequest categorieRequest)
    {
        Categorie categorie = new Categorie();
        categorie.setName(categorieRequest.getName());
        categorie.setDescription(categorieRequest.getDescription());
        categorie.setDeleteAt(false);
        return new ResponseEntity<>(categorieRepository.save(categorie), HttpStatus.CREATED);
    }


    @GetMapping("/api/categorie/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> getOneCategorie(@PathVariable Long id)
    {
        Optional<Categorie> categorie = categorieRepository.findByIdAndDeleteAt(id, false);
        if(categorie.isPresent())
        {
            return new ResponseEntity<>(categorie.get(), HttpStatus.OK);
        }
       throw new ResourceNotFoundException("Product", "Categorie", "La categorie n'existe pas");
    }

    @PutMapping("/api/categorie/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> updateCategorie(@Valid @RequestBody CategorieRequest categorieRequest, @PathVariable Long id)
    {
        Optional<Categorie> cat = categorieRepository.findByIdAndDeleteAt(id, false);
        if(cat.isPresent())
        {
            Categorie categorie = cat.get();
            categorie.setName(categorieRequest.getName());
            categorie.setDescription(categorieRequest.getDescription());
            categorie.setDeleteAt(false);
            return new ResponseEntity<>(categorieRepository.save(categorie), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Product", "Categorie", "La categorie n'existe pas");
    }

    @DeleteMapping("/api/categorie/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategorie(@PathVariable Long id)
    {
        Optional<Categorie> cat = categorieRepository.findByIdAndDeleteAt(id, false);
        if(cat.isPresent())
        {
            Categorie categorie = cat.get();
            categorie.setDeleteAt(true);
            categorieRepository.save(categorie);
            return ResponseEntity.ok().build();
        }
        throw new ResourceNotFoundException("Product", "Categorie", "La categorie n'existe pas");
    }
}
