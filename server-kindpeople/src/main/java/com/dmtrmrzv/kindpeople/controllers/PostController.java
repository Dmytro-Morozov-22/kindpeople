package com.dmtrmrzv.kindpeople.controllers;

import com.dmtrmrzv.kindpeople.dto.PostDTO;
import com.dmtrmrzv.kindpeople.entities.Post;
import com.dmtrmrzv.kindpeople.facade.PostFacade;
import com.dmtrmrzv.kindpeople.payload.response.MessageResponse;
import com.dmtrmrzv.kindpeople.services.PostService;
import com.dmtrmrzv.kindpeople.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@CrossOrigin
public class PostController {

    private final PostFacade postFacade;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PostController(PostFacade postFacade, PostService postService,
                          ResponseErrorValidation responseErrorValidation) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        ResponseEntity<Object> response;

        if (!ObjectUtils.isEmpty(errors)) {
            response = errors;
        } else {
            Post post = postService.createPost(postDTO, principal);
            PostDTO createdPost = postFacade.postToPostDTO(post);
            response = new ResponseEntity<>(createdPost, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = postService.getAllPosts().stream()
                .map(postFacade::postToPostDTO).collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllUserPosts(Principal principal) {
        List<PostDTO> postDTOList = postService.getAllPostsByUser( principal).stream()
                .map(postFacade::postToPostDTO).collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username) {
        Post post = postService.likePost(Long.parseLong(postId), username);
        PostDTO postDTO = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);

        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }

}
