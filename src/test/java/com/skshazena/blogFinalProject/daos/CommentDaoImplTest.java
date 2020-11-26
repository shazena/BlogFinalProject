package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class CommentDaoImplTest {

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

    Role role;
    Role role2;
    User user;
    User user2;
    Post post;
    Post post2;
    Hashtag hashtag;
    Hashtag hashtag2;

    public CommentDaoImplTest() {
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

        role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

        user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEnabled(true);
        user.setLastLogin(LocalDateTime.now().minusHours(3).withNano(0));
        user.setUsername("firstUsername");
        user.setPassword("password");
        user.setProfilePicture("profilePicturePath");
        user.setRoles(roles);

        user = userDao.createUser(user);

        user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1).withNano(0));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        post.setPostAt(LocalDateTime.now().minusDays(2).withNano(0));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
        post.setLastEditedAt(LocalDateTime.now().minusHours(3).withNano(0));
        post.setContent("This is the content of my post");
        post.setApprovalStatus(true);
        post.setStaticPage(false);
        post.setTitlePhoto("photoFileName");
        post.setUser(user);

        hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = new ArrayList<Hashtag>();
        hashtags.add(hashtag);

        post.setHashtagsForPost(hashtags);
        post = postDao.createPost(post);

        post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9).withNano(0));
        post2.setPostAt(LocalDateTime.now().minusDays(9).withNano(0));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7).withNano(0));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user2);

        hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

    }

    /**
     * Test of getCommentById method, of class CommentDaoImpl.
     */
    @Test
    public void testAddGetCommentById() {

        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(false);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment commentById = commentDao.getCommentById(comment.getCommentId());
        assertEquals(comment, commentById);
    }

    /**
     * Test of getAllComments method, of class CommentDaoImpl.
     */
    @Test
    public void testGetAllComments() {

        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(false);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTitle("commentTitle2");
        comment2.setContent("This is also the comment content");
        comment2.setCreatedAt(LocalDateTime.now().minusDays(3).withNano(0));
        comment2.setApprovalStatus(true);
        comment2.setPost(post2);
        comment2.setUser(user2);

        comment2 = commentDao.createComment(comment2);

        List<Comment> allComments = commentDao.getAllComments();
        assertEquals(allComments.size(), 2);
        assertTrue(allComments.contains(comment));
        assertTrue(allComments.contains(comment2));

    }

    /**
     * Test of getAllCommentsForPost method, of class CommentDaoImpl.
     */
    @Test
    public void testGetAllCommentsForPost() {

        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(true);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTitle("commentTitle2");
        comment2.setContent("This is also the comment content");
        comment2.setCreatedAt(LocalDateTime.now().minusDays(3).withNano(0));
        comment2.setApprovalStatus(false);
        comment2.setPost(post2);
        comment2.setUser(user2);

        comment2 = commentDao.createComment(comment2);

        List<Comment> allCommentsForPost = commentDao.getAllCommentsForPost(post.getPostId());
        assertEquals(allCommentsForPost.size(), 1);
        assertTrue(allCommentsForPost.contains(comment));
        assertFalse(allCommentsForPost.contains(comment2));

    }

    /**
     * Test of getAllCommentsWrittenByUser method, of class CommentDaoImpl.
     */
    @Test
    public void testGetAllCommentsWrittenByUser() {
        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(false);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTitle("commentTitle2");
        comment2.setContent("This is also the comment content");
        comment2.setCreatedAt(LocalDateTime.now().minusDays(3).withNano(0));
        comment2.setApprovalStatus(false);
        comment2.setPost(post2);
        comment2.setUser(user2);

        comment2 = commentDao.createComment(comment2);

        List<Comment> allCommentsWrittenByUser = commentDao.getAllCommentsWrittenByUser(user.getUserId());
        assertEquals(allCommentsWrittenByUser.size(), 1);
        assertTrue(allCommentsWrittenByUser.contains(comment));
        assertFalse(allCommentsWrittenByUser.contains(comment2));
    }

    /**
     * Test of getAllCommentsNeedingApproval method, of class CommentDaoImpl.
     */
    @Test
    public void testGetAllCommentsNeedingApproval() {
        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(true);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTitle("commentTitle2");
        comment2.setContent("This is also the comment content");
        comment2.setCreatedAt(LocalDateTime.now().minusDays(3).withNano(0));
        comment2.setApprovalStatus(false);
        comment2.setPost(post2);
        comment2.setUser(user2);

        comment2 = commentDao.createComment(comment2);

        List<Comment> allCommentsNeedingApproval = commentDao.getAllCommentsNeedingApproval();
        assertEquals(allCommentsNeedingApproval.size(), 1);
        assertFalse(allCommentsNeedingApproval.contains(comment));
        assertTrue(allCommentsNeedingApproval.contains(comment2));
    }

    /**
     * Test of updateComment method, of class CommentDaoImpl.
     */
    @Test
    public void testUpdateComment() {

        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(true);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment createdComment = commentDao.getCommentById(comment.getCommentId());

        comment.setTitle("commentTitle2");
        comment.setContent("This is also the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(3).withNano(0));
        comment.setApprovalStatus(false);
        comment.setPost(post2);
        comment.setUser(user2);

        assertNotEquals(comment, createdComment);

        commentDao.updateComment(comment);

        Comment updatedComment = commentDao.getCommentById(comment.getCommentId());

        assertEquals(comment, updatedComment);
        assertNotEquals(comment, createdComment);

    }

    /**
     * Test of deleteComment method, of class CommentDaoImpl.
     */
    @Test
    public void testDeleteComment() {

        Comment comment = new Comment();
        comment.setTitle("commentTitle");
        comment.setContent("This is the comment content");
        comment.setCreatedAt(LocalDateTime.now().minusDays(2).withNano(0));
        comment.setApprovalStatus(false);
        comment.setPost(post);
        comment.setUser(user);

        comment = commentDao.createComment(comment);

        Comment comment2 = new Comment();
        comment2.setTitle("commentTitle2");
        comment2.setContent("This is also the comment content");
        comment2.setCreatedAt(LocalDateTime.now().minusDays(3).withNano(0));
        comment2.setApprovalStatus(true);
        comment2.setPost(post2);
        comment2.setUser(user2);

        comment2 = commentDao.createComment(comment2);

        List<Comment> allComments = commentDao.getAllComments();
        assertEquals(allComments.size(), 2);
        assertTrue(allComments.contains(comment));
        assertTrue(allComments.contains(comment2));

        commentDao.deleteComment(comment.getCommentId());

        allComments = commentDao.getAllComments();
        assertEquals(allComments.size(), 1);
        assertFalse(allComments.contains(comment));
        assertTrue(allComments.contains(comment2));

        commentDao.deleteComment(comment2.getCommentId());

        allComments = commentDao.getAllComments();
        assertEquals(allComments.size(), 0);
        assertFalse(allComments.contains(comment));
        assertFalse(allComments.contains(comment2));

    }

}
