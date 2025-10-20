package up.devops.fgd.stockManager.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import up.devops.fgd.stockManager.model.Product;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Configura una base de datos H2 en memoria automáticamente
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repo;

    private Product product;

    @BeforeEach
    void setup() {
        repo.deleteAll(); // limpia la DB antes de cada test
        product = new Product(null, "Remera", 10, 1999.0);
    }

    @Test
    void testSaveAndFindById() {
        Product saved = repo.save(product);
        Optional<Product> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Remera", found.get().getName());
        System.out.println("testSaveAndFindById passed");
    }

    @Test
    void testFindAll() {
        repo.save(new Product(null, "Pantalón", 5, 2999.0));
        repo.save(new Product(null, "Zapato", 3, 3999.0));

        List<Product> products = repo.findAll();
        assertEquals(2, products.size());
        System.out.println("testFindAll passed, size=" + products.size());
    }

    @Test
    void testDelete() {
        Product saved = repo.save(product);
        repo.deleteById(saved.getId());
        assertFalse(repo.findById(saved.getId()).isPresent());
        System.out.println("testDelete passed");
    }

    @Test
    void testUpdate() {
        Product saved = repo.save(product);
        saved.setQuantity(20);
        saved.setPrice(2499.0);
        Product updated = repo.save(saved);

        assertEquals(20, updated.getQuantity());
        assertEquals(2499.0, updated.getPrice());
        System.out.println("testUpdate passed");
    }
}
