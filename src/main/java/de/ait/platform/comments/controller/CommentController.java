package de.ait.platform.comments.controller;

import de.ait.platform.comments.dto.CommentsRequestDto;
import de.ait.platform.comments.dto.CommentsResponseDto;
import de.ait.platform.comments.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private final CommentsService commentsService;

    @Operation(summary = "Get all comments", description = "Retrieves a list of all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of comments retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.class)))
    })
    @GetMapping("/comments")
    public List<CommentsResponseDto> getAllComments() {
        return commentsService.getAllComments();
    }

    @Operation(summary = "Get comment by ID", description = "Retrieves a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @GetMapping("/comments/{id}")
    public CommentsResponseDto getCommentById(@Parameter(description = "ID of the comment to retrieve") @PathVariable Long id) {
        return commentsService.getCommentById(id);
    }

    @Operation(summary = "Create a new comment", description = "Adds a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comments")
    public CommentsResponseDto createComment(@RequestBody CommentsRequestDto commentDto) {
        return commentsService.save(commentDto);
    }

    @Operation(summary = "Update comment by ID", description = "Updates an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/comments/{id}")
    public CommentsResponseDto updateComment(@Parameter(description = "ID of the comment to update") @PathVariable Long id,
                                             @RequestBody CommentsRequestDto commentDto) {
        return commentsService.updateComment(id, commentDto);
    }

    @Operation(summary = "Delete comment by ID", description = "Deletes a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @DeleteMapping("/comments/{id}")
    public CommentsResponseDto deleteComment(@Parameter(description = "ID of the comment to delete") @PathVariable Long id) {
        return commentsService.deleteComment(id);
    }

}
