package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Shazena
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostDaoImplTest {

    @Autowired
    CommentDao commentDao;

    @Autowired
    HashtagDao hashtagDao;

    @Autowired
    PostDao postDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    public PostDaoImplTest() {
    }

    @BeforeEach
    public void setUpClass() {
        List<Comment> allComments = commentDao.getAllComments();
        for (Comment comment : allComments) {
            commentDao.deleteComment(comment.getCommentId());
        }

        List<Hashtag> allHashtags = hashtagDao.getAllHashtags();
        for (Hashtag hashtag : allHashtags) {
            hashtagDao.deleteHashtag(hashtag.getHashtagId());
        }

        List<Post> allPosts = postDao.getAllPostsForAdminNewestFirst();
        for (Post post : allPosts) {
            postDao.deletePost(post.getPostId());
        }

        List<Role> allRoles = roleDao.getAllRoles();
        for (Role role : allRoles) {
            roleDao.deleteRole(role.getRoleId());
        }

        List<User> allUsers = userDao.getAllUsers();
        for (User user : allUsers) {
            userDao.deleteUser(user.getUserId());
        }
    }

    /**
     * Test of getPostById method, of class PostDaoImpl.
     */
    @Test
    public void testGetPostById() {
    }

    /**
     * Test of getAllPostsForAdminNewestFirst method, of class PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForAdminNewestFirst() {
    }

    /**
     * Test of getAllPostsForBlogNonStaticNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForBlogNonStaticNewestFirst() {
    }

    /**
     * Test of getAllPostsForBlogThatAreStaticNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForBlogThatAreStaticNewestFirst() {
    }

    /**
     * Test of getAllPostsForHashtagForAdminNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForHashtagForAdminNewestFirst() {
    }

    /**
     * Test of getAllPostsForHashtagForUserNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForHashtagForUserNewestFirst() {
    }

    /**
     * Test of getAllPostsNeedingApprovalWrittenByUserOldestFirst method, of
     * class PostDaoImpl.
     */
    @Test
    public void testGetAllPostsNeedingApprovalWrittenByUserOldestFirst() {
    }

    /**
     * Test of getAllPostsWrittenByCreatorNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsWrittenByCreatorNewestFirst() {
    }

    /**
     * Test of getAllPostsNeedingApprovalForAdminOldestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsNeedingApprovalForAdminOldestFirst() {
    }

    /**
     * Test of updatePost method, of class PostDaoImpl.
     */
    @Test
    public void testUpdatePost() {
    }

    /**
     * Test of deletePost method, of class PostDaoImpl.
     */
    @Test
    public void testDeletePost() {
    }

    /**
     * Test of createPost method, of class PostDaoImpl.
     */
    @Test
    public void testCreatePost() {
    }

}
