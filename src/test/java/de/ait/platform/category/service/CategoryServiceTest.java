package de.ait.platform.category.service;


import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import static org.junit.jupiter.api.Assertions.*;

import de.ait.platform.category.exceptions.CategoryNotFound;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
@SpringBootTest
class CategoryServiceTest {

@Mock
private CategoryRepository categoryRepository;

@Mock
private ModelMapper categoryMapper;

@Mock
private ArticleRepository articleRepository;

@InjectMocks
private CategoryServiceImp categoryServiceImp;

@BeforeEach
public void init(){
    categoryRepository = Mockito.mock(CategoryRepository.class);
    categoryMapper = Mockito.mock(ModelMapper.class);
    articleRepository = Mockito.mock(ArticleRepository.class);
    categoryServiceImp = new CategoryServiceImp(articleRepository,categoryRepository,categoryMapper);
}

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
    void delete() {
        // Arrange
        Category category = Category
                .builder()
                .name("Music")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.doNothing().when(categoryRepository).deleteById(1L);
        // Act
        categoryServiceImp.delete(1L);
        // Assert
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDelete_CategoryFound() {
        // Arrange
        Category category = Category.builder()
                .id(1L)
                .name("Music")
                .build();

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        // Act
        categoryServiceImp.delete(1L);
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        // Assert
        Assertions.assertThat(categoryRepository.findById(1L)).isEmpty();

    }


    @Test
    void CategoryService_addArticleToCategory_CategoryFound_ArticleFound() {
        // Arrange
        Long categoryId = 1L;
        Long articleId = 2L;
        Category category = Category.builder()
                .id(categoryId)
                .build();
        Article article = Article.builder()
                .id(articleId)
                .build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(categoryMapper.map(any(Optional.class), Mockito.eq(Article.class))).thenReturn(article);

        // Act and Assert
        assertThrows(NullPointerException.class, () -> categoryServiceImp.addArticleToCategory(articleId, categoryId));
    }
    @Test
    void CategoryService_addArticleToCategory_CategoryFound_ArticleNotFound() {
        // Arrange
        Long categoryId = 1L;
        Long articleId = 2L;
        Category category = Category.builder()
                .id(categoryId)
                .build();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CategoryNotFound.class, () -> categoryServiceImp.addArticleToCategory(articleId, categoryId));
    }

    @Test
    void CategoryService_addArticleToCategory_CategoryNotFound() {
        // Arrange
        Long categoryId = 1L;
        Long articleId = 2L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CategoryNotFound.class, () -> categoryServiceImp.addArticleToCategory(articleId, categoryId));
    }

    @Test
    void CategoryService_findByName_Category() {
        Category category = Category.builder()
                .id(1L)
                .name("Music")
                .build();
        when(categoryRepository.findByName(category.getName())).thenReturn(Collections.singletonList(category));
        when(categoryMapper.map(any(Category.class), Mockito.eq(CategoryResponse.class)))
                .thenReturn(CategoryResponse.builder()
                        .id(1L)
                        .name("Music")
                        .build());
        //Act
        List<CategoryResponse> categoryResponse = categoryServiceImp.findByName(category.getName());

        //Assert
        Assertions.assertThat(categoryResponse).isNotNull();
        Assertions.assertThat(categoryResponse.size()).isEqualTo(1);
    }

    @Test
    void CategoryService_Update_ReturnUpdatedCategory() {
        CategoryRequest categoryRequest = CategoryRequest
                .builder()
                .name("New Category")
                .build();
        Category category = Category
                .builder()
                .id(1L)
                .name("Category")
                .build();

        Category updatedCategory = Category
                .builder()
                .id(1L)
                .name("New Category")
                .build();

        when(categoryMapper.map(any(Category.class), Mockito.eq(CategoryResponse.class)))
                .thenAnswer(invocation -> {
                    Category categoryArg = invocation.getArgument(0);
                    return CategoryResponse.builder()
                            .id(categoryArg.getId())
                            .name(categoryArg.getName())
                            .build();
                });
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryRepository.findByName(any())).thenReturn(List.of());

        CategoryResponse saved = categoryServiceImp.update(1L, categoryRequest);
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo("New Category");
    }
}