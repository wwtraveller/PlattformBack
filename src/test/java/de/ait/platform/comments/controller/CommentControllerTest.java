package de.ait.platform.comments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.platform.article.entity.Article;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.comments.service.CommentsService;
import de.ait.platform.security.service.TokenService;
import de.ait.platform.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private CommentsService commentsService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentsRequestDto commentRequestDto;
    private CommentsResponseDto commentResponseDto;

    @BeforeEach
    public void setup() {
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
        commentRequestDto = CommentsRequestDto.builder()
                .text("Test content")
                .user_id(1L)
                .article_id(1L)
                .build();
        commentResponseDto = CommentsResponseDto.builder()
                .id(1L)
                .text("Test content")
                .userId(1L)
                .articleId(1L)
                .build();
    }

    @Test
    public void CommentController_CreateComment_ReturnCreated() throws Exception {
        CommentsRequestDto requestDto = new CommentsRequestDto("Test content", 1L, 1L);
        CommentsResponseDto responseDto = new CommentsResponseDto(1L, "Test content", 1L, 1L);
        given(commentsService.save(any())).willReturn(responseDto);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isOk()) // Ось тут перевіряємо статус 201
                        .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Test content"));
    }

    @Test
    public void CommentController_GetAllComments_ReturnResponseDto() throws Exception {
        given(commentsService.getAllComments()).willReturn(Arrays.asList(commentResponseDto));
        mockMvc.perform(get("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void CommentController_GetCommentById_ReturnResponseDto() throws Exception {
        given(commentsService.getCommentById(1L)).willReturn(commentResponseDto);
        mockMvc.perform(get("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentResponseDto.getText()));
    }


    @Test
    public void CommentController_UpdateComment_ReturnUpdatedComment() throws Exception {
        given(commentsService.updateComment(1L, commentRequestDto)).willReturn(commentResponseDto);
        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentResponseDto.getText()));
    }


    @Test
    public void CommentController_DeleteComment_ReturnSuccess() throws Exception {
        given(commentsService.deleteComment(1L)).willReturn(commentResponseDto);
        mockMvc.perform(delete("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentResponseDto.getId()))
                .andExpect(jsonPath("$.text").value(commentResponseDto.getText()));
    }
}
