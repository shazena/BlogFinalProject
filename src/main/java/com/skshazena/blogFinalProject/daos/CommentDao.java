package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Comment;
import java.util.List;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public interface CommentDao {

    Comment getCommentById(int commentId);

    List<Comment> getAllComments();

    List<Comment> getAllCommentsForPost(int postId);

    List<Comment> getAllCommentsWrittenByUser(int userId);

    List<Comment> getAllCommentsNeedingApproval();

    void updateComment(Comment comment);

    void deleteComment(int commentId);

    Comment createComment(Comment comment);
}
