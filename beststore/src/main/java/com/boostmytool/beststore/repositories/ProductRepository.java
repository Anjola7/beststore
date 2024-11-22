// ProductRepository.java
package com.boostmytool.beststore.repositories;

import com.boostmytool.beststore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
