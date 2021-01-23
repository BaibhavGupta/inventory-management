package com.sl.ms.inventorymanagement.controller;

import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.service.InventoryService;
import com.sl.ms.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    InventoryService inventoryService;
    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProduct(){
      return productService.getProducts();
    }

    @GetMapping("/products/{product_id}")
    public ResponseEntity<Object> getSpecificProducts(@PathVariable(name = "product_id") int productId) throws Exception {
        return new ResponseEntity<>(productService.findByProductId(productId), HttpStatus.OK);
    }

    @PostMapping(path = "/{product_id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addInventory(@PathVariable(name = "product_id") int productId,@RequestBody Product product) {
        inventoryService.addInventory(productId,product);
        return "Inventory Added Successfully";
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addProducts(@RequestBody List<Product> products) {
        inventoryService.addInventoryList(products);
        return "Inventory & Product Added Successfully";
    }

    @PostMapping(path = "/file")

    public String addInventoryFile(@RequestParam("file") MultipartFile file) {
        inventoryService.addInventoryFile(file);
        return "Inventory File & Product Added Successfully";
    }

    @PutMapping(path = "/{product_id}")
    public String updateProduct(@RequestBody Product product, @PathVariable(name = "product_id") int productId) {
        productService.updateProduct(productId, product);
        return "product updated successfully";
    }

    @DeleteMapping(path = "/{product_id}")
    public String deleteInventory(@PathVariable(name = "product_id") int productId) {
        productService.deleteProduct(productId);
        return "product deleted successfully";
    }

    @GetMapping(path = "/supported")
    public List<Product> supportedProducts() {
        return productService.specificProducts();
    }





}
