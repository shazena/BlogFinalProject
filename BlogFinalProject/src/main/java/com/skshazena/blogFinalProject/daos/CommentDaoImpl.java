package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.PostDaoImpl.PostMapper;
import com.skshazena.blogFinalProject.daos.UserDaoImpl.UserMapper;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Comment getCommentById(int commentId) {
        try {
            final String SELECT_COMMENT_BY_ID = "SELECT * FROM Comment "
                    + "WHERE commentId = ?";
            Comment comment = jdbc.queryForObject(SELECT_COMMENT_BY_ID, new CommentMapper(), commentId);
            associateUserAndPostWithComment(comment);
            return comment;
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Comment> getAllComments() {
        final String SELECT_COMMENTS = "SELECT * FROM Comment";

        List<Comment> allComments = jdbc.query(SELECT_COMMENTS, new CommentMapper());

        for (Comment comment : allComments) {
            associateUserAndPostWithComment(comment);
        }

        return allComments;

    }

    @Override
    public List<Comment> getAllCommentsForPost(int postId) {

        final String SELECT_COMMENTS_FOR_POST = "SELECT * from Comment "
                + "WHERE postId = ?";

        List<Comment> commentsForPost = jdbc.query(SELECT_COMMENTS_FOR_POST, new CommentMapper(), postId);

        for (Comment comment : commentsForPost) {
            associateUserAndPostWithComment(comment);
        }
        return commentsForPost;
    }

    @Override
    public List<Comment> getAllCommentsWrittenByUser(int userId) {
        final String SELECT_COMMENTS_FOR_USER = "SELECT * from Comment "
                + "WHERE userId = ?";

        List<Comment> commentsForUser = jdbc.query(SELECT_COMMENTS_FOR_USER, new CommentMapper(), userId);

        for (Comment comment : commentsForUser) {
            associateUserAndPostWithComment(comment);
        }

        return commentsForUser;
    }

    @Override
    public List<Comment> getAllCommentsNeedingApproval() {
        final String SELECT_COMMENTS_NEEDING_APPROVAL = "SELECT * from Comment "
                + "WHERE approvalStatus = 0";

        List<Comment> commentsNeedingApproval = jdbc.query(SELECT_COMMENTS_NEEDING_APPROVAL, new CommentMapper());

        for (Comment comment : commentsNeedingApproval) {
            associateUserAndPostWithComment(comment);
        }

        return commentsNeedingApproval;
    }

    @Override
    @Transactional
    public void updateComment(Comment comment) {
        final String UPDATE_COMMENT = "UPDATE comment SET "
                + "title = ?, "
                + "content = ?, "
                + "createdAt = ?, "
                + "approvalStatus = ?, "
                + "postId = ?, "
                + "userId = ? "
                + "WHERE commentId = ?";

        jdbc.update(UPDATE_COMMENT,
                comment.getTitle(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.isApprovalStatus(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId(),
                comment.getCommentId());

    }

    @Override
    @Transactional
    public void deleteComment(int commentId) {
        final String DELETE_COMMENT = "DELETE FROM Comment "
                + "WHERE commentId = ?";
        jdbc.update(DELETE_COMMENT, commentId);
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        final String INSERT_COMMENT = "INSERT INTO Comment(title, content, createdAt, approvalStatus, postId, userId) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        jdbc.update(INSERT_COMMENT,
                comment.getTitle(),
                comment.getContent(),
                comment.getCreatedAt().withNano(0),
                comment.isApprovalStatus(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        comment.setCommentId(newId);

        associateUserAndPostWithComment(comment);

        return comment;

    }

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
