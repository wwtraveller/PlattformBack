package de.ait.platform.category.service;

import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll();
    CategoryResponse findById(int id);
    CategoryResponse findByName(String name);
    CategoryResponse save(CategoryRequest categoryDTO);
    CategoryResponse delete(Long id);
}
