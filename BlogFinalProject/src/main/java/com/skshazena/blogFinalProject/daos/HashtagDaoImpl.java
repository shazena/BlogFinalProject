package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.daos.PostDaoImpl.PostMapper;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
public class HashtagDaoImpl implements HashtagDao {

    @Autowired
    JdbcTemplate jdbc;

    //Helper Methods
    private int getNumberOfPostsForHashtag(int hashtagId) {

        final String SELECT_POSTS_FOR_HASHTAG = "SELECT p.* FROM Hashtag h "
                + "JOIN postHashtag ph ON h.hashtagId = ph.hashtagId "
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
