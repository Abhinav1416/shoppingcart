package com.firstspringbootproject.shoppingcart.Controller;


import com.firstspringbootproject.shoppingcart.exceptions.ResourceNotFoundException;
import com.firstspringbootproject.shoppingcart.model.Product;
import com.firstspringbootproject.shoppingcart.request.AddProductRequest;
import com.firstspringbootproject.shoppingcart.request.ProductUpdateRequest;
import com.firstspringbootproject.shoppingcart.response.ApiResponse;
import com.firstspringbootproject.shoppingcart.service.product.IProductService;
import com.firstspringbootproject.shoppingcart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")

public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = this.productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("success", products));
    }

    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = this.productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("success", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = this.productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add product success!", theProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
        try {
            Product theProduct = this.productService.updateProduct(request, productId);
            return ResponseEntity.ok(new ApiResponse("Update product success!", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            this.productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = this.productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = this.productService.getProductsByCategoryAndBrand(category, brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", null));
        }
    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = this.productService.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", null));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = this.productService.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = this.productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            // If you use var then you don't need to specify the data type of the variable
            var productCount = this.productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}