package com.sl.ms.inventorymanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public void addInventory(Integer productId, Product product) {
        String request="";
        ObjectMapper mapper=new ObjectMapper();
        Inventory inventory = new Inventory();
        Set<Product> products = new HashSet<Product>();
        products.clear();
        products.add(product);
        try {
            request=mapper.writeValueAsString(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inventory.setId(productId);
        inventory.setProducts(products);
        inventory.setRequest(request);
        inventoryRepository.save(inventory);
    }

    public void addInventoryList(List<Product> products) {
        ObjectMapper mapper=new ObjectMapper();

        List<Inventory> list = new ArrayList<>();
        products.forEach(a -> {
            Inventory inventory = new Inventory();
            Product product = new Product();
            String request="";

            inventory.setId(a.getId());
            product.setId(a.getId());
            product.setName(a.getName());
            product.setPrice(a.getPrice());
            product.setQuantity(a.getQuantity());
            Set<Product> set = new HashSet<Product>();
            set.add(product);
            inventory.setProducts(set);

            try {
                request=mapper.writeValueAsString(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
            inventory.setRequest(request);

            list.add(inventory);
        });

        inventoryRepository.saveAll(list);
    }
    public void addInventoryFile(MultipartFile file) {
        List<Inventory> list = new ArrayList<>();
        Resource resource = file.getResource();

        try {
            InputStream input = resource.getInputStream();

            BufferedReader readFile = new BufferedReader(new InputStreamReader(input));

            list = readFile.lines().skip(1).map(maptoclass).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        inventoryRepository.saveAll(list);
    }

    private Function<String, Inventory> maptoclass = (line) -> {
        ObjectMapper mapper=new ObjectMapper();
        Inventory inventory = new Inventory();
        Product cls = new Product();
        String request="";
        String[] items = line.split(",");

        Set<Product> products = new HashSet<Product>();
        products.clear();

        if (items[0] != null) {
            inventory.setId(Integer.parseInt(items[0]));
            cls.setId(Integer.parseInt(items[0]));
        }
        if (items[1] != null)
            cls.setName(items[1]);
        if (items[2] != null)
            cls.setPrice(Double.parseDouble(items[2]));
        if (items[3] != null)
            cls.setQuantity(Integer.parseInt(items[3]));

        products.add(cls);
        inventory.setProducts(products);
        try {
            request=mapper.writeValueAsString(cls);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        inventory.setRequest(request);
        return inventory;
    };


}
