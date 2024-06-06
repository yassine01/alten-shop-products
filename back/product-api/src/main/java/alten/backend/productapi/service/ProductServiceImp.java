package alten.backend.productapi.service;

import alten.backend.productapi.entity.Product;
import alten.backend.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> deleteProduct(Long id) {
        return Optional.empty();
    }
}
