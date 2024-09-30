package de.ait.platform.comments.service;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import java.util.List;

public interface CommentsService {
        List<CommentsResponseDto> getAllComments();
        CommentsResponseDto getCommentById(Long id);
        CommentsResponseDto save(CommentsRequestDto comment);
        CommentsResponseDto deleteComment(Long id);
        CommentsResponseDto updateComment(Long id, CommentsRequestDto comment);

}