package alten.backend.productapi.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String DEFAULT_MSG = "Produit non trouvé avec l'id %d";
    public ProductNotFoundException(Long id) {
        super(String.format(DEFAULT_MSG, id));
    }
}
