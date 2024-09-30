package de.ait.platform.category.service;

import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

@Mock
private CategoryRepository categoryRepository;

@InjectMocks
private CategoryServiceImp categoryServiceImp;

    @Mock
    private ModelMapper categoryMapper;

    @Test
   public void CategoryService_Save_ReturnSavedCategory() {
        Category category = Category
                .builder()
                .name("Category")
                .build();
        CategoryRequest categoryRequest = CategoryRequest
                .builder()
                .name("Category")
                .build();
        when(categoryMapper.map(any(CategoryRequest.class), Mockito.eq(Category.class)))
                .thenReturn(category);
        when(categoryMapper.map(any(Category.class), Mockito.eq(CategoryResponse.class)))
                .thenReturn(new CategoryResponse("Category"));

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        CategoryResponse saved = categoryServiceImp.save(categoryRequest);
        Assertions.assertThat(saved).isNotNull();

    }

    @Test
    void CategoryService_FindAll_ReturnListCategory() {
        Category category = Category
                .builder()
                .name("Category").build();
        Category finance = Category.builder()
                .name("Finance")
                .build();

        categoryRepository.save(finance);
        categoryRepository.save(category);
        CategoryResponse categoryResponse = Mockito.mock(CategoryResponse.class);
        List<Category> all = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = all.stream()
                .map(c -> categoryMapper.map(c, CategoryResponse.class))
                .toList();
        Assertions.assertThat(categoryResponses).isNotNull();
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
    void addArticleToCategory() {


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