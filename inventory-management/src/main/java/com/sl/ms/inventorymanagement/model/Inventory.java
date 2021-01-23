package com.sl.ms.inventorymanagement.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table
public class Inventory {

    @Id
    @Column
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date date;

    @Column
    @Lob
    private String request;

    @OneToMany(targetEntity = Product.class,cascade = CascadeType.ALL)
    @JoinColumn(name="PRODUCT_ID")
    private Set<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
    public Set<Product> getProducts() {
        return products;
    }
    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
