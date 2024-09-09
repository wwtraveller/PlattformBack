package de.ait.platform.category.service;

import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.exceptions.CategoryNotFound;
import de.ait.platform.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryServiceImp implements CategoryService {
    private final ModelMapper mapper;
    private final CategoryRepository repository;
    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = repository.findAll();
        return categories.stream()
                .map(c-> mapper.map(c, CategoryResponse.class))
                .toList();
    }

    @Override
    public CategoryResponse findById(Long id) {
        String message = "Couldn't find category with id:" + id;
        Category foundCategory = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFound(message));
        return mapper.map(foundCategory, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> findByName(String name) {
        String message = "Couldn't find category with name:" + name;
        List<Category> foundCategory = repository.findByName(name);
        if (foundCategory == null) {
            throw new CategoryNotFound(message);
        }
        else {
            return foundCategory.stream()
                    .map(c -> mapper.map(c, CategoryResponse.class))
                    .toList();
        }
    }



    @Override
    public CategoryResponse delete(Long id) {
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            repository.deleteById(id);
        }
        else {
            throw new CategoryNotFound("Error deleting category. Couldn't find category with id:" + id);
        }
        return mapper.map(category, CategoryResponse.class);
    }
    @Override
    public CategoryResponse save(CategoryRequest categoryDTO) {
        Category entity = mapper.map(categoryDTO, Category.class);
        Category newCategory = repository.save(entity);
        return mapper.map(newCategory, CategoryResponse.class);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryDTO) {
        Category entity = mapper.map(categoryDTO, Category.class);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.map(entity, CategoryResponse.class);
    }
}
