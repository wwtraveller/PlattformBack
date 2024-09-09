package de.ait.platform.comments.service;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;
import de.ait.platform.comments.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


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
    public Comment getCommentById(Long commentid) {
        Comment comment = commentsRepository
                .findById(commentid)
                .orElseThrow(()-> new NoSuchElementException("Comment not found"));
      return comment;
    }
@Transactional
@Override
public CommentsResponseDto saveComment(CommentsRequestDto dto) {
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
            throw new NoSuchElementException("Comment with id " + id + " does not exist");
        }
    }
}
