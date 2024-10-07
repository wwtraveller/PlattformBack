package de.ait.platform.article.repository;

import de.ait.platform.article.entity.Article;
import de.ait.platform.category.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Test
    public void ArticleRepository_Save_ReturnArticle() {
        //Arrange
        Article article = Article
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        //Act
        Article savedArticle = articleRepository.save(article);
        //Assert
        Assertions.assertThat(savedArticle).isNotNull();
        Assertions.assertThat(savedArticle.getId()).isGreaterThan(0);
    }

    @Test
    public void ArticleRepository_FindAll_ReturnListArticle() {
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
        //Act
        articleRepository.save(article);
        articleRepository.save(article2);
        List<Article> articleList = articleRepository.findAll();

        //Assert
        Assertions.assertThat(articleList).isNotNull();
        Assertions.assertThat(articleList.size()).isEqualTo(2);
    }

    @Test
    public void ArticleRepository_FindById_ReturnArticle() {
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
        //Act
        articleRepository.save(article);
        articleRepository.save(article2);
        Article articleList = articleRepository.findById(article2.getId()).get();

        //Assert
        Assertions.assertThat(articleList).isNotNull();

    }
    @Test
    public void ArticleRepository_FindByTitle_ReturnArticle() {
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
        //Act
        articleRepository.save(article);
        articleRepository.save(article2);
        Article foundArticle = articleRepository.findByTitle(article2.getTitle());

        //Assert
        Assertions.assertThat(foundArticle).isNotNull();

    }
    @Test
    public void ArticleRepository_Update_ReturnArticle() {
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
        //Act
        articleRepository.save(article);
        articleRepository.save(article2);
        Article foundArticle = articleRepository.findById(article2.getId()).get();
        foundArticle.setContent("New Content");
        Article saved = articleRepository.save(foundArticle);
        //Assert
        Assertions.assertThat(saved.getContent()).isNotNull();
        Assertions.assertThat(saved.getTitle()).isNotNull();

    }

    @Test
    public void ArticleRepository_DeleteById_ReturnArticle() {
        //Arrange
        Article article2 = Article
                .builder()
                .title("My Article2")
                .content("My Content2")
                .photo("")
                .build();
        //Act
        articleRepository.save(article2);
        articleRepository.deleteById(article2.getId());
        Optional<Article> articleList = articleRepository.findById(article2.getId());

        //Assert
        Assertions.assertThat(articleList).isEmpty();

    }
}