package com.firstspringbootproject.shoppingcart.service.category;

import com.firstspringbootproject.shoppingcart.exceptions.AlreadyExistsException;
import com.firstspringbootproject.shoppingcart.exceptions.ResourceNotFoundException;
import com.firstspringbootproject.shoppingcart.model.Category;
import com.firstspringbootproject.shoppingcart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return this.categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !this.categoryRepository.existsByName(c.getName()))
                .map(this.categoryRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(
                        category.getName() + " already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return this.categoryRepository.save(oldCategory);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        this.categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,() -> {
                    throw new ResourceNotFoundException("Category Not Found!");
                });
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }
}
