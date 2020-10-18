package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.UserDaoImpl.UserMapper;
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
public class PostDaoImpl implements PostDao {

    @Autowired
    JdbcTemplate jdbc;

//    private User user;
//    private Set<Hashtag> hashtagsForPost;
    //Helper Methods
    private User getUserForPost(int postId) {
        final String SELECT_USER_FOR_POST = "SELECT u.* FROM User u "
                + "JOIN Post p ON u.userId = p.userId "
                + "WHERE p.postId = ?";
        User user = jdbc.queryForObject(SELECT_USER_FOR_POST, new UserMapper(), postId);
        return user;
    }

    public static final class PostMapper implements RowMapper<Post> {

        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            Post post = new Post();
            post.setPostId(rs.getInt("postId"));
            post.setTitle(rs.getString("title"));
            post.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
            post.setPostAt(rs.getTimestamp("postAt").toLocalDateTime());
            post.setExpireAt(rs.getTimestamp("expireAt").toLocalDateTime());
            post.setLastEditedAt(rs.getTimestamp("lastEditedAt").toLocalDateTime());
            post.setContent(rs.getString("content"));
            post.setApprovalStatus(rs.getBoolean("approvalStatus"));
            post.setStaticPage(rs.getBoolean("staticPage"));
            post.setTitlePhoto(rs.getString("titlePhoto"));
            return post;
        }
    }
}
