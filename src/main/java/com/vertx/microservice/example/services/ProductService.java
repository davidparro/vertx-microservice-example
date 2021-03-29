package com.vertx.microservice.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.vertx.microservice.example.models.Product;

public class ProductService {
    private final List<Product> products = new ArrayList<Product>();

    public ProductService() {
        products.add(new Product("1", "raidentrance", "superSecret"));
        products.add(new Product("2", "root", "superExtraSecret"));
        products.add(new Product("3", "dummy", "notSecret"));
    }

    public List findAll() {
        return products;
    }

    public Optional findByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public void create(Product product) {
        products.add(product);
    }
}
