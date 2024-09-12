package de.ait.platform.comments.controller;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentsService commentsService;

        @GetMapping
    public List<CommentsResponseDto> getAllComments() {
        return commentsService.getAllComments();
    }

@GetMapping("/{id}")
public CommentsResponseDto getCommentById(@PathVariable Long id) {
    return commentsService.getCommentById(id);
}

    @PostMapping
    public CommentsResponseDto createComment(@RequestBody CommentsRequestDto commentDto) {
        return  commentsService.save(commentDto);
        }


    @PutMapping("/{id}")
    public CommentsResponseDto updateComment(@PathVariable Long id, @RequestBody CommentsRequestDto commentDto) {
        return  commentsService.updateComment(id, commentDto);
        }


    @DeleteMapping("/{id}")
    public CommentsResponseDto deleteComment(@PathVariable Long id) {
return commentsService.deleteComment(id);

    }
}