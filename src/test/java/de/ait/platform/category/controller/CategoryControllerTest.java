package de.ait.platform.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.service.ArticleService;
import de.ait.platform.category.dto.CategoryRequest;
import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.service.CategoryService;
import de.ait.platform.comments.service.CommentsService;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.security.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension .class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryResponse categoryResponse;
    private CategoryRequest categoryRequest;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private CommentsService commentsService;

    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setup() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test Category");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Test Category");

        Article article = Article.builder().id(1L).title("Test Article").content("Test Content").build();
    }

    @Test
    void CategoryController_create_ReturnCreatedCategory() throws Exception {
        given(categoryService.save(any())).willReturn(categoryResponse);
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void CategoryController_getCategoryById_ReturnCreatedCategoryById() throws Exception {
            given(categoryService.findById(1L)).willReturn(categoryResponse);

            mockMvc.perform(get("/api/categories/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    void CategoryController_update_ReturnUpdeatedCategory() throws Exception {
        given(categoryService.update(any(Long.class), any(CategoryRequest.class))).willReturn(categoryResponse);
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }


    @Test
    void CategoryController_Delete_Return() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());
    }
    @WithMockUser
    @Test
    void CategoryController_addArticleToCategory() throws Exception {
        Long articleId = 1L;
        Long categoryId = 1L;
        given(categoryService.addArticleToCategory(articleId,categoryId)).willReturn(categoryResponse.getArticles());
        mockMvc.perform(put("/api/categories/" + categoryId + "/articles/" + articleId))
                .andExpect(status().isOk());
    }

    @Test
    void CategoryController_getCategory_ReturnAllCategories() throws Exception {
        List<CategoryResponse> all = categoryService.findAll();
        given(categoryService.findAll()).willReturn(all);
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }
}