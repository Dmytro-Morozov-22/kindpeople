package com.dmtrmrzv.kindpeople.controllers;

import com.dmtrmrzv.kindpeople.dto.CommentDTO;
import com.dmtrmrzv.kindpeople.entities.Comment;
import com.dmtrmrzv.kindpeople.facade.CommentFacade;
import com.dmtrmrzv.kindpeople.payload.response.MessageResponse;
import com.dmtrmrzv.kindpeople.services.CommentService;
import com.dmtrmrzv.kindpeople.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValidation responseErrorValidation;

    public CommentController(CommentService commentService, CommentFacade commentFacade,
                             ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> response;
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            response = errors;
        } else {
            Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
            CommentDTO createdComment = commentFacade.commentToCommentDTO(comment);
            response = new ResponseEntity<>(createdComment, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllPostComments(@PathVariable("postId") String postId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId)).stream()
                .map(commentFacade::commentToCommentDTO).collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }

}
