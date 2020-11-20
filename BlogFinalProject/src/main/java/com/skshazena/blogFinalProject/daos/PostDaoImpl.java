package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.HashtagDaoImpl.HashtagMapper;
import com.skshazena.blogFinalProject.daos.UserDaoImpl.UserMapper;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            final String SELECT_POST_BY_ID = "SELECT * FROM post "
                    + "WHERE postId = ?";

            Post post = jdbc.queryForObject(SELECT_POST_BY_ID, new PostMapper(), postId);

            associateUserAndHashtagsWithPost(post);

            return post;

        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Post> getAllPostsForAdminNewestFirst() {
        final String SELECT_ALL_POSTS = "SELECT * FROM post ORDER BY postAt DESC";
        List<Post> allPosts = jdbc.query(SELECT_ALL_POSTS, new PostMapper());
        for (Post post : allPosts) {
            associateUserAndHashtagsWithPost(post);
        }
        return allPosts;
    }

    @Override
    public List<Post> getAllPostsForBlogNonStaticNewestFirst() {
        final String SELECT_ALL_POSTS_FOR_BLOG = "SELECT * FROM post "
                + "WHERE postAt <= NOW() AND (expireAt >= NOW() OR expireAt IS NULL) "
                + "AND staticPage = 0 AND approvalStatus = 1 "
                + "ORDER BY postAt DESC";
        List<Post> allPosts = jdbc.query(SELECT_ALL_POSTS_FOR_BLOG, new PostMapper());
        for (Post post : allPosts) {
            associateUserAndHashtagsWithPost(post);
        }
        return allPosts;
    }

    @Override
    public List<Post> getAllPostsForBlogThatAreStaticNewestFirst() {
        final String SELECT_POSTS_THAT_ARE_STATIC = "SELECT * FROM post "
                + "WHERE staticPage = 1 "
                + "AND postAt <= NOW() "
                + "AND (expireAt >= NOW() OR expireAt IS NULL) "
                + "AND approvalStatus = 1 "
                + "ORDER BY postAt DESC";
        List<Post> staticPosts = jdbc.query(SELECT_POSTS_THAT_ARE_STATIC, new PostMapper());
        for (Post post : staticPosts) {
            associateUserAndHashtagsWithPost(post);
        }
        return staticPosts;
    }

    @Override
    public List<Post> getAllPostsForHashtagForAdminNewestFirst(int hashtagId) {
        final String SELECT_POSTS_FOR_HASHTAG = "SELECT p.* FROM post p "
                + "JOIN postHashtag ph ON p.postId = ph.postId "
                + "JOIN hashtag h ON h.hashtagId = ph.hashtagId "
                + "WHERE h.hashtagId = ? ORDER BY postAt DESC";
        List<Post> postsForHashtag = jdbc.query(SELECT_POSTS_FOR_HASHTAG, new PostMapper(), hashtagId);
        for (Post post : postsForHashtag) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsForHashtag;
    }

    @Override
    public List<Post> getAllPostsForBlogForHashtagNewestFirst(int hashtagId) {
        final String SELECT_POSTS_FOR_HASHTAG = "SELECT p.* FROM post p "
                + "JOIN postHashtag ph ON p.postId = ph.postId "
                + "JOIN hashtag h ON h.hashtagId = ph.hashtagId "
                + "WHERE h.hashtagId = ? "
                + "AND postAt <= NOW() AND (expireAt >= NOW() OR expireAt IS NULL) "
                + "AND approvalStatus = 1 "
                + "ORDER BY postAt DESC";
        List<Post> postsForHashtag = jdbc.query(SELECT_POSTS_FOR_HASHTAG, new PostMapper(), hashtagId);
        for (Post post : postsForHashtag) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsForHashtag;
    }

    @Override
    public List<Post> getAllPostsNeedingApprovalWrittenByCreatorOldestFirst(int userId) {
        final String SELECT_POSTS_NEEDING_APPROVAL_WRITTEN_BY_USER = "SELECT * from post "
                + "WHERE (approvalStatus = 0) AND (userId = ?) ORDER BY postAt ASC";
        List<Post> postsNeedingApprovalWrittenByThisUser = jdbc.query(SELECT_POSTS_NEEDING_APPROVAL_WRITTEN_BY_USER, new PostMapper(), userId);
        for (Post post : postsNeedingApprovalWrittenByThisUser) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsNeedingApprovalWrittenByThisUser;
    }

    @Override
    public List<Post> getAllPostsWrittenByCreatorNewestFirst(int userId) {
        final String SELECT_POSTS_WRITTEN_BY_USER = "SELECT * FROM post "
                + "WHERE userId = ? ORDER BY postAt DESC";
        List<Post> postsByUser = jdbc.query(SELECT_POSTS_WRITTEN_BY_USER, new PostMapper(), userId);
        for (Post post : postsByUser) {
            associateUserAndHashtagsWithPost(post);
        }
        return postsByUser;

    }

    @Override
    public List<Post> getAllPostsNeedingApprovalForAdminOldestFirst() {
        final String SELECT_POSTS_NEEDING_APPROVAL = "SELECT * FROM post "
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
        final String UPDATE_POST = "UPDATE post SET "
                + "title = ?, "
                + "userId = ?, "
                + "createdAt = ?, "
                + "postAt = ?, "
                + "expireAt = ?, "
                + "lastEditedAt = ?, "
                + "content = ?, "
                + "approvalStatus = ?, "
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

        final String DELETE_FROM_POSTHASHTAG = "DELETE FROM postHashtag "
                + "WHERE postId = ?";
        jdbc.update(DELETE_FROM_POSTHASHTAG, post.getPostId());

        final String INSERT_INTO_POSTHASHTAG = "INSERT INTO postHashtag (postId, hashtagId) "
                + "VALUES (?,?)";
        for (Hashtag hashtag : post.getHashtagsForPost()) {
            jdbc.update(INSERT_INTO_POSTHASHTAG, post.getPostId(), hashtag.getHashtagId());
        }
    }

    @Override
    @Transactional
    public void deletePost(int postId) {
        final String DELETE_COMMENTS_FOR_POST = "DELETE FROM comment "
                + "WHERE postId = ?";

        jdbc.update(DELETE_COMMENTS_FOR_POST, postId);

        final String DELETE_FROM_POSTHASHTAG = "DELETE FROM postHashtag "
                + "WHERE postId = ?";

        jdbc.update(DELETE_FROM_POSTHASHTAG, postId);

        final String DELETE_POST = "DELETE FROM post "
                + "WHERE postId = ?";

        jdbc.update(DELETE_POST, postId);

    }

    @Override
    @Transactional
    public Post createPost(Post post) {
        final String INSERT_POST = "INSERT INTO post"
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

        final String INSERT_INTO_POSTHASHTAG = "INSERT INTO postHashtag (postId, hashtagId) "
                + "VALUES (?,?)";
        for (Hashtag hashtag : post.getHashtagsForPost()) {
            jdbc.update(INSERT_INTO_POSTHASHTAG, post.getPostId(), hashtag.getHashtagId());
        }

        associateUserAndHashtagsWithPost(post);

        return post;
    }

    //Helper Methods
    private User getUserForPost(int postId) {
        final String SELECT_USER_FOR_POST = "SELECT u.* FROM user u "
                + "JOIN post p ON u.userId = p.userId "
                + "WHERE p.postId = ?";
        User user = jdbc.queryForObject(SELECT_USER_FOR_POST, new UserMapper(), postId);

        final String SELECT_ROLES_FOR_USER = "SELECT r.* FROM userRole ur "
                + "JOIN role r ON ur.role_id = r.roleId "
                + "WHERE ur.user_id = ?";

        Set<Role> roles = new HashSet(jdbc.query(SELECT_ROLES_FOR_USER, new RoleDaoImpl.RoleMapper(), user.getUserId()));

        user.setRoles(roles);

        return user;
    }

    private List<Hashtag> getHashtagsForPost(int postId) {
        final String SELECT_HASHTAGS_FOR_POST = "SELECT h.* from hashtag h "
                + "JOIN postHashtag ph ON h.hashtagId = ph.hashtagId "
                + "JOIN post p ON p.postId = ph.postId "
                + "WHERE p.postId = ?";
        return jdbc.query(SELECT_HASHTAGS_FOR_POST, new HashtagMapper(), postId);
        //this method intentionally does not bring in the number of posts per hashtag
        //it is not necessary when accessing hashtags from the PostDao.
        //It is only needed for when Hashtags are accessed from the HashtagDao
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
            if (rs.getTimestamp("expireAt") != null) {
                post.setExpireAt(rs.getTimestamp("expireAt").toLocalDateTime());
            } else {
                post.setExpireAt(null);
            }
            post.setLastEditedAt(rs.getTimestamp("lastEditedAt").toLocalDateTime());
            post.setContent(rs.getString("content"));
            post.setApprovalStatus(rs.getBoolean("approvalStatus"));
            post.setStaticPage(rs.getBoolean("staticPage"));
            post.setTitlePhoto(rs.getString("titlePhoto"));
            return post;
        }
    }
}
