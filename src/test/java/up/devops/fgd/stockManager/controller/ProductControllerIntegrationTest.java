package up.devops.fgd.stockManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import up.devops.fgd.stockManager.model.Product;
import up.devops.fgd.stockManager.repository.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/products/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("StockManager API is up and running!"));
        System.out.println("Health check passed");
    }

    @Test
    void testCRUDFlow() throws Exception {
        // CREATE
        Product product = new Product(null, "Chaqueta", 5, 7999.0);
        String json = objectMapper.writeValueAsString(product);

        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Product saved = objectMapper.readValue(response, Product.class);

        // READ
        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chaqueta"));

        // UPDATE
        saved.setQuantity(10);
        String updatedJson = objectMapper.writeValueAsString(saved);
        mockMvc.perform(put("/api/products/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(10));

        // DELETE
        mockMvc.perform(delete("/api/products/" + saved.getId()))
                .andExpect(status().isNoContent());

        // VERIFY DELETED
        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isNotFound());

        System.out.println("Full CRUD flow passed");
    }
}
