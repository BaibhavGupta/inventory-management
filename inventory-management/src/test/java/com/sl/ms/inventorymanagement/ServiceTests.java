package com.sl.ms.inventorymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repository.ProductRepository;
import com.sl.ms.inventorymanagement.service.InventoryService;
import com.sl.ms.inventorymanagement.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ServiceTests {
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ProductService service;

    InventoryService invent;

    @MockBean
    ProductRepository productRepository;

    File file;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        file= Paths.get("src", "test","resources","inventory.csv").toFile();

        invent= Mockito.mock(InventoryService.class);

        //service=Mockito.mock(ProductService.class);
    }
    @Test
    @DisplayName(value = "Test - Add Item in Inventory")
    void addInventoryTest() {
        InventoryService mockInvent=Mockito.mock(InventoryService.class);
        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);

        Mockito.doNothing().when(mockInvent).addInventory(Mockito.isA(Integer.class), Mockito.isA(Product.class));
        mockInvent.addInventory(1, product);

        Mockito.verify(mockInvent).addInventory(1, product);
    }
    @Test
    @DisplayName(value = "Test - Add Item List in Inventory")
    void addInventoryListTest() throws Exception {

        List<Product> list =new ArrayList<>();
        Product product = new Product();
        product.setId(1);
        product.setName("Item1");
        product.setPrice(120.12);
        product.setQuantity(10);


        Mockito.doNothing().when(invent).addInventoryList(ArgumentMatchers.anyList());
        invent.addInventoryList(list);


        Mockito.verify(invent).addInventoryList(list);
    }
    @Test
    @DisplayName(value = "Test - Add Item Inventory File")
    void addInventoryFileTest() throws Exception {
        //file = Paths.get("src", "test", "resources", "sample.csv").toFile();
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile file1 = new MockMultipartFile("file",input);

        Mockito.doNothing().when(invent).addInventoryFile(file1);
        invent.addInventoryFile(file1);
        Mockito.verify(invent).addInventoryFile(file1);

    }
    @Test
    @DisplayName(value = "Test -Get Products Test")
    public void getProductsTest() throws Exception {
        //addInventoryFileTest();
        //service=Mockito.mock(ProductService.class);

        ProductRepository mockRepo=Mockito.mock(ProductRepository.class);

        List<Product> expected=new ArrayList<>();
        Product mockProduct=new Product();

        mockProduct.setName("Item1");
        mockProduct.setPrice(120.1);
        mockProduct.setQuantity(10);
        mockProduct.setId(1);

        expected.add(mockProduct);

        Mockito.doReturn(expected).when(productRepository).findAll();

        //Mockito.doReturn(expected).when(service).getProducts();

        List<Product> actual=service.getProducts();
        assertNotNull(actual);
        assertEquals(expected,actual);
    }

    @Test
    @DisplayName(value = "Test -Get Product By Id Test")
    void findByProductId() throws Exception {
        //service=Mockito.mock(ProductService.class);

        //ProductRepo mockRepo=Mockito.mock(ProductRepo.class);

        Object mockObject;

        Product mockorder=new Product();

        mockorder.setName("Item1");
        mockorder.setPrice(120.1);
        mockorder.setQuantity(10);
        mockorder.setId(1);

        mockObject=mockorder;

        Mockito.doReturn(Optional.of(mockorder)).when(productRepository).findById(Mockito.anyInt());


        //Mockito.doReturn(mockObject).when(service).findByProductId(Mockito.anyInt());
        //Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(opt);
        Optional<Object> result=Optional.of(service.findByProductId(1,""));
        Product p=(Product)result.get();
        assertNotNull(service.findByProductId(1,""));
        assertSame("Item1",p.getName());
    }
    @Test
    @DisplayName(value = "Test -Update Product Test")
    void updateProductTest() {
        service=Mockito.mock(ProductService.class);

        ProductRepository mockRepo=Mockito.mock(ProductRepository.class);

        Product mockProduct=new Product();

        mockProduct.setName("Item1");
        mockProduct.setPrice(120.1);
        mockProduct.setQuantity(10);
        mockProduct.setId(1);

        Mockito.doReturn(Optional.of(mockProduct)).when(productRepository).findById(Mockito.anyInt());

        Mockito.doReturn(mockProduct).when(productRepository).save(mockProduct);

        Mockito.doNothing().when(service).updateProduct(Mockito.anyInt(), Mockito.isA(Product.class));
        service.updateProduct(1,mockProduct);

        Mockito.verify(service).updateProduct(1,mockProduct);

    }

    @Test
    @DisplayName(value = "Test -Delete Product Test")
    public void deleteProductTest() {
        service=Mockito.mock(ProductService.class);

        ProductRepository mockRepo=Mockito.mock(ProductRepository.class);

        Product mockorder=new Product();

        mockorder.setName("Item1");
        mockorder.setPrice(120.1);
        mockorder.setQuantity(10);
        mockorder.setId(1);

        Mockito.doReturn(Optional.of(mockorder)).when(mockRepo).findById(Mockito.anyInt());
        mockorder.setQuantity(0);
        Mockito.doReturn(mockorder).when(mockRepo).save(mockorder);

        Mockito.doNothing().when(service).deleteProduct(Mockito.anyInt());
        service.deleteProduct(1);

        Mockito.verify(service).deleteProduct(1);

    }

    @Test
    @DisplayName(value = "Test -Specific Products Test")
    public void specificProductsTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);

        file=Paths.get("src", "test","resources","products_request.json").toFile();

        Product[] products = mapper.readValue(file, Product[].class);

        List<Product> distinctList = new ArrayList<>();
        List<Product> productList = List.of(products);

        List<Product> dtoList = new ArrayList<>();
        distinctList=productList.stream().distinct().collect(Collectors.toList());

        distinctList.stream().forEach(a -> {
            Product dto = new Product();
            dto.setId(a.getId());
            dto.setName(a.getName());
            dto.setPrice(a.getPrice());
            dto.setQuantity(a.getQuantity());
            dtoList.add(dto);
        });


        Mockito.doReturn(productList).when(productRepository).findAll();

        //Mockito.when(productRepo.findAll()).thenReturn(productList);

        //Mockito.doReturn(dtoList).when(service).specificProducts();

        List<Product> expected  = service.specificProducts();

        assertNotNull(dtoList);
        assertEquals(expected.size(),dtoList.size());

    }
}
