package de.ait.platform.article.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.article.service.ArticleService;
import de.ait.platform.article.service.ArticleServiceImp;
import de.ait.platform.category.service.CategoryServiceImp;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.security.service.TokenFilter;
import de.ait.platform.security.service.TokenService;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(controllers = ArticleController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@ActiveProfiles("local")
@WebMvcTest(controllers = ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("local")
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private ModelMapper articleMapper;

    @MockBean
    private AuthService service;
    @MockBean
    private CategoryServiceImp categoryService;

    @MockBean
    private TokenService tokenService;
    @MockBean
    private TokenFilter tokenFilter;

    private RequestArticle requestArticle;
    private ResponseArticle responseArticle;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init(){
        articleRepository = Mockito.mock(ArticleRepository.class);
        articleMapper = Mockito.mock(ModelMapper.class);
        service = Mockito.mock(AuthService.class);
        categoryService = Mockito.mock(CategoryServiceImp.class);
        articleService = new ArticleServiceImp(articleRepository, categoryService, articleMapper, service);
        when(articleMapper.map(any(RequestArticle.class), any())).thenReturn(new Article());
        when(articleMapper.map(any(Article.class), any())).thenReturn(new ResponseArticle());
        when(service.getAuthenticatedUser()).thenReturn(new UserResponseDto());
        when(articleMapper.map(any(UserResponseDto.class), any())).thenReturn(new User());
        requestArticle = RequestArticle
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .comments(new HashSet<>())
                .build();

        Article article = Article
                .builder()
                .id(1L)
                .title("My Article")
                .content("My Content")
                .photo("")
                .comments(new HashSet<>())
                .build();

        responseArticle = ResponseArticle
                .builder()
                .id(1L)
                .title("My Article")
                .content("My Content")
                .photo("")
                .comments(new HashSet<>())
                .build();
    }

    @Test
    void findById() throws Exception {
        articleService.createArticle(requestArticle);
        // Arrange
        Long id = 1L;
        when(articleService.findById(id)).thenReturn(responseArticle);

        // Act
        MvcResult result = mockMvc.perform(get("/api/articles/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        ResponseArticle resultArticle = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseArticle.class);
        Assertions.assertEquals(responseArticle, resultArticle);
    }

//    @Test
//    void ArticleController_addArticle_ReturnArticle() throws Exception {
//        // Arrange
//        when(articleService.createArticle(ArgumentMatchers.any(RequestArticle.class))).thenReturn(responseArticle);
//
//        // Act
//        MvcResult result = mockMvc.perform(post("/api/articles")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestArticle)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Assert
//        ResponseArticle resultArticle = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseArticle.class);
//        Assertions.assertEquals(responseArticle, resultArticle);
//    }
@Test
@WithMockUser(username = "admin", roles = "ADMIN", value = "admin")
@ResponseStatus(HttpStatus.CREATED)
void testAddArticle_ReturnsArticle() throws Exception {
    // Arrange
    given(articleService.createArticle(any())).willReturn(responseArticle);

    // Act

    ResultActions result = mockMvc.perform(post("/api/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestArticle)))
            .andExpect(status().isCreated()) // Changed to isCreated() to match the expected HTTP status code for a POST request
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test title"))
            .andExpect(jsonPath("$.content").value("Test content"));

    // No need to assert the resultArticle here, as we've already verified the response in the previous step
}
    @ResponseStatus(HttpStatus.CREATED)
    @Test
    void updateArticle() throws Exception {
        // Arrange
        Long id = 1L;
        when(articleService.updateArticle(id, any(RequestArticle.class))).thenReturn(responseArticle);

        // Act
        MvcResult result = mockMvc.perform(put("/api/articles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestArticle)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        ResponseArticle resultArticle = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseArticle.class);
        Assertions.assertEquals(responseArticle, resultArticle);
    }

    @Test
    void deleteArticle() throws Exception {
        // Arrange
        articleService.createArticle(requestArticle);
        when(articleService.deleteArticle(1L)).thenReturn(responseArticle);

        // Act
        MvcResult result = mockMvc.perform(delete("/api/articles/1L"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        ResponseArticle resultArticle = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseArticle.class);
        Assertions.assertEquals(responseArticle, resultArticle);
    }

    @Test
    void getArticle() throws Exception {
        articleService.createArticle(requestArticle);

        // Arrange
        List<ResponseArticle> articles = articleService.findAll();
        when(articleService.findAll()).thenReturn(articles);

        // Act
        MvcResult result = mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        List<ResponseArticle> resultArticles = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ResponseArticle>>() {});
        Assertions.assertEquals(articles, resultArticles);
    }
}