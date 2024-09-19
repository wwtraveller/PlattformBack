package de.ait.platform.comments.controller;
import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentsService commentsService;

    @Operation(summary = "Get all comments", description = "Retrieve a list of all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of comments", content = @Content(schema = @Schema(implementation = CommentsResponseDto.class)))
    })
    @GetMapping("/comments")
    public List<CommentsResponseDto> getAllComments() {
        return commentsService.getAllComments();
    }

    @Operation(summary = "Get comment by ID", description = "Retrieve a single comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment details", content = @Content(schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @GetMapping("/comments/{id}")
    public CommentsResponseDto getCommentById(@PathVariable Long id) {
    return commentsService.getCommentById(id);
    }

    @Operation(summary = "Create a new comment", description = "Create a new comment for an article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created", content = @Content(schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/comments")
    public CommentsResponseDto createComment(@RequestBody CommentsRequestDto commentDto) {
        return  commentsService.save(commentDto);
    }

    @Operation(summary = "Update a comment", description = "Update an existing comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated", content = @Content(schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @PutMapping("/comments/{id}")
    public CommentsResponseDto updateComment(@PathVariable Long id, @RequestBody CommentsRequestDto commentDto) {
                return  commentsService.updateComment(id, commentDto);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted", content = @Content(schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @DeleteMapping("/comments/{id}")
    public CommentsResponseDto deleteComment(@PathVariable Long id) {
    return commentsService.deleteComment(id);
    }
}