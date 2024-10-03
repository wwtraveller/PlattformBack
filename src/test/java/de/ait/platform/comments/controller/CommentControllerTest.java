package de.ait.platform.comments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.service.CommentsService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentsService commentsService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentsRequestDto commentRequestDto;
    private CommentsResponseDto commentResponseDto;

    @BeforeEach
    public void setup() {
        // Ініціалізація DTO для тестування
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
        // Мокаємо створення коментаря
        given(commentsService.save(ArgumentMatchers.any())).willReturn(commentResponseDto);

        ResultActions response = mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(commentResponseDto.getText())));
    }

    @Test
    public void CommentController_GetAllComments_ReturnResponseDto() throws Exception {
        // Мокаємо отримання всіх коментарів
        when(commentsService.getAllComments()).thenReturn(Arrays.asList(commentResponseDto));

        ResultActions response = mockMvc.perform(get("/api/comments")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(1)));
    }

    @Test
    public void CommentController_GetCommentById_ReturnResponseDto() throws Exception {
        // Мокаємо отримання коментаря по ID
        when(commentsService.getCommentById(1L)).thenReturn(commentResponseDto);

        ResultActions response = mockMvc.perform(get("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(commentResponseDto.getText())));
    }

    @Test
    public void CommentController_UpdateComment_ReturnUpdatedComment() throws Exception {
        // Мокаємо оновлення коментаря
        when(commentsService.updateComment(1L, commentRequestDto)).thenReturn(commentResponseDto);

        ResultActions response = mockMvc.perform(put("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequestDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(commentResponseDto.getText())));
    }

    @Test
    public void CommentController_DeleteComment_ReturnSuccess() throws Exception {
        // Мокаємо видалення коментаря
        doNothing().when(commentsService).deleteComment(1L);

        ResultActions response = mockMvc.perform(delete("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
