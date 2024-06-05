package com.dmtrmrzv.kindpeople.services;

import com.dmtrmrzv.kindpeople.dto.PostDTO;
import com.dmtrmrzv.kindpeople.entities.ImageModel;
import com.dmtrmrzv.kindpeople.entities.Post;
import com.dmtrmrzv.kindpeople.entities.User;
import com.dmtrmrzv.kindpeople.exceptions.PostNotFoundException;
import com.dmtrmrzv.kindpeople.repositories.ImageRepository;
import com.dmtrmrzv.kindpeople.repositories.PostRepository;
import com.dmtrmrzv.kindpeople.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);


    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, ImageRepository imageRepository, UserService userService) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving Post by User: {} ", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found by username: " + user.getEmail()));
    }

    public List<Post> getAllPostsByUser(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers().stream()
                .filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }







}
