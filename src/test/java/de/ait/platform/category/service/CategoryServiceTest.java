package de.ait.platform.category.service;

import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;


    @Mock
    private ModelMapper categoryMapper; // Initialize the mapper object

    @InjectMocks
    private CategoryServiceImp categoryServiceImp;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void save() {
        CategoryRequest categoryRequest = CategoryRequest
                .builder()
                .name("Category")
                .build();
        Category category = Category
                .builder()
                .name("Category")
                .build();
        System.out.println("Category: " + category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        CategoryResponse saved = categoryServiceImp.save(categoryRequest);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    void addArticleToCategory() {


    }

    @Test
    void findAll() {
    }

    @Test
    public void CategoryService_FindById_ReturnCategory() {
        //Arrange
        Category category = Category
                .builder()
                .name("My Category")
                .build();
        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .name("My Category")
                .build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(categoryMapper.map(any(Category.class), any())).thenReturn(categoryResponse);
        //Act
        CategoryResponse categoryResponse1 = categoryServiceImp.findById(1L);

        //Assert
        assertNotNull(categoryResponse1);
    }


    @Test
    void findByName() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}