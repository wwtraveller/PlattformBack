package de.ait.platform.category.service;

import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;

import java.util.List;

public class CategoryServiceImp implements CategoryService {

    @Override
    public List<CategoryResponse> findAll() {

        return List.of();
    }

    @Override
    public CategoryResponse findById(int id) {
        return null;
    }

    @Override
    public CategoryResponse findByName(String name) {
        return null;
    }

    @Override
    public CategoryResponse save(CategoryRequest categoryDTO) {
        return null;
    }

    @Override
    public CategoryResponse delete(Long id) {
        return null;
    }
}
