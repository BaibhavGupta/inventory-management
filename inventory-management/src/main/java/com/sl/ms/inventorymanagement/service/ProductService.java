package com.sl.ms.inventorymanagement.service;

import com.sl.ms.inventorymanagement.exception.ProductNotFound;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Object findByProductId(int productId) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent())
            return product.get();
        else
            throw new ProductNotFound();
    }

    public void updateProduct(int productId, Product product) {
        Optional<Product> pro = productRepository.findById(productId);
        if (pro.isPresent()) {
            pro.get().setId(product.getId());
            pro.get().setName(product.getName());
            pro.get().setPrice(product.getPrice());
            pro.get().setQuantity(product.getQuantity());

            productRepository.save(pro.get());
        }
    }

    public void deleteProduct(int productId) {
        Optional<Product> pro = productRepository.findById(productId);
        if (pro.isPresent()) {
            pro.get().setQuantity(0);
            productRepository.save(pro.get());
        }
    }

    public List<Product> specificProducts() {
        List<Product> list = productRepository.findAll();
        List<Product> distinctList = productRepository.findAll();
        List<Product> list1 = new ArrayList<>();
        distinctList=list.stream().distinct().collect(Collectors.toList());

        distinctList.stream().forEach(a -> {
            Product dto = new Product();
            dto.setId(a.getId());
            dto.setName(a.getName());
            list1.add(dto);
        });

        return list1;
    }

}
