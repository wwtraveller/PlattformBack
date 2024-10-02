package de.ait.platform.category.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
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
        Category category = Category.builder().name("Music").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.doNothing().when(categoryRepository).deleteById(1L);
        // Act
        categoryServiceImp.delete(1L);
        // Assert
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(1L);
    }


    @Test
    void addArticleToCategory() {
//        // Arrange
//        Category category = Category
//                .builder()
//                .name("Technology")
//                .articles(new ArrayList<>()) // Ініціалізуємо порожній список статей
//                .build();
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//
//        Article article = Article
//                .builder()
//                .title("Technology Article")
//                .build();
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//
//        // Act
//        List<Article> articles = categoryServiceImp.addArticleToCategory(1L, 1L);
//
//        // Діагностичний println для перевірки, чи стаття додається до списку
//        System.out.println("Articles in category: " + articles);
//
//        // Assert
//        Assertions.assertThat(articles).isNotNull(); // Перевіряємо, що список не є null
//        Assertions.assertThat(articles).hasSize(1); // Перевіряємо, що у списку 1 елемент
//        Assertions.assertThat(articles.get(0)).isNotNull(); // Перевіряємо, що перший елемент не null
//        Assertions.assertThat(articles.get(0).getId()).isEqualTo(article.getId()); // Перевіряємо, що додана стаття має правильний ID
//
//        // Перевіряємо, що стаття дійсно додана до категорії
//        Assertions.assertThat(category.getArticles()).contains(article);
//
//        // Перевіряємо, що категорія збережена після додавання статті
//        Mockito.verify(categoryRepository, Mockito.times(1)).save(category);
    }

    @Test
    void findByName() {
    }

    @Test
    void CategoryService_Update_ReturnUpdatedCategory() {
//        Category category = Category
//                .builder()
//                .name("Category")
//                .build();
//        CategoryRequest categoryRequest = CategoryRequest
//                .builder()
//                .name("Category")
//                .build();
//        when(categoryMapper.map(any(CategoryRequest.class), Mockito.eq(Category.class)))
//                .thenReturn(category);
//        when(categoryMapper.map(any(Category.class), Mockito.eq(CategoryResponse.class)))
//                .thenReturn(new CategoryResponse("Category"));
//        when(categoryRepository.update(any(Category.class))).thenReturn(category);
//        CategoryResponse saved = categoryServiceImp.save(categoryRequest);
//        Assertions.assertThat(saved).isNotNull();
   }
}