package de.ait.platform.article.service;

import de.ait.platform.article.dto.RequestArticle;
import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.category.service.CategoryService;
import de.ait.platform.category.service.CategoryServiceImp;
import de.ait.platform.security.service.AuthService;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ActiveProfiles("local")
@SpringBootTest
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ModelMapper articleMapper;

    @Mock
    private AuthService service;
    @Mock
    private CategoryServiceImp categoryService;

    private ArticleServiceImp articleService;
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
    }

    @Test
    public void ArticleService_Save_ReturnArticle() {

        //Arrange
        RequestArticle dto = RequestArticle
                .builder()
                .title("My Article")
                .content("My Content")
                .build();
        Article article = Article
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        ResponseArticle responseArticle = ResponseArticle
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();


        when(articleRepository.save(any(Article.class))).thenReturn(article);
        ResponseArticle saved = articleService.createArticle(dto);
        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void ArticleService_FindAll_ReturnListArticle() {
        //Arrange
        Article article = Article
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();

        Article article2 = Article
                .builder()
                .title("My Article2")
                .content("My Content2")
                .photo("")
                .build();

        articleRepository.save(article);
        articleRepository.save(article2);
        //Act
        List<Article> all = articleRepository.findAll();
        List<ResponseArticle> articleList = all.stream()
                .map(a-> articleMapper.map(a, ResponseArticle.class))
                        .toList();
        //Assert
        Assertions.assertThat(articleList).isNotNull();

    }

    @Test
    public void ArticleService_FindById_ReturnArticle() {
        //Arrange
        Article article = Article
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        //Act
        ResponseArticle articleResponse = articleService.findById(1L);

        //Assert
        assertNotNull(articleResponse);
    }

//    @Test
//    public void ArticleService_FindByTitle_ReturnArticle() {
//        Article article = Article
//                .builder()
//                .title("My Article")
//                .content("My Content")
//                .photo("")
//                .build();
//        articleRepository.save(article);
//        when(articleRepository.findByTitle(article.getTitle())).thenReturn(article);
//        //Act
//        List<ResponseArticle> articleResponse = articleService.findByTitle(article.getTitle());
//
//        //Assert
//        Assertions.assertThat(articleResponse).isNotNull();
//        Article article = Article
//                .builder()
//                .title("My Article")
//                .content("My Content")
//                .photo("")
//                .build();
//        ResponseArticle responseArticle = ResponseArticle
//                .builder()
//                .title("My Article")
//                .content("My Content")
//                .photo("")
//                .build();
//        when(articleRepository.findByTitle(any())).thenReturn(article);
//        when(articleMapper.map(any(Article.class), any())).thenReturn(responseArticle);
//        //Act
//        List<ResponseArticle> articleResponse = articleService.findByTitle("My Article");
//
//        //Assert
//        Assertions.assertThat(articleResponse).isNotNull();
//        Assertions.assertThat(articleResponse.size()).isEqualTo(1);
//    }
@Test
public void ArticleService_FindByTitle_ReturnArticle() {
    Article article = Article
            .builder()
            .title("My Article")
            .content("My Content")
            .photo("")
            .build();
    when(articleRepository.findAll()).thenReturn(List.of(article));
    when(articleMapper.map(any(Article.class), Mockito.eq(ResponseArticle.class)))
            .thenReturn(ResponseArticle.builder().title("My Article").content("My Content").photo("").build());
    //Act
    List<ResponseArticle> articleResponse = articleService.findByTitle(article.getTitle());

    //Assert
    Assertions.assertThat(articleResponse).isNotNull();
    Assertions.assertThat(articleResponse.size()).isEqualTo(1);
}
    @Test
    public void ArticleService_Update_ReturnArticle() {
        //Arrange
        RequestArticle dto = RequestArticle
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();

        Article article = Article
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        Article updatedArticle = Article
                .builder()
                .title("My Article Updated")
                .content("My Content Updated")
                .photo("")
                .build();
        ResponseArticle responseArticle = ResponseArticle
                .builder()
                .title("My Article Updated")
                .content("My Content Updated")
                .photo("")
                .build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));
        when(articleRepository.save(any(Article.class))).thenReturn(updatedArticle);
        when(articleMapper.map(any(Article.class), any())).thenReturn(responseArticle);
        //Act
        ResponseArticle saved = articleService.updateArticle(1L, dto);
        //Assert
        assertNotNull(saved);
        assertEquals("My Article Updated", saved.getTitle());
    }

    @Test
    public void ArticleService_DeleteById_ReturnArticle() {
        // Arrange
        Article article = Article
                .builder()
                .id(1L)
                .title("My Article")
                .content("My Content")
                .photo("")
                .comments(new HashSet<>())
                .build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));

        // Act
        articleService.deleteArticle(1L);
        when(articleRepository.findById(any())).thenReturn(Optional.empty());

        // Assert
        Assertions.assertThat(articleRepository.findById(1L)).isEmpty();
    }
}