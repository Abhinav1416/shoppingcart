package com.firstspringbootproject.shoppingcart.service.product;

import com.firstspringbootproject.shoppingcart.model.Product;
import com.firstspringbootproject.shoppingcart.request.AddProductRequest;
import com.firstspringbootproject.shoppingcart.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    // 11 methods

    Product addProduct(AddProductRequest product);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    void deleteProductById(Long id);

    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

}
