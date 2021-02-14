package com.sl.ms.inventorymanagement;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repository.InventoryRepository;
import com.sl.ms.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RepositoryTests {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryRepository inventoryRepository;

    @BeforeEach
    public void save() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);


        File file = Paths.get("src", "test", "resources", "products_request.json").toFile();
        Product[] products = mapper.readValue(file, Product[].class);

        ProductRepository productRepository1 = productRepository;
        for (Product product : products) {
            productRepository1.save(product);
        }
        Set<Product> productSet = new HashSet<Product>(Arrays.asList(products));
        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setProducts(productSet);
        inventory.setRequest("");
        inventoryRepository.save(inventory);
    }

    @AfterEach
    public void delete() {
        productRepository.deleteAll();
        inventoryRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "Test - Check Repository")
    public void findById() {
        Assertions.assertNotNull(productRepository.findAll());
        Assertions.assertNotNull(productRepository.findById(1));
        Assertions.assertNotNull(inventoryRepository.findAll());
        Assertions.assertNotNull(inventoryRepository.findById(1));
    }
}
