package com.dmtrmrzv.kindpeople.facade;

import com.dmtrmrzv.kindpeople.dto.PostDTO;
import com.dmtrmrzv.kindpeople.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setCaption(post.getCaption());
        postDTO.setLocation(post.getLocation());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsersLiked(post.getLikedUsers());
        return postDTO;
    }

}
