package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.PostDaoImpl.PostMapper;
import com.skshazena.blogFinalProject.daos.PostDaoImpl.UserMapper;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
@Repository
public class CommentDaoImpl implements CommentDao {

    @Autowired
    JdbcTemplate jdbc;

//    private String title;
//    private String content;
//    private LocalDateTime createdAt;
//    private boolean approvalStatus;
//    private Post post;
//    private User user;
    //Helper methods
    private User getUserForComment(int commentId) {
        final String SELECT_USER_FOR_COMMENT = "SELCT u.* from User u "
                + "JOIN Comment c ON c.userId = u.userId "
                + "WHERE c.commentId = ?";
        User user = jdbc.queryForObject(SELECT_USER_FOR_COMMENT, new UserMapper(), commentId);
        return user;
    }

    private Post getPostForComment(int commentId) {
        final String SELECT_POST_FOR_COMMENT = "SELCT p.* from Post p "
                + "JOIN Comment c ON c.postId = p.postId "
                + "WHERE c.commentId = ?";
        Post post = jdbc.queryForObject(SELECT_POST_FOR_COMMENT, new PostMapper(), commentId);

        final String SELECT_USER_FOR_POST = "SELECT u.* FROM User u "
                + "JOIN Post p ON u.userId = p.userId "
                + "WHERE p.postId = ?";
        User user = jdbc.queryForObject(SELECT_USER_FOR_POST, new UserDaoImpl.UserMapper(), post.getPostId());

        post.setUser(user);

        return post;
    }

    private void associateUserAndPostWithComment(Comment comment) {
        comment.setUser(getUserForComment(comment.getCommentId()));
        comment.setPost(getPostForComment(comment.getCommentId()));
    }

    public static final class CommentMapper implements RowMapper<Comment> {

        @Override
        public Comment mapRow(ResultSet rs, int i) throws SQLException {
            Comment comment = new Comment();
            comment.setCommentId(rs.getInt("commentId"));
            comment.setTitle(rs.getString("title"));
            comment.setContent(rs.getString("content"));
            comment.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
            comment.setApprovalStatus(rs.getBoolean("approvalStatus"));
            return comment;
        }
    }
}
