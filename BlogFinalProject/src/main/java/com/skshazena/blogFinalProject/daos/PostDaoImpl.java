package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.HashtagDaoImpl.HashtagMapper;
import com.skshazena.blogFinalProject.daos.UserDaoImpl.UserMapper;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
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
public class PostDaoImpl implements PostDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Post getPostById(int postId) {
        try {
            final String SELECT_POST_BY_ID = "SELECT * FROM Post "
                    + "WHERE postId = ?";

            Post post = jdbc.queryForObject(SELECT_POST_BY_ID, new PostMapper(), postId);

            associateUserAndHashtagsWithPost(post);

            return post;

        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        final String SELECT_ALL_POSTS = "SELECT * FROM Post ORDER BY postAt DESC";
        List<Post> allPosts = jdbc.query(SELECT_ALL_POSTS, new PostMapper());
        for (Post post : allPosts) {
            associateUserAndHashtagsWithPost(post);
        }
        return allPosts;
    }

    @Override
    public List<Post> getAllPostsThatAreStatic() {
        final String SELECT_POSTS_THAT_ARE_STATIC = "SELECT * FROM Post "
                + "WHERE staticPage = 1 ORDER BY postAt DESC";
        List<Post> staticPosts = jdbc.query(SELECT_POSTS_THAT_ARE_STATIC, new PostMapper());
        for (Post post : staticPosts) {
            associateUserAndHashtagsWithPost(post);
        }
        return staticPosts;
    }

    @Override
    public List<Post> getAllPostsForHashtag(int hashtagId) {
        final String SELECT_POSTS_FOR_HASHTAG = "SELECT p.* FROM Post p "
                + "JOIN PostHashtag ph ON p.postId = ph.postId "
                + "JOIN Hashtag h ON h.hashtagId = ph.hashtagId "
                + "WHERE h.hashtagId = ? ORDER BY postAt DESC";
        List<Post> postsForHashtag = jdbc.query(SELECT_POSTS_FOR_HASHTAG, new PostMapper(), hashtagId);
        for (Post post : postsForHashtag) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsForHashtag;
    }

    @Override
    public List<Post> getAllPostsNeedingApprovalWrittenByUser(int userId) {
        final String SELECT_POSTS_NEEDING_APPROVAL_WRITTEN_BY_USER = "SELECT * from Post "
                + "WHERE (approvalStatus = 0) AND (userId = ?) ORDER BY postAt ASC";
        List<Post> postsNeedingApprovalWrittenByThisUser = jdbc.query(SELECT_POSTS_NEEDING_APPROVAL_WRITTEN_BY_USER, new PostMapper(), userId);
        for (Post post : postsNeedingApprovalWrittenByThisUser) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsNeedingApprovalWrittenByThisUser;
    }

    @Override
    public List<Post> getAllPostsWrittenByUser(int userId) {
        final String SELECT_POSTS_WRITTEN_BY_USER = "SELECT * FROM Post "
                + "where userId = ? ORDER BY postAt DESC";
        List<Post> postsByUser = jdbc.query(SELECT_POSTS_WRITTEN_BY_USER, new PostMapper(), userId);
        for (Post post : postsByUser) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsByUser;

    }

    @Override
    public List<Post> getAllPostsNeedingApprovalForAdmin() {
        final String SELECT_POSTS_NEEDING_APPROVAL = "SELECT * FROM Post "
                + "WHERE approvalStatus = 0 "
                + "ORDER BY postAt ASC";
        List<Post> postsNeedingApproval = jdbc.query(SELECT_POSTS_NEEDING_APPROVAL, new PostMapper());
        for (Post post : postsNeedingApproval) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsNeedingApproval;
    }

    @Override
    @Transactional
    public void updatePost(Post post) {
        final String UPDATE_POST = "UPDATE Post SET "
                + "title = ?, "
                + "userId = ?, "
                + "createdAt = ?, "
                + "postAt = ?, "
                + "expireAt = ?, "
                + "lastEditedAt = ?, "
                + "content = ?, "
                + "spprovalStatus = ?, "
                + "staticPage = ?, "
                + "titlePhoto = ? "
                + "WHERE postId = ?";

        jdbc.update(UPDATE_POST,
                post.getTitle(),
                post.getUser().getUserId(),
                post.getCreatedAt(),
                post.getPostAt(),
                post.getExpireAt(),
                post.getLastEditedAt(),
                post.getContent(),
                post.isApprovalStatus(),
                post.isStaticPage(),
                post.getTitlePhoto(),
                post.getPostId());

        final String DELETE_FROM_POSTHASHTAG = "DELETE FROM PostHashtag "
                + "WHERE postId = ?";
        jdbc.update(DELETE_FROM_POSTHASHTAG, post.getPostId());

        final String INSERT_INTO_POSTHASHTAG = "INSERT INTO PostHashtag (postId, hashtagId) "
                + "VALUES (?,?)";
        for (Hashtag hashtag : post.getHashtagsForPost()) {
            jdbc.update(INSERT_INTO_POSTHASHTAG, post.getPostId(), hashtag.getHashtagId());
        }
    }

    @Override
    @Transactional
    public void deletePost(int postId) {
        final String DELETE_COMMENTS_FOR_POST = "DELETE FROM Comment "
                + "WHERE postId = ?";

        jdbc.update(DELETE_COMMENTS_FOR_POST, postId);

        final String DELETE_FROM_POSTHASHTAG = "DELETE FROM PostHashtag "
                + "WHERE postId = ?";

        jdbc.update(DELETE_FROM_POSTHASHTAG, postId);

        final String DELETE_POST = "DELETE FROM POST "
                + "WHERE postId = ?";

        jdbc.update(DELETE_POST, postId);

    }

    @Override
    @Transactional
    public Post createPost(Post post) {
        final String INSERT_POST = "INSERT INTO Post"
                + "(title, userId, createdAt, postAt, expireAt, lastEditedAt, content, approvalStatus, staticPage, titlePhoto) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(INSERT_POST,
                post.getTitle(),
                post.getUser().getUserId(),
                post.getCreatedAt(),
                post.getPostAt(),
                post.getExpireAt(),
                post.getLastEditedAt(),
                post.getContent(),
                post.isApprovalStatus(),
                post.isStaticPage(),
                post.getTitlePhoto());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        post.setPostId(newId);

        final String INSERT_INTO_POSTHASHTAG = "INSERT INTO PostHashtag (postId, hashtagId) "
                + "VALUES (?,?)";
        for (Hashtag hashtag : post.getHashtagsForPost()) {
            jdbc.update(INSERT_INTO_POSTHASHTAG, post.getPostId(), hashtag.getHashtagId());
        }

        return post;
    }

    //Helper Methods
    private User getUserForPost(int postId) {
        final String SELECT_USER_FOR_POST = "SELECT u.* FROM User u "
                + "JOIN Post p ON u.userId = p.userId "
                + "WHERE p.postId = ?";
        User user = jdbc.queryForObject(SELECT_USER_FOR_POST, new UserMapper(), postId);
        return user;
    }

    private List<Hashtag> getHashtagsForPost(int postId) {
        final String SELECT_HASHTAGS_FOR_POST = "SELECT h.* from Hashtag h "
                + "JOIN postHashtag ph ON h.hashtagId = ph.hashtagId "
                + "JOIN post p ON p.postId = ph.postId "
                + "WHERE p.postId = ?";
        return jdbc.query(SELECT_HASHTAGS_FOR_POST, new HashtagMapper(), postId);
    }

    private void associateUserAndHashtagsWithPost(Post post) {
        post.setUser(getUserForPost(post.getPostId()));
        post.setHashtagsForPost(getHashtagsForPost(post.getPostId()));
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
