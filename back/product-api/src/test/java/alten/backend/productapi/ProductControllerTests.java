package alten.backend.productapi;

import alten.backend.productapi.controller.ProductController;
import alten.backend.productapi.entity.Product;
import alten.backend.productapi.exception.ProductServiceException;
import alten.backend.productapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductControllerTests {

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetAllProducts_success() {
        List<Product> mockProducts = List.of(
                new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                        "bamboo-watch.jpg", 65.0, "Accessories", 24L, "INSTOCK", 5),
                new Product(1001L, "nvklal433", "Black Watch", "Product Description",
                        "black-watch.jpg", 7.2, "Accessories", 61L, "INSTOCK", 4));

        Mockito.when(productService.findAllProducts()).thenReturn(mockProducts);
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockProducts);
    }

    @Test
    public void testGetAllProducts_empty() {
        Mockito.when(productService.findAllProducts()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void testGetAllProducts_exception() {
        Mockito.when(productService.findAllProducts()).thenThrow(new ProductServiceException("Error fetching products"));
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testGetProduct_success() {
        Product mockProduct = new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                "bamboo-watch.jpg", 65.0, "Accessories", 24L, "INSTOCK", 5);
        Mockito.when(productService.getProduct(1000L)).thenReturn(Optional.of(mockProduct));

        ResponseEntity<Product> response = productController.getProduct(1000L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockProduct);
    }

    @Test
    public void testGetProduct_notFound() {
        Mockito.when(productService.getProduct(1000L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProduct(1000L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testGetProduct_exception() {
        Mockito.when(productService.getProduct(1000L)).thenThrow(new ProductServiceException("Error fetching product"));
        ResponseEntity<Product> response = productController.getProduct(1000L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }


    @Test
    public void testCreateProduct_success() {
        Product mockProduct = new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                "bamboo-watch.jpg", 65.0, "Accessories", 24L, "INSTOCK", 5);

        Mockito.when(productService.saveProduct(mockProduct)).thenReturn(mockProduct);
        ResponseEntity<Product> response = productController.createProduct(mockProduct);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(mockProduct);
    }

    @Test
    public void testCreateProduct_exception() {

        Mockito.when(productService.saveProduct(Mockito.any(Product.class)))
                .thenThrow(new ProductServiceException("Error creating product"));
        ResponseEntity<Product> response = productController.createProduct(new Product());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testUpdateProduct_success() {
        Product mockProduct = new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                "bamboo-watch.jpg", 65.0, "Accessories", 24L, "INSTOCK", 5);
        Mockito.when(productService.getProduct(1000L)).thenReturn(Optional.of(mockProduct));

        ResponseEntity<Product> response = productController.updateProduct(mockProduct, 1000L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockProduct);
    }

    @Test
    public void testUpdateProduct_notFound() {
        Mockito.when(productService.getProduct(1000L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(new Product(), 1000L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testUpdateProduct_exception() {
        Mockito.when(productService.getProduct(1000L)).thenReturn(Optional.of(new Product()));
        Mockito.when(productService.updateProduct(Mockito.any(Product.class)))
                .thenThrow(new ProductServiceException("Error updating product"));

        ResponseEntity<Product> response = productController.updateProduct(Mockito.any(Product.class), 1000L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testDeleteProduct_success() {
        Mockito.when(productService.deleteProduct(1000L)).thenReturn(Optional.of(true));

        ResponseEntity<Void> response = productController.deleteProduct(1000L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void testDeleteProduct_notFound() {
        ResponseEntity<Void> response = productController.deleteProduct(1111L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteProduct_exception() {
        Mockito.when(productService.deleteProduct(1000L)).thenThrow(new ProductServiceException("Error deleting product"));

        ResponseEntity<Void> response = productController.deleteProduct(1000L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
