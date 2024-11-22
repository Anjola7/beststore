package com.boostmytool.beststore.services;

import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Krijon një produkt të ri në bazën e të dhënave
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    // Përditëson informacionin e një produkti ekzistues
    public void updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImageFileName(product.getImageFileName());
        productRepository.save(existingProduct);
    }

    // Fshin një produkt nga baza e të dhënave
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Merr një produkt nga baza e të dhënave duke përdorur ID-në
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Merr të gjitha produktet nga baza e të dhënave
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

