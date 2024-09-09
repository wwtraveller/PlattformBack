package de.ait.platform.comments.service;

import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.entity.Comment;

import java.util.List;

public interface CommentsService {
        List<CommentsResponseDto> getAllComments();
        Comment getCommentById(Long id);
        CommentsResponseDto saveComment(CommentsRequestDto comment);  // Тут приймає DTO
        void deleteComment(Long id);
}