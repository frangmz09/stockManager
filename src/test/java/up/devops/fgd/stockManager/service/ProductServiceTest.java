package up.devops.fgd.stockManager.service;

import org.junit.jupiter.api.*;
import up.devops.fgd.stockManager.model.Product;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setup() {
        service = new ProductService();
    }

    @Test
    @Order(1)
    void testAddProduct() {
        Product product = new Product(null, "Camisa", 10, 1999.99);
        Product saved = service.addProduct(product);
        assertNotNull(saved.getId());
        assertEquals("Camisa", saved.getName());
        System.out.println("testAddProduct passed");
    }

    @Test
    @Order(2)
    void testGetAll() {
        service.addProduct(new Product(null, "Zapato", 5, 3999.0));
        List<Product> products = service.getAll();
        assertFalse(products.isEmpty());
        System.out.println("testGetAll passed, size=" + products.size());
    }

    @Test
    @Order(3)
    void testGetById() {
        Product product = service.addProduct(new Product(null, "Pantalón", 3, 2999.0));
        Product found = service.getById(product.getId());
        assertEquals("Pantalón", found.getName());
        System.out.println("testGetById passed");
    }

    @Test
    @Order(4)
    void testUpdateProduct() {
        Product product = service.addProduct(new Product(null, "Cinturón", 2, 499.0));
        product.setQuantity(10);
        Product updated = service.updateProduct(product.getId(), product);
        assertEquals(10, updated.getQuantity());
        System.out.println("testUpdateProduct passed");
    }

    @Test
    @Order(5)
    void testDeleteProduct() {
        Product product = service.addProduct(new Product(null, "Gorra", 1, 299.0));
        service.deleteProduct(product.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(product.getId()));
        System.out.println("testDeleteProduct passed");
    }

    @Test
    @Order(6)
    void testClear() {
        service.addProduct(new Product(null, "Bufanda", 5, 599.0));
        service.clear();
        assertTrue(service.getAll().isEmpty());
        System.out.println("testClear passed");
    }
}
