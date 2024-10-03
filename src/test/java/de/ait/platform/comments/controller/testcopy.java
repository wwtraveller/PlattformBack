//package de.ait.platform.comments.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import de.ait.platform.article.repository.ArticleRepository;
//import de.ait.platform.comments.dto.CommentsRequestDto;
//import de.ait.platform.comments.dto.CommentsResponseDto;
//import de.ait.platform.comments.repository.CommentsRepository;
//import de.ait.platform.comments.service.CommentsServiceImpl;
//import de.ait.platform.user.reposittory.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class testcopy {
//
//
//
//
//
//
//
//    package de.ait.platform.comments.controller;
//
//import de.ait.platform.article.repository.ArticleRepository;
//import de.ait.platform.comments.dto.CommentsRequestDto;
//import de.ait.platform.comments.dto.CommentsResponseDto;
//import de.ait.platform.comments.repository.CommentsRepository;
//import de.ait.platform.comments.service.CommentsService;
//import de.ait.platform.comments.service.CommentsServiceImpl;
//import de.ait.platform.user.reposittory.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import com.fasterxml.jackson.databind.ObjectMapper;
//    @WebMvcTest(controllers = CommentController.class)
//    @AutoConfigureMockMvc(addFilters = false)
//    @ExtendWith(MockitoExtension.class)
//    class CommentControllerTest {
//
//        @Autowired
//        private MockMvc mockMvc;
//
//        @MockBean
//        private CommentsServiceImpl commentsService;
//
//        @Autowired
//        private ModelMapper commentMapper;
//
//        private UserRepository userRepository;
//
//        private CommentsRepository commentsRepository;
//
//        private CommentsServiceImpl commentsServiceImpl;
//
//        private ArticleRepository articleRepository;
//        private CommentsResponseDto commentResponse;
//        private CommentsRequestDto commentRequest;
//
//
//        @BeforeEach
//        public void Init() {
//            commentResponse = CommentsResponseDto.builder()
//                    .id(1L)
//                    .text("Test comment")
//                    .articleId(1L)
//                    .build();
//
//            commentRequest = CommentsRequestDto.builder()
//                    .text("Test comment")
//                    .user_id(1L)
//                    .article_id(1L)
//                    .build();}
//
//        @Test
//        public void CommentsController_SaveComment_ReturnSaved() throws Exception {
//            when(commentsService.save(Mockito.any(CommentsRequestDto.class))).thenReturn(commentResponse);
//
//            ObjectMapper objectMapper = null;
//            mockMvc.perform(post("/api/comments")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(commentRequest)))
//                    .andExpect(status().isCreated())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.text").value("Test comment"));
//        }
//
//        @Test
//        public void getAllComments() {
//        }
//
//        @Test
//        void getCommentById() {
//        }
//
//        @Test
//        void createComment() {
//        }
//
//        @Test
//        void updateComment() {
//        }
//
//        @Test
//        void deleteComment() {
//        }
//    }
//}
