package up.devops.fgd.stockManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import up.devops.fgd.stockManager.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {}

