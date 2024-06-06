package alten.backend.productapi.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter @Setter @Data
@AllArgsConstructor @NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String name;
    private String description;
    private String image;
    private Double price;
    private String category;
    private Long quantity;
    private String inventoryStatus;
    private Integer rating;
}
