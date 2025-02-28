package com.firstspringbootproject.shoppingcart.request;

import com.firstspringbootproject.shoppingcart.model.Category;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductUpdateRequest {
    private Long id;
    private String brand;
    private String name;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;

}
