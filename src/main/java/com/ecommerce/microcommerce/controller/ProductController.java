package com.ecommerce.microcommerce.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exception.ProduitIntrouvableException;
import com.ecommerce.microcommerce.model.Product;

@RestController
public class ProductController {
	@Autowired
	private ProductDao productDao;

	//Produits
	@GetMapping(value = "Produits")
	public List<Product> listProduits(){
		return productDao.findAll();
	}

	//Récupérer la liste des produits
//	@RequestMapping(value = "/Produits", method = RequestMethod.GET)
//	public MappingJacksonValue listeProduits() {
//
//		List<Product> produits = productDao.findAll();
//
//		SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat", "id");
//		FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
//		MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
//		produitsFiltres.setFilters(listDeNosFiltres);
//
//		return produitsFiltres;
//	}

	//Produit/{id}
	@GetMapping(value = "Produits/{id}")
	public Product afficherUnProduit(@PathVariable int id) throws ProduitIntrouvableException {
		Product product = productDao.findById(id);
		
		if(product == null) {
			throw new ProduitIntrouvableException("Le produit avec l'Id " + id + " n'hesiste pas");
		}
		
		return product;
	}

	@PostMapping(value = "/Produits")
	public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
		Product productAdded  = productDao.save(product);

		if(productAdded  == null) {
			return ResponseEntity.noContent().build();
		}

		//création d'une URI à partir de la requette
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(productAdded.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping(value = "test/produits/{prixLimit}")
	public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
		return productDao.chercherUnProduitCher(prixLimit);
	}
	
	@GetMapping(value = "test/produits/{recherche}")
    public List<Product> testeDeRequetes(@PathVariable String recherche) {
        return productDao.findByNomLike("%"+recherche+"%");
    }
	
//	@DeleteMapping (value = "/Produits/{id}")
//	public void supprimerProduit(@PathVariable int id) {
//		productDao.delete(id);
//	}
}
