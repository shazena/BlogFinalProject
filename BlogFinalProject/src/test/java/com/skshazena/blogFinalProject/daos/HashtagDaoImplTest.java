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
public class HashtagDaoImplTest {

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

    public HashtagDaoImplTest() {
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

        List<Post> allPosts = postDao.getAllPosts();
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
     * Test of getHashtagById method, of class HashtagDaoImpl.
     */
    @Test
    public void testAddGetHashtagByIdWithPost() {

        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEnabled(true);
        user.setLastLogin(LocalDateTime.now().minusHours(3));
        user.setUsername("firstUsername");
        user.setPassword("password");
        user.setProfilePicture("profilePicturePath");
        user.setRoles(roles);

        user = userDao.createUser(user);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5));
        post.setLastEditedAt(LocalDateTime.now().minusHours(3));
        post.setContent("This is the content of my post");
        post.setApprovalStatus(true);
        post.setStaticPage(false);
        post.setTitlePhoto("photoFileName");
        post.setUser(user);

        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        List<Hashtag> hashtags = new ArrayList<Hashtag>();
        hashtags.add(hashtag);

        post.setHashtagsForPost(hashtags);

        Hashtag hashtagById = hashtagDao.getHashtagById(hashtag.getHashtagId());
        assertEquals(hashtag.getHashtagId(), hashtagById.getHashtagId());
        assertEquals(hashtag.getTitle(), hashtagById.getTitle());
        assertEquals(hashtag.getHashtagId(), hashtagById.getHashtagId());

    }

    /**
     * Test of getHashtagById method, of class HashtagDaoImpl.
     */
    @Test
    public void testAddGetHashtagById() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtagById = hashtagDao.getHashtagById(hashtag.getHashtagId());
        assertEquals(hashtag, hashtagById);
    }

    /**
     * Test of getHashtagByTitle method, of class HashtagDaoImpl.
     */
    @Test
    public void testAddGetHashtagByTitle() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtagByTitle = hashtagDao.getHashtagByTitle(hashtag.getTitle());
        assertEquals(hashtag, hashtagByTitle);
    }

    /**
     * Test of getAllHashtags method, of class HashtagDaoImpl.
     */
    @Test
    public void testGetAllHashtags() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> allHashtags = hashtagDao.getAllHashtags();
        assertEquals(allHashtags.size(), 2);
        assertTrue(allHashtags.contains(hashtag));
        assertTrue(allHashtags.contains(hashtag2));
    }

    /**
     * Test of getAllHashtagsForPost method, of class HashtagDaoImpl.
     */
    @Test
    public void testGetAllHashtagsForPost() {

        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEnabled(true);
        user.setLastLogin(LocalDateTime.now().minusHours(3));
        user.setUsername("firstUsername");
        user.setPassword("password");
        user.setProfilePicture("profilePicturePath");
        user.setRoles(roles);

        user = userDao.createUser(user);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5));
        post.setLastEditedAt(LocalDateTime.now().minusHours(3));
        post.setContent("This is the content of my post");
        post.setApprovalStatus(true);
        post.setStaticPage(false);
        post.setTitlePhoto("photoFileName");
        post.setUser(user);

        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setTitle("myThirdHashtag");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        List<Hashtag> hashtags = new ArrayList<Hashtag>();
        hashtags.add(hashtag);
        hashtags.add(hashtag2);

        post.setHashtagsForPost(hashtags);

        post = postDao.createPost(post);

        hashtag = hashtagDao.getHashtagById(hashtag.getHashtagId());
        hashtag2 = hashtagDao.getHashtagById(hashtag2.getHashtagId());
        hashtag3 = hashtagDao.getHashtagById(hashtag3.getHashtagId());

        List<Hashtag> allHashtagsForPost = hashtagDao.getAllHashtagsForPost(post.getPostId());

        assertEquals(allHashtagsForPost.size(), 2);
        assertTrue(allHashtagsForPost.contains(hashtag));
        assertTrue(allHashtagsForPost.contains(hashtag2));
        assertFalse(allHashtagsForPost.contains(hashtag3));

    }

    /**
     * Test of getAllHashtagsNotUsedInPost method, of class HashtagDaoImpl.
     */
    @Test
    public void testGetAllHashtagsNotUsedInPost() {

        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEnabled(true);
        user.setLastLogin(LocalDateTime.now().minusHours(3));
        user.setUsername("firstUsername");
        user.setPassword("password");
        user.setProfilePicture("profilePicturePath");
        user.setRoles(roles);

        user = userDao.createUser(user);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5));
        post.setLastEditedAt(LocalDateTime.now().minusHours(3));
        post.setContent("This is the content of my post");
        post.setApprovalStatus(true);
        post.setStaticPage(false);
        post.setTitlePhoto("photoFileName");
        post.setUser(user);

        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setTitle("myThirdHashtag");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        List<Hashtag> hashtags = new ArrayList<Hashtag>();
        hashtags.add(hashtag);
        hashtags.add(hashtag2);

        post.setHashtagsForPost(hashtags);

        post = postDao.createPost(post);

        hashtag = hashtagDao.getHashtagById(hashtag.getHashtagId());
        hashtag2 = hashtagDao.getHashtagById(hashtag2.getHashtagId());
        hashtag3 = hashtagDao.getHashtagById(hashtag3.getHashtagId());

        List<Hashtag> allHashtagsNotUsedInPost = hashtagDao.getAllHashtagsNotUsedInPost(post.getPostId());

        assertEquals(allHashtagsNotUsedInPost.size(), 1);
        assertFalse(allHashtagsNotUsedInPost.contains(hashtag));
        assertFalse(allHashtagsNotUsedInPost.contains(hashtag2));
        assertTrue(allHashtagsNotUsedInPost.contains(hashtag3));
    }

    /**
     * Test of updateHashtag method, of class HashtagDaoImpl.
     */
    @Test
    public void testUpdateHashtag() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        Hashtag createdHashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtagById = hashtagDao.getHashtagById(hashtag.getHashtagId());

        assertEquals(hashtagById, createdHashtag);

        createdHashtag.setTitle("mySecondHashtag");

        assertNotEquals(createdHashtag, hashtagById);

        hashtagDao.updateHashtag(hashtag);

        Hashtag updatedHashtag = hashtagDao.getHashtagById(createdHashtag.getHashtagId());

        assertEquals(createdHashtag, updatedHashtag);

    }

    /**
     * Test of deleteHashtag method, of class HashtagDaoImpl.
     */
    @Test
    public void testDeleteHashtag() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTitle("myFirstHashtag");
        hashtag = hashtagDao.createHashtag(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> allHashtags = hashtagDao.getAllHashtags();
        assertEquals(allHashtags.size(), 2);
        assertTrue(allHashtags.contains(hashtag));
        assertTrue(allHashtags.contains(hashtag2));

        hashtagDao.deleteHashtag(hashtag.getHashtagId());

        allHashtags = hashtagDao.getAllHashtags();
        assertEquals(allHashtags.size(), 1);
        assertFalse(allHashtags.contains(hashtag));
        assertTrue(allHashtags.contains(hashtag2));

        hashtagDao.deleteHashtag(hashtag2.getHashtagId());

        allHashtags = hashtagDao.getAllHashtags();
        assertEquals(allHashtags.size(), 0);
        assertFalse(allHashtags.contains(hashtag));
        assertFalse(allHashtags.contains(hashtag2));

    }

}
