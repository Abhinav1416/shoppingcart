package com.firstspringbootproject.shoppingcart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This will generate the id automatically when a new row is inserted
    private Long id; // Primary Key for this table

    private String fileName;
    private String fileType;
    private String downloadUrl;

    @Lob
    private Blob image;

    @ManyToOne
    @JoinColumn(name="product_id")  // product_id is the foreign key for Image table
    private Product product;
}
