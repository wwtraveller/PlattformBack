package de.ait.platform.comments.repository;


import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentsRepositoryTest {
    @Autowired
    private CommentsRepository commentsRepository;

    @Test
    public void CommentsRepository_Save_ReturnSavedComment() {
        Article article = Article
                .builder()
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        User user = User.builder()
                .username("exampleUser")
                .email("exampleUser@gmail.com")
                .password("qwerty007")
                .build();
        Comment comment = Comment.builder()
                .article(article).user(user)
                .text("Test comment")
                .build();
        Comment savedComment = commentsRepository.save(comment);
        System.out.println(savedComment.getText());
        Assertions.assertThat(savedComment).isNotNull();
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0);
    }
}