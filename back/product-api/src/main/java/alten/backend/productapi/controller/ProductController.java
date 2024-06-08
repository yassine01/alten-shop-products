package alten.backend.productapi.controller;

import alten.backend.productapi.entity.Product;
import alten.backend.productapi.exception.ProductControllerException;
import alten.backend.productapi.exception.ProductNotFoundException;
import alten.backend.productapi.exception.ProductServiceException;
import alten.backend.productapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//TODO add roles

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;


    /**
     * Récupère la liste de tous les produits.
     *
     * @return La liste des produits avec le code HTTP 200 (OK) si trouvés, sinon le code HTTP 404 (Not Found).
     */
    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();
            return ResponseEntity.ok(products);
        } catch (ProductServiceException e) {
            logger.error("Error while fetching all products", e);
            throw new ProductControllerException(e.getMessage(), e.getCause() instanceof ProductNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Récupère un produit par son id.
     *
     * @param id L'id du produit à récupérer.
     * @return Le produit correspondant avec le code HTTP 200 (OK) si trouvé, sinon le code HTTP 404 (Not Found).
     * @throws ProductNotFoundException Si le produit n'est pas trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        try {
            Optional<Product> productOptional = productService.getProduct(id);
            return productOptional.map(ResponseEntity::ok)
                    .orElseThrow(() -> new ProductControllerException("Product not found with ID: " + id, HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            logger.error("Error while fetching product with ID: {}", id, e);
            throw new ProductControllerException("Error while fetching product", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Crée un nouveau produit.
     *
     * @param product Le produit à créer.
     * @return Le produit créé avec le code HTTP 201 (Created) si la création est réussie,
     * sinon le code HTTP 500 (Internal Server Error) en cas d'erreur.
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (RuntimeException e) {
            logger.error("Error while creating product", e);
            throw new ProductControllerException("Error while creating product", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Met à jour un produit existant.
     *
     * @param product Le produit mis à jour.
     * @param id L'id du produit à mettre à jour.
     * @return Le produit mis à jour avec le code HTTP 200 (OK) si la mise à jour est réussie,
     * sinon le code HTTP 404 (Not Found) si le produit n'est pas trouvé ou le code HTTP 500 (Internal Server Error) en cas d'erreur.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long id) {
        try {
            Optional<Product> existingProductOptional = productService.getProduct(id);

            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();
                productService.updateProductFields(existingProduct, product);
                Product updatedProduct = productService.updateProduct(existingProduct);

                return ResponseEntity.ok(updatedProduct);
            } else {
                throw new ProductControllerException("Product not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            logger.error("Error while updating product", e);
            throw new ProductControllerException("Error while updating product", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Supprime un produit par son id.
     *
     * @param id L'id du produit à supprimer.
     * @return Le code HTTP 204 (No Content) si le produit est supprimé,
     * sinon le code HTTP 404 (Not Found) si le produit n'est pas trouvé ou le code HTTP 500 (Internal Server Error) en cas d'erreur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            Optional<Boolean> deleted = productService.deleteProduct(id);

            if (deleted.isPresent() && deleted.get()) {
                return ResponseEntity.noContent().build();
            } else {
                throw new ProductControllerException("Product not found with Id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting product", e);
            throw new ProductControllerException("Error while deleting product", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
