package com.sl.ms.inventorymanagement.service;

import com.sl.ms.inventorymanagement.exception.ProductNotFound;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Cacheable("all-products")
    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Object findByProductId(int productId, String uuid) throws Exception {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent() && product.get().getQuantity() > 0){
            Product lProduct = product.get();
            LOG.info("UUID: "+ uuid +" Product Found " + lProduct);
            return product.get();
        } else{
            LOG.error("UUID: "+ uuid +" Product Found " + productId);
            throw new ProductNotFound();
        }

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
            dto.setQuantity(a.getQuantity());
            dto.setPrice(a.getPrice());
            list1.add(dto);
        });

        return list1;
    }

}
