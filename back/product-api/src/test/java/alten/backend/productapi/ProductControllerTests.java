package alten.backend.productapi;

import alten.backend.productapi.controller.ProductController;
import alten.backend.productapi.entity.Product;
import alten.backend.productapi.exception.ProductControllerException;
import alten.backend.productapi.exception.ProductServiceException;
import alten.backend.productapi.service.ProductService;
import alten.backend.productapi.utils.InventoryStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
                        "bamboo-watch.jpg", 65.0, "Accessories", 24L, InventoryStatus.INSTOCK, 5),
                new Product(1001L, "nvklal433", "Black Watch", "Product Description",
                        "black-watch.jpg", 7.2, "Accessories", 61L, InventoryStatus.INSTOCK, 4));

        when(productService.findAllProducts()).thenReturn(mockProducts);
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockProducts);
    }

    @Test
    public void testGetAllProducts_empty() {
        when(productService.findAllProducts()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void testGetAllProducts_exception() {
        when(productService.findAllProducts()).thenThrow(new ProductServiceException("Error fetching products"));
        try {
            ResponseEntity<List<Product>> response = productController.getAllProducts();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNull();
        } catch (ProductControllerException productControllerException) {
            assertEquals("Error fetching products", productControllerException.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, productControllerException.getStatusCode());
        }

    }


    @Test
    public void testGetProduct_success() {
        Product mockProduct = new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                "bamboo-watch.jpg", 65.0, "Accessories", 24L, InventoryStatus.INSTOCK, 5);
        when(productService.getProduct(1000L)).thenReturn(Optional.of(mockProduct));

        ResponseEntity<Product> response = productController.getProduct(1000L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockProduct);
    }

    @Test
    public void testGetProduct_notFound() {
        when(productService.getProduct(10L)).thenReturn(Optional.empty());
        ProductControllerException exception = assertThrows(ProductControllerException.class, () -> {
                                                            productController.getProduct(10L);
        });
        assertEquals("Error while fetching product", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }


    @Test
    public void testGetProduct_exception() {
        when(productService.getProduct(10L)).thenThrow(new ProductServiceException("Error while fetching product"));

        try {
            ResponseEntity<Product> response = productController.getProduct(10L);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNull();
        } catch (ProductControllerException productControllerException) {
            assertEquals("Error while fetching product", productControllerException.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, productControllerException.getStatusCode());
        }

    }

    @Test
    public void testCreateProduct_success() {
        Product mockProduct = new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                "bamboo-watch.jpg", 65.0, "Accessories", 24L, InventoryStatus.INSTOCK, 5);

        when(productService.saveProduct(mockProduct)).thenReturn(mockProduct);
        ResponseEntity<Product> response = productController.createProduct(mockProduct);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(mockProduct);
    }

    @Test
    public void testCreateProduct_exception() {

        when(productService.saveProduct(any(Product.class)))
                .thenThrow(new ProductServiceException("Error while creating product"));

        try {
            ResponseEntity<Product> response = productController.createProduct(new Product());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNull();
        } catch (ProductControllerException productControllerException) {
            assertEquals("Error while creating product", productControllerException.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, productControllerException.getStatusCode());
        }
    }

    @Test
    public void testUpdateProduct_success() {
        Product mockProduct = new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description",
                "bamboo-watch.jpg", 65.0, "Accessories", 24L, InventoryStatus.INSTOCK, 5);
        when(productService.getProduct(1000L)).thenReturn(Optional.of(mockProduct));

        ResponseEntity<Product> response = productController.updateProduct(mockProduct, 1000L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void testUpdateProduct_ProductNotFound() {
        Product product = new Product();
        Long id = 1L;
        when(productService.getProduct(id)).thenReturn(Optional.empty());
        ProductControllerException exception = assertThrows(ProductControllerException.class, () -> {
            productController.updateProduct(product, id);
        });
        assertEquals("Error while updating product", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }



    @Test
     void testUpdateProduct_exception() {
        when(productService.getProduct(111L)).thenReturn(Optional.of(new Product()));
        when(productService.updateProduct(any(Product.class)))
                .thenThrow(new ProductServiceException("Error updating product"));
        try {
            ResponseEntity<Product> response = productController.updateProduct(any(Product.class), 1000L);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNull();
        } catch (ProductControllerException productControllerException) {
            assertEquals("Error while updating product", productControllerException.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, productControllerException.getStatusCode());
        }
    }

    @Test
    public void testDeleteProduct_success() {
        when(productService.deleteProduct(1000L)).thenReturn(Optional.of(true));

        ResponseEntity<Void> response = productController.deleteProduct(1000L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testDeleteProduct_notFound() {
        when(productService.getProduct(1L)).thenReturn(Optional.empty());
        ProductControllerException exception = assertThrows(ProductControllerException.class, () ->
                                                productController.deleteProduct(1L));

        assertEquals("Error while deleting product", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void testDeleteProduct_exception() {
        when(productService.deleteProduct(10L)).thenThrow(new ProductServiceException("Error while deleting product"));

        try {
            ResponseEntity<Void> response = productController.deleteProduct(10L);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ProductControllerException productControllerException) {
            assertEquals("Error while deleting product", productControllerException.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, productControllerException.getStatusCode());
        }
    }

}
