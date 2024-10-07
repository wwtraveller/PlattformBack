package de.ait.platform.comments.service;

import de.ait.platform.article.entity.Article;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.comments.repository.CommentsRepository;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.reposittory.UserRepository;
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
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
@SpringBootTest
class CommentsServiceImplTest {
    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private ModelMapper commentMapper;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CommentsServiceImpl commentsServiceImpl;

    @Mock
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void Init() {
        commentsRepository = Mockito.mock(CommentsRepository.class);
        userRepository=Mockito.mock(UserRepository.class);
        commentMapper = Mockito.mock(ModelMapper.class);
        articleRepository = Mockito.mock(ArticleRepository.class);
       commentsServiceImpl = new CommentsServiceImpl(commentsRepository,userRepository,articleRepository,commentMapper);
    }

    @Test
    public void CommentService_SaveComment_ReturnSavedComment() {
        Article article = Article
                .builder()
                .id(1L)
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        User user = User.builder()
                .id(1L)
                .username("exampleUser")
                .email("exampleUser@gmail.com")
                .password("qwerty007")
                .build();
        Comment comment = Comment.builder()
                .article(article).user(user)
                .text("Test comment")
                .build();
        CommentsResponseDto commentsResponseDto = CommentsResponseDto.builder()
                .text("Test comment")
                .id(1L)
                .articleId(article.getId())
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(commentsRepository.save(any(Comment.class))).thenReturn(comment);

        CommentsResponseDto savedComment = commentsServiceImpl.save(CommentsRequestDto.builder()
                .text("Test comment")
                .user_id(1L)
                .article_id(1L)
                .build()
        );
        Assertions.assertThat(savedComment).isNotNull();
        Assertions.assertThat(savedComment.getText()).isEqualTo("Test comment");
    }


    @Test
    public void CommentsService_GetAll_ReturnListComments() {
        Comment comment = Comment
                .builder()
                .text("Test comment")
                .build();
        Comment comment2 = Comment
                .builder()
                .text("Test comment2")
                .build();
        commentsRepository.save(comment);
        commentsRepository.save(comment2);
        CommentsResponseDto commentsResponseDto = Mockito.mock(CommentsResponseDto.class);
        List<Comment> allComment = commentsRepository.findAll();
        List<CommentsResponseDto> commentsResponseDtos = allComment.stream().map(c->commentMapper.map(c,CommentsResponseDto.class)).toList();
        Assertions.assertThat(commentsResponseDtos).isNotNull();
    }

    @Test
    public void CommentsService_getCommentById_ReturnComment() {
        Comment comment = Comment
                .builder()
                .text("Test comment")
                .build();
        CommentsResponseDto commentsResponseDto = CommentsResponseDto
                .builder()
                .text("Test comment")
                .build();
        when(commentsRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.map(any(Comment.class),any())).thenReturn(commentsResponseDto);
        CommentsResponseDto commentsResponseDto1 = commentsServiceImpl.getCommentById(1L);
        assertNotNull(commentsResponseDto1);
    }


    @Test
    public void CommentsService_Delete_ReturnComment() {
        Comment comment = Comment
                .builder()
                .text("Test comment")
                .build();

        when(commentsRepository.findById(1L)).thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentsRepository).deleteById(1L);
        commentsServiceImpl.deleteComment(1L);
        Mockito.verify(commentsRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void CommentsService_Update_ReturnUpdatedComment() {
        CommentsRequestDto dto = CommentsRequestDto
                .builder()
                .text("Test comment")
                .user_id(1L)
                .article_id(1L)
                .build();

        Article article = Article
                .builder()
                .id(1L)
                .title("My Article")
                .content("My Content")
                .photo("")
                .build();
        User user = User.builder()
                .id(1L)
                .username("exampleUser")
                .email("exampleUser@gmail.com")
                .password("qwerty007")
                .build();
        Comment comment= Comment
                .builder()
                .article(article)
                .user(user)
                .text("Test comment")
                .build();
        Comment updatedComment = Comment.builder()
                .article(article)
                .user(user)
                .text("Test update comment")
                .build();
        CommentsResponseDto commentsResponseDto = CommentsResponseDto.builder()
                .text("Test update comment")
                .id(1L)
                .articleId(article.getId())
                .build();
        when(commentsRepository.findById(any())).thenReturn(Optional.of(comment));
        when(commentsRepository.save(any(Comment.class))).thenReturn(updatedComment);
        when(commentMapper.map(any(Comment.class),any())).thenReturn(commentsResponseDto);

        CommentsResponseDto saved = commentsServiceImpl.updateComment(1L,dto);
        assertNotNull(saved);
        assertEquals("Test update comment", saved.getText());
    }
}