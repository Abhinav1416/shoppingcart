package com.firstspringbootproject.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter // Here instead of @Data we are using @Getter and @Setter because
@Setter // @Date automates so many things that are risky for database package(model)
@NoArgsConstructor
@Entity

public class Product {
    private String brand;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int inventory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    // mappedBy = "product" is telling that the variable product in Image is maintaining the relationship between these 2 tables
    // cascade = CascadeType.ALL tells whatever the operations done on the product image is done to the associated images in the database
    // orphanRemoval=true means if for a product if an image is deleted then it is automatically deleted from the database of images
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand=brand;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }
}
