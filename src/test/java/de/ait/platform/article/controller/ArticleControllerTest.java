package de.ait.platform.article.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.article.service.ArticleService;
import de.ait.platform.category.service.CategoryServiceImp;
import de.ait.platform.comments.service.CommentsServiceImpl;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.security.service.TokenFilter;
import de.ait.platform.security.service.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

////@WebMvcTest(controllers = ArticleController.class)
////@AutoConfigureMockMvc(addFilters = false)
////@ExtendWith(MockitoExtension.class)
////@SpringBootTest
////@ActiveProfiles("local")
@WebMvcTest(controllers = ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
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
    private CommentsServiceImpl commentsServiceImpl;

    @MockBean
    private TokenService tokenService;
    @MockBean
    private TokenFilter tokenFilter;

    @MockBean
    private RequestArticle requestArticle;
    @MockBean
    private ResponseArticle responseArticle;

    @Autowired
    private ObjectMapper objectMapper;


////
    @BeforeEach
    public void setup() {
        requestArticle = RequestArticle
                .builder()
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
        // Arrange
        Long id = 1L;
        when(articleService.findById(id)).thenReturn(responseArticle);

        // Act
        MvcResult result = mockMvc.perform(get("/api/articles/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        ResponseArticle resultArticle = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseArticle.class);
        Assertions.assertThat(responseArticle.equals(resultArticle));

    }

    @Test
    @WithMockUser(username = "admin", password = "qwerty001")
    void ArticleController_addArticle_ReturnArticle() throws Exception {
        RequestArticle newArticle = RequestArticle.builder()
                .title("Cool Article")
                .content("Cool Content")
                .build();
        // Arrange
        given(articleService.createArticle(newArticle)).willReturn(responseArticle);
        // Act
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestArticle)))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    void updateArticle() throws Exception {
        given(articleService.updateArticle(1L, requestArticle)).willReturn(responseArticle);
        mockMvc.perform(put("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestArticle)))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void deleteArticle() throws Exception {
        // Arrange
        given(articleService.deleteArticle(1L)).willReturn(responseArticle);
        mockMvc.perform(delete("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
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
        assertEquals(articles, resultArticles);
    }
}