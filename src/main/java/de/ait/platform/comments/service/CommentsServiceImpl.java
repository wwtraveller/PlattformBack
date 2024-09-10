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
    public CommentsResponseDto getCommentById(Long commentid) {
        Comment comment = commentsRepository
                .findById(commentid)
                .orElseThrow(()-> new CommentNotFound("Comment not found"));
        return new CommentsResponseDto();
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
    public void deleteComment(Long id) {
        if (commentsRepository.existsById(id)) {
            commentsRepository.deleteById(id);
        } else {
            throw new CommentNotFound("Comment with id " + id + " does not exist");
        }
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