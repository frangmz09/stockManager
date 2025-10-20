package up.devops.fgd.stockManager.service;

import org.springframework.stereotype.Service;
import up.devops.fgd.stockManager.model.Product;

import java.util.*;

/**
 * Servicio que maneja las operaciones CRUD de productos.
 * En esta versión los datos se almacenan en memoria (Map),
 * pero puede migrarse fácilmente a una base de datos con JPA.
 */
@Service
public class ProductService {

    // Simula una base de datos en memoria
    private final Map<Integer, Product> products = new HashMap<>();
    private int nextId = 1;

    /**
     * Devuelve todos los productos disponibles.
     */
    public List<Product> getAll() {
        return new ArrayList<>(products.values());
    }

    /**
     * Agrega un nuevo producto.
     */
    public Product addProduct(Product product) {
        product.setId(nextId++);
        products.put(product.getId(), product);
        return product;
    }

    /**
     * Busca un producto por su ID.
     * @throws NoSuchElementException si el producto no existe.
     */
    public Product getById(int id) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchElementException("Producto con ID " + id + " no encontrado.");
        }
        return product;
    }

    /**
     * Actualiza un producto existente.
     * @throws NoSuchElementException si el producto no existe.
     */
    public Product updateProduct(int id, Product updated) {
        if (!products.containsKey(id)) {
            throw new NoSuchElementException("No existe un producto con ID " + id);
        }
        updated.setId(id);
        products.put(id, updated);
        return updated;
    }

    /**
     * Elimina un producto por su ID.
     * @throws NoSuchElementException si el producto no existe.
     */
    public void deleteProduct(int id) {
        if (products.remove(id) == null) {
            throw new NoSuchElementException("No existe un producto con ID " + id);
        }
    }

    /**
     * Limpia el almacenamiento en memoria (útil para tests).
     */
    public void clear() {
        products.clear();
        nextId = 1;
    }
}
