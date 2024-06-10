package alten.backend.productapi.service;

import alten.backend.productapi.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAllProducts();
    Product saveProduct(Product product);
    Product updateProduct(Product product);
    Optional<Product> getProduct(Long id);
    Optional<Boolean> deleteProduct(Long id);
    Product updateProductFields(Product existingProduct, Product newProduct);

}
