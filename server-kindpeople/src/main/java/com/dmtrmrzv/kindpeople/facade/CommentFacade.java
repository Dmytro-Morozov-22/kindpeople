package com.dmtrmrzv.kindpeople.facade;

import com.dmtrmrzv.kindpeople.dto.CommentDTO;
import com.dmtrmrzv.kindpeople.entities.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setMessage(comment.getMessage());
        return commentDTO;
    }


}
