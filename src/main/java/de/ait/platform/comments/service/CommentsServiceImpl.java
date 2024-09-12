package de.ait.platform.comments.service;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.article.repository.ArticleRepository;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.comments.exception.CommentNotFound;
import de.ait.platform.comments.repository.CommentsRepository;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.exceptions.UserNotFound;
import de.ait.platform.user.reposittory.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@AllArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ModelMapper mapper;

    @Override
    public List<CommentsResponseDto> getAllComments() {
        List<Comment> comments = commentsRepository.findAll();
        return comments.stream().map(c->mapper.map(c, CommentsResponseDto.class)).toList();
    }


@Override
public CommentsResponseDto getCommentById(Long id) {
    Comment comment = commentsRepository
            .findById(id)
            .orElseThrow(() -> new CommentNotFound("Comment not found"));
    return mapper.map(comment, CommentsResponseDto.class);
}



@Override
public CommentsResponseDto save(CommentsRequestDto dto) {
    User user = userRepository.findById(dto.getUser_id())
          .orElseThrow(() -> new UserNotFound("Пользователь не найден с ID: " + dto.getUser_id()));
    Article article = articleRepository.findById(dto.getArticle_id())
            .orElseThrow(() -> new ArticleNotFound("Статья не найдена с ID: " + dto.getArticle_id()));
    Comment newComment = new Comment();
    newComment.setText(dto.getText());
    newComment.setUser(user);
    newComment.setArticle(article);
    Comment savedComment = commentsRepository.save(newComment);
    CommentsResponseDto commentsResponseDto = new CommentsResponseDto(savedComment.getId(),
            newComment.getText(),newComment
            .getUser().getId(), newComment.getArticle().getId());
    return commentsResponseDto;
}

@Override
public CommentsResponseDto deleteComment(Long id) {
    Comment comment = commentsRepository
            .findById(id)
            .orElseThrow(() -> new CommentNotFound("Comment not found"));
    commentsRepository.deleteById(id);
    return mapper.map(comment, CommentsResponseDto.class);
}


@Transactional
@Override
public CommentsResponseDto updateComment(Long id, CommentsRequestDto dto) {
    Comment comment = commentsRepository.findById(id)
            .orElseThrow(() -> new CommentNotFound("Comment not found"));
    comment.setText(dto.getText());
    Comment updatedComment = commentsRepository.save(comment);
    return mapper.map(updatedComment, CommentsResponseDto.class);
    }
}