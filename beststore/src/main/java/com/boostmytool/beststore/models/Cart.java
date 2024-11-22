package com.boostmytool.beststore.models;

import java.util.List;

public class Cart {
    private List<Product> products; // Lista e produkteve në kartë

    public Cart() {
    }

    public Cart(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return products.stream()
                       .mapToDouble(Product::getPrice)
                       .sum();
    }
}
