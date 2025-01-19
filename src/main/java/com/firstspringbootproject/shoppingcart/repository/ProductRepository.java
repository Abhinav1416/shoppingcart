package com.firstspringbootproject.shoppingcart.repository;

import com.firstspringbootproject.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // This all are custom finder methods

    List<Product> findByCategoryName(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    List<Product> findByName(String name);
    List<Product> findByBrandAndName(String brand, String name);
    Long countByBrandAndName(String brand, String name);

}
