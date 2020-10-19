package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.PostDaoImpl.PostMapper;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
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
public class HashtagDaoImpl implements HashtagDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Hashtag getHashtagById(int hashtagId) {

        try {
            final String SELECT_HASHTAG_BY_ID = "SELECT * FROM Hashtag "
                    + "WHERE hashtagId = ?";
            Hashtag hashtag = jdbc.queryForObject(SELECT_HASHTAG_BY_ID, new HashtagMapper(), hashtagId);
            hashtag.setNumberOfPosts(getNumberOfPostsForHashtag(hashtagId));
            return hashtag;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Hashtag getHashtagByTitle(String title) {
        try {
            final String SELECT_HASHTAG_BY_TITLE = "SELECT * FROM Hashtag "
                    + "WHERE title = ?";
            Hashtag hashtag = jdbc.queryForObject(SELECT_HASHTAG_BY_TITLE, new HashtagMapper(), title);
            hashtag.setNumberOfPosts(getNumberOfPostsForHashtag(hashtag.getHashtagId()));
            return hashtag;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Hashtag> getAllHashtags() {
        final String SELECT_HASHTAGS = "SELECT * FROM Hashtag";
        List<Hashtag> allHashtags = jdbc.query(SELECT_HASHTAGS, new HashtagMapper());
        for (Hashtag hashtag : allHashtags) {
            hashtag.setNumberOfPosts(getNumberOfPostsForHashtag(hashtag.getHashtagId()));
        }
        return allHashtags;
    }

    @Override
    public List<Hashtag> getAllHashtagsForPost(int postId) {
        final String SELECT_HASHTAGS_FOR_POST = "SELECT h.* FROM Hashtag h "
                + "JOIN PostHashtag ph ON h.hashtagId = ph.hashtagId "
                + "WHERE ph.postId = ?";
        List<Hashtag> hashtagsForPost = jdbc.query(SELECT_HASHTAGS_FOR_POST, new HashtagMapper(), postId);
        for (Hashtag hashtag : hashtagsForPost) {
            hashtag.setNumberOfPosts(getNumberOfPostsForHashtag(hashtag.getHashtagId()));
        }
        return hashtagsForPost;
    }

    @Override
    public List<Hashtag> getAllHashtagsNotUsedInPost(int postId) {
        final String SELECT_HASHTAGS_NOT_IN_POST = "SELECT h.* FROM hashtag h "
                + "WHERE h.hashtagId NOT IN "
                + "(SELECT h.hashtagId FROM hashtag h "
                + "JOIN posthashtag ph ON h.hashtagId = ph.hashtagId "
                + "WHERE ph.postId = ?)";
        List<Hashtag> hashtagsNotInPost = jdbc.query(SELECT_HASHTAGS_NOT_IN_POST, new HashtagMapper(), postId);

        for (Hashtag hashtag : hashtagsNotInPost) {
            hashtag.setNumberOfPosts(getNumberOfPostsForHashtag(hashtag.getHashtagId()));
        }

        return hashtagsNotInPost;

    }

    @Override
    @Transactional
    public void updateHashtag(Hashtag hashtag) {
        final String UPDATE_HASHTAG = "UPDATE Hashtag SET "
                + "title = ? "
                + "WHERE hashtagId = ?";
        jdbc.update(UPDATE_HASHTAG,
                hashtag.getTitle(),
                hashtag.getHashtagId());

    }

    @Override
    @Transactional
    public void deleteHashtag(int hashtagId) {

        final String DELETE_FROM_POSTHASHTAG = "DELETE FROM PostHashtag "
                + "WHERE hashtagId = ?";

        jdbc.update(DELETE_FROM_POSTHASHTAG, hashtagId);

        final String DELETE_HASHTAG = "DELETE FROM Hashtag "
                + "WHERE hashtagId = ?";

        jdbc.update(DELETE_HASHTAG, hashtagId);

    }

    @Override
    @Transactional
    public Hashtag createHashtag(Hashtag hashtag) {
        final String INSERT_HASHTAG = "INSERT INTO Hashtag(title) "
                + "VALUES (?)";
        jdbc.update(INSERT_HASHTAG,
                hashtag.getTitle());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hashtag.setHashtagId(newId);

        hashtag.setNumberOfPosts(getNumberOfPostsForHashtag(hashtag.getHashtagId()));

        return hashtag;

    }

    //Helper Methods
    private int getNumberOfPostsForHashtag(int hashtagId) {

        final String SELECT_POSTS_FOR_HASHTAG = "SELECT p.* FROM Hashtag h "
                + "JOIN postHashtag ph ON h.hashtagId = ph.hashtagId "
                + "JOIN post p ON p.postId = ph.postId "
                + "WHERE h.hashtagId = ?";
        List<Post> listOfPosts = jdbc.query(SELECT_POSTS_FOR_HASHTAG, new PostMapper(), hashtagId);

        int numberOfPosts = listOfPosts.size();

        return numberOfPosts;
    }

    public static final class HashtagMapper implements RowMapper<Hashtag> {

        @Override
        public Hashtag mapRow(ResultSet rs, int i) throws SQLException {
            Hashtag hashtag = new Hashtag();
            hashtag.setHashtagId(rs.getInt("hashtagId"));
            hashtag.setTitle(rs.getString("title"));
            return hashtag;
        }
    }
}
