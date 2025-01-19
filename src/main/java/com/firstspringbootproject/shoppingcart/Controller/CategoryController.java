package com.firstspringbootproject.shoppingcart.Controller;


import com.firstspringbootproject.shoppingcart.exceptions.AlreadyExistsException;
import com.firstspringbootproject.shoppingcart.exceptions.ResourceNotFoundException;
import com.firstspringbootproject.shoppingcart.model.Category;
import com.firstspringbootproject.shoppingcart.response.ApiResponse;
import com.firstspringbootproject.shoppingcart.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")

public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = this.categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        try {
            Category theCategory = this.categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Success", theCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body((new ApiResponse(e.getMessage(), null)));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category theCategory = this.categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body((new ApiResponse(e.getMessage(), null)));
        }
    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category theCategory = this.categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body((new ApiResponse(e.getMessage(), null)));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            this.categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body((new ApiResponse(e.getMessage(), null)));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category Category) {
        try {
            Category updatedCategory = this.categoryService.updateCategory(Category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body((new ApiResponse(e.getMessage(), null)));
        }
    }
}
