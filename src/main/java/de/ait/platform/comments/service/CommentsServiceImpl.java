package de.ait.platform.comments.service;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.comments.exception.CommentNotFound;
import de.ait.platform.comments.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@AllArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
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

    @Transactional
    @Override
    public CommentsResponseDto save(CommentsRequestDto dto) {
        Comment entity = mapper.map(dto, Comment.class);
        Comment newComment = commentsRepository.save(entity);
        return mapper.map(newComment, CommentsResponseDto.class);
    }


@Transactional
@Override
public CommentsResponseDto deleteComment(Long id) {
    Comment comment = commentsRepository
            .findById(id)
            .orElseThrow(() -> new CommentNotFound("Comment not found"));
    commentsRepository.deleteById(id);
    return mapper.map(comment, CommentsResponseDto.class);
}

    @Override
    @Transactional
    public CommentsResponseDto updateComment(Long id, CommentsRequestDto dto) {
    Comment entity = mapper.map(dto, Comment.class);
    entity.setId(id);
    Comment updatedComment = commentsRepository.save(entity);
    return mapper.map(updatedComment, CommentsResponseDto.class);
        }
    }