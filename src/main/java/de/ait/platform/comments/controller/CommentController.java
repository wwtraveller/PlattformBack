package de.ait.platform.comments.controller;

import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentsService commentsService;

    @GetMapping
    public List<CommentsResponseDto> getAllComments() {
        return commentsService.getAllComments();
    }
}
