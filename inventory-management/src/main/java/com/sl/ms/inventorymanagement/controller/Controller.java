package com.sl.ms.inventorymanagement.controller;

import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.service.InventoryService;
import com.sl.ms.inventorymanagement.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    @Autowired
    InventoryService inventoryService;
    @Autowired
    ProductService productService;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/products")
    public List<Product> getAllProduct(){
      return productService.getProducts();
    }

    @GetMapping("/products/{product_id}")
    public ResponseEntity<Object> getSpecificProducts(@PathVariable(name = "product_id") int productId,
                                                      //@RequestHeader HttpHeaders headers,
                                                      @RequestHeader(name = "UUID", required = false,defaultValue = "") String headerUUID) throws Exception {
    //Write logic to extract it from header and put a condition
        //if in header use it else create new and log headers
        //Add logging in order as well
        String uuid;
        if(headerUUID.isEmpty()){
            uuid = UUID.randomUUID().toString();
        }else{
            uuid = headerUUID;
        }
        LOG.info("UUID: "+ uuid +" Inside Inventory Controller Get - products/" + productId);
        return new ResponseEntity<>(productService.findByProductId(productId,uuid), HttpStatus.OK);
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
