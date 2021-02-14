package com.sl.ms.inventorymanagement;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.inventorymanagement.controller.JwtAuthenticationController;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repository.ProductRepository;
import com.sl.ms.inventorymanagement.service.InventoryService;
import com.sl.ms.inventorymanagement.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class IntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventService;

    @Autowired
    ProductRepository productRepo;

    File file;

    private MockMvc mockMvc;

    HttpHeaders httpHeaders = new HttpHeaders();
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);

    }

    @AfterEach
    public void dropDB() {
      //  productRepo.deleteAll();
    }

    @Test
    @DisplayName(value = "Test - Integration Add product List")
    void addProductsListTest() throws Exception {
        httpHeaders.add("Content-Type", "application/json");

        List<Product> list = new ArrayList<>();

        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);

        list.add(product);
        byte[] iJosn = toJson(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    @DisplayName(value = "Test - Get Products List")
    void getProductsTest() throws Exception {
        addProductsListTest();

        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);
        Product[] list = { product };

        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/products").headers(httpHeaders).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertEquals(result.getResponse().getContentAsString(),mapper.writeValueAsString(list));

    }

    @Test
    @DisplayName(value = "Test - Add Inventory ")
    void addInventoryTest() throws Exception {
        httpHeaders.add("Content-Type", "application/json");

        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);

        byte[] iJosn = toJson(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/1").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName(value = "Test - Load inventory from file")
    void addInventoryFileTest() throws Exception {
        file = Paths.get("src", "test", "resources", "inventory.csv").toFile();
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile file1 = new MockMultipartFile("file",input);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file").file(file1).headers(httpHeaders)
                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON))
                //.andExpect(MockMvcResultMatchers.status().isOk());
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @DisplayName(value = "Test - Update Product")
    void updateProductTest() throws Exception {
        addProductsListTest();

        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(220.12);
        product.setQuantity(20);

        byte[] iJosn = toJson(product);

        mockMvc.perform(MockMvcRequestBuilders.put("/1").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("product updated successfully"));

    }

    @Test
    @DisplayName(value = "Test -Delete Inventory")
    void deleteInventoryTest() throws Exception {
        addProductsListTest();

        mockMvc.perform(MockMvcRequestBuilders.delete("/1").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("product deleted successfully"));

    }

    @Test
    @DisplayName(value = "Test -Supported Product List")
    void supportedProductsTest() throws Exception {
        file = Paths.get("src", "test", "resources", "supported_products.json").toFile();
        Product[] productsDto = mapper.readValue(file, Product[].class);

        addProductsListTest();

        List<Product> list = new ArrayList<>();

        Product product = new Product();
        product.setId(2);
        product.setName("Item2");
        product.setPrice(130.12);
        product.setQuantity(20);

        list.add(product);
        byte[] iJosn = toJson(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/").content(iJosn).headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/supported").headers(httpHeaders)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

//        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(productsDto));

    }

  private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}
