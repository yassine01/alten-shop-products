package alten.backend.productapi;

import alten.backend.productapi.entity.Product;
import alten.backend.productapi.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ProductApiApplication implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    private static final String JSON_FILE_PATH = "src/main/resources/products.json";


    public static void main(String[] args) {
        SpringApplication.run(ProductApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Directly read JSON file into a map with specific type
            Map<String, List<Product>> data = mapper.readValue(new File(JSON_FILE_PATH), new TypeReference<Map<String, List<Product>>>() {});

            // Get the list of products
            List<Product> products = data.get("data");

            // Save products to repository
            productRepository.saveAll(products);

            System.out.println("Products successfully saved to the repository.");
        } catch (Exception e) {
            System.err.println("An error occurred while loading products: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
