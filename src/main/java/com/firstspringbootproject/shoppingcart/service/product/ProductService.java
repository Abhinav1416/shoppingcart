package com.firstspringbootproject.shoppingcart.service.product;

import com.firstspringbootproject.shoppingcart.exceptions.ResourceNotFoundException;
import com.firstspringbootproject.shoppingcart.model.Category;
import com.firstspringbootproject.shoppingcart.model.Product;
import com.firstspringbootproject.shoppingcart.repository.CategoryRepository;
import com.firstspringbootproject.shoppingcart.repository.ProductRepository;
import com.firstspringbootproject.shoppingcart.request.AddProductRequest;
import com.firstspringbootproject.shoppingcart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor /* This will automatically create a constructor with the variables that are declared as Final or with @NotNull
                          and this will inject the DI for productRepository */

public class ProductService implements IProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // Here there are some built-in methods in spring boot like findById() and findAll() and we have also created some custom methods

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(this.categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return this.categoryRepository.save(newCategory);
                });

        request.setCategory(category);   // I think this line is useless
        return this.productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return this.productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        this.productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ResourceNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return this.productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(this.productRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

        // The logic for the below 5 lines is correct because these are just Strings, int etc but for Category as it is an entity we can't use the same like code

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setBrand(request.getBrand());

        Category category = this.categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

//      existingProduct.setCategory(request.getCategory().getName()); as this Category is an entity we can't use this line
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return this.productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return this.productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return this.productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return this.productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return this.productRepository.countByBrandAndName(brand, name);
    }
}
