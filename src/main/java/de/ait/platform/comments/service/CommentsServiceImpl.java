package de.ait.platform.comments.service;

import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.comments.exception.CommentConflictException;
import de.ait.platform.comments.exception.CommentForbiddenException;
import de.ait.platform.comments.exception.CommentNotFound;
import de.ait.platform.comments.repository.CommentsRepository;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.exceptions.UserNotFound;
import de.ait.platform.user.reposittory.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ModelMapper mapper;
@Autowired
    public CommentsServiceImpl(CommentsRepository commentsRepository, UserRepository userRepository, ArticleRepository articleRepository, ModelMapper mapper) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CommentsResponseDto> getAllComments() {
        List<Comment> comments = commentsRepository.findAll();
        if (comments.isEmpty()) {

            throw new CommentNotFound("No comments found");
        }

        return comments.stream().map(c->mapper.map(c, CommentsResponseDto.class)).toList();
    }

    @Transactional
    @Override
    public CommentsResponseDto getCommentById(Long id) {
        Comment comment = commentsRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFound("No comment found"));
        return mapper.map(comment, CommentsResponseDto.class);
    }

    @Transactional
    @Override
    public CommentsResponseDto save(CommentsRequestDto dto) {
        User user = userRepository.findById(dto.getUser_id())
                .orElseThrow(() -> new UserNotFound(STR."User with ID: \{dto.getUser_id()} not found"));
        Article article = articleRepository.findById(dto.getArticle_id())
                .orElseThrow(() -> new ArticleNotFound(STR."Article with  ID: \{dto.getArticle_id()}not found"));

        Comment newComment = new Comment();
        newComment.setText(dto.getText());
        newComment.setUser(user);
        newComment.setArticle(article);
        Comment savedComment = commentsRepository.save(newComment);
        return new CommentsResponseDto(savedComment.getId(),
                newComment.getText(),newComment
                .getUser().getId(), newComment.getArticle().getId());
    }

    @Transactional
    @Override
    public CommentsResponseDto deleteComment(Long id) {
        Comment comment = commentsRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFound("Comment not found"));
//
//        if(!comment.getUser().getId().equals(comment.getArticle().getUser().getId())) {
//            throw new CommentForbiddenException("You are not allowed to delete this comment");
//        }
        commentsRepository.deleteById(id);
        return mapper.map(comment, CommentsResponseDto.class);
    }


    @Transactional
    @Override
    public CommentsResponseDto updateComment(Long id, CommentsRequestDto dto) {
        Comment comment = commentsRepository.findById(id)
                .orElseThrow(() -> new CommentNotFound("Comment not found"));
//        if(!comment.getUser().getId().equals(comment.getArticle().getUser().getId())) {
//            throw new CommentForbiddenException("You are not allowed to delete this comment");
//        }
        comment.setText(dto.getText());
        Comment updatedComment = commentsRepository.save(comment);
        return mapper.map(updatedComment, CommentsResponseDto.class);
    }
}