package alten.backend.productapi.service.imp;

import alten.backend.productapi.entity.Product;
import alten.backend.productapi.exception.ProductServiceException;
import alten.backend.productapi.repository.ProductRepository;
import alten.backend.productapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    @Autowired
    private ProductRepository productRepository;


    /**
     * Récupère tous les produits de la base de données.
     * @return Une liste de tous les produits, ou une liste vide si aucun produit n'est trouvé.
     * @throws ProductServiceException Si une erreur survient lors de la récupération des produits.
     */
    @Override
    public List<Product> findAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            logger.info("Found : {} products", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Error while fetching all products..", e);
            throw new ProductServiceException("Error while fetching all products..", e);
        }
    }


    /**
     * Enregistre un nouveau produit dans la base de données.
     * @param product Le produit à enregistrer.
     * @return Le produit enregistré.
     * @throws ProductServiceException Si une erreur survient lors de l'enregistrement du produit.
     */
    @Override
    public Product saveProduct(Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            logger.info("Product saved successfully: {}", savedProduct);
            return savedProduct;
        } catch (Exception e) {
            logger.error("Error while saving product", e);
            throw new ProductServiceException("Error while saving product", e);
        }
    }


    /**
     * Met à jour un produit existant dans la base de données.
     * @param product Le produit à mettre à jour.
     * @return Le produit mis à jour.
     * @throws ProductServiceException Si une erreur survient lors de la mise à jour du produit.
     */
    @Override
    public Product updateProduct(Product product) {
        try {
            Product updatedProduct = productRepository.save(product);
            logger.info("Product updated successfully: {}", updatedProduct);
            return updatedProduct;
        } catch (Exception e) {
            logger.error("Error while updating product", e);
            throw new ProductServiceException("Error while updating product", e);
        }
    }


    /**
     * Récupère un produit par son ID.
     * @param id L'ID du produit à récupérer.
     * @return Un Optional contenant le produit si trouvé, sinon un Optional vide.
     * @throws ProductServiceException Si une erreur survient lors de la récupération du produit.
     */
    @Override
    public Optional<Product> getProduct(Long id) {
        try {
            Optional<Product> product = productRepository.findById(id);

            if (product.isPresent()) {
                logger.info("Product found: {}", product.get());
            } else {
                logger.info("Product not found with ID: {}", id);
            }
            return product;

        } catch (Exception e) {
            logger.error("Error while fetching product with ID: {}", id, e);
            throw new ProductServiceException("Error while fetching product", e);
        }
    }


    /**
     * Supprime un produit par son ID.
     * @param id L'ID du produit à supprimer.
     * @return Un Optional contenant true si le produit a été supprimé, false sinon, et une exception si une erreur survient.
     * @throws ProductServiceException Si une erreur survient lors de la suppression du produit.
     */
    @Override
    public Optional<Boolean> deleteProduct(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                productRepository.delete(product);
                logger.info("Product deleted successfully: {}", product);
                return Optional.of(true);

            } else {
                logger.info("Product not found with ID: {}", id);
                return Optional.of(false);
            }
        } catch (Exception e) {
            logger.error("Error while deleting product with ID: {}", id, e);
            throw new ProductServiceException("Error while deleting product", e);
        }
    }


    /**
     * Met à jour les champs d'un produit existant avec les valeurs non nulles d'un nouveau produit.
     *
     * @param existingProduct Le produit existant à mettre à jour.
     * @param newProduct      Le nouveau produit contenant les valeurs à mettre à jour.
     * @return
     * @throws ProductServiceException Si une erreur survient lors de la mise à jour du produit.
     */
    @Override
    public Product updateProductFields(Product existingProduct, Product newProduct) {
        try {
            existingProduct.setCode(newProduct.getCode() != null ? newProduct.getCode() : existingProduct.getCode());
            existingProduct.setName(newProduct.getName() != null ? newProduct.getName() : existingProduct.getName());
            existingProduct.setDescription(newProduct.getDescription() != null ? newProduct.getDescription() : existingProduct.getDescription());
            existingProduct.setPrice(newProduct.getPrice() != null ? newProduct.getPrice() : existingProduct.getPrice());
            existingProduct.setQuantity(newProduct.getQuantity() != null ? newProduct.getQuantity() : existingProduct.getQuantity());
            existingProduct.setInventoryStatus(newProduct.getInventoryStatus() != null ? newProduct.getInventoryStatus() : existingProduct.getInventoryStatus());
            existingProduct.setCategory(newProduct.getCategory() != null ? newProduct.getCategory() : existingProduct.getCategory());
            existingProduct.setImage(newProduct.getImage() != null ? newProduct.getImage() : existingProduct.getImage());
            existingProduct.setRating(newProduct.getRating() != null ? newProduct.getRating() : existingProduct.getRating());

            Product updatedProduct = productRepository.save(existingProduct);
            logger.info("Product updated successfully: {}", updatedProduct);

        } catch (Exception e) {
            logger.error("Error while updating product fields", e);
            throw new ProductServiceException("Error while updating product fields", e);
        }
        return existingProduct;
    }

}
