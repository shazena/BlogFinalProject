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
    public void testAddGetPostById() {
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
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post postById = postDao.getPostById(post.getPostId());
        assertEquals(post, postById);
    }

    /**
     * Test of getAllPostsForAdminNewestFirst method, of class PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForAdminNewestFirst() {

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
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());

        List<Post> allPostsForAdminNewestFirst = postDao.getAllPostsForAdminNewestFirst();

        assertEquals(allPostsForAdminNewestFirst.size(), 2);

        assertTrue(allPostsForAdminNewestFirst.contains(postById));
        assertTrue(allPostsForAdminNewestFirst.contains(postById2));

        assertTrue(allPostsForAdminNewestFirst.indexOf(postById) == 0);
        assertTrue(allPostsForAdminNewestFirst.indexOf(postById2) == 1);
    }

    /**
     * Test of getAllPostsForBlogNonStaticNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForBlogNonStaticNewestFirst() {
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
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());

        List<Post> allPostsForBlogNonStaticNewestFirst = postDao.getAllPostsForBlogNonStaticNewestFirst();
        assertEquals(allPostsForBlogNonStaticNewestFirst.size(), 1);
        assertTrue(allPostsForBlogNonStaticNewestFirst.contains(postById));
        assertFalse(allPostsForBlogNonStaticNewestFirst.contains(postById2));
    }

    /**
     * Test of getAllPostsForBlogThatAreStaticNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForBlogThatAreStaticNewestFirst() {
        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post post3 = new Post();
        post3.setTitle("thirdTitle");
        post3.setCreatedAt(LocalDateTime.now().minusDays(15));
        post3.setPostAt(LocalDateTime.now().minusDays(16));
        post3.setExpireAt(null);
        post3.setLastEditedAt(LocalDateTime.now().minusDays(2));
        post3.setContent("This is the content of my post yet again. Again.");
        post3.setApprovalStatus(true);
        post3.setStaticPage(true);
        post3.setTitlePhoto("photoFileName3");
        post3.setUser(user2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setTitle("myThirdHashtag");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        List<Hashtag> hashtags3 = new ArrayList<Hashtag>();
        hashtags3.add(hashtag3);

        post3.setHashtagsForPost(hashtags3);
        post3 = postDao.createPost(post3);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());
        Post postById3 = postDao.getPostById(post3.getPostId());

        List<Post> allPostsForBlogThatAreStaticNewestFirst = postDao.getAllPostsForBlogThatAreStaticNewestFirst();
        assertEquals(allPostsForBlogThatAreStaticNewestFirst.size(), 1);
        assertFalse(allPostsForBlogThatAreStaticNewestFirst.contains(postById));
        assertFalse(allPostsForBlogThatAreStaticNewestFirst.contains(postById2));
        assertTrue(allPostsForBlogThatAreStaticNewestFirst.contains(postById3));
    }

    /**
     * Test of getAllPostsForHashtagForAdminNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsForHashtagForAdminNewestFirst() {
        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post post3 = new Post();
        post3.setTitle("thirdTitle");
        post3.setCreatedAt(LocalDateTime.now().minusDays(15));
        post3.setPostAt(LocalDateTime.now().minusDays(16));
        post3.setExpireAt(null);
        post3.setLastEditedAt(LocalDateTime.now().minusDays(2));
        post3.setContent("This is the content of my post yet again. Again.");
        post3.setApprovalStatus(true);
        post3.setStaticPage(true);
        post3.setTitlePhoto("photoFileName3");
        post3.setUser(user2);

        post3.setHashtagsForPost(hashtags2);
        post3 = postDao.createPost(post3);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());
        Post postById3 = postDao.getPostById(post3.getPostId());

        List<Post> allPostsForHashtagForAdminNewestFirst = postDao.getAllPostsForHashtagForAdminNewestFirst(hashtag2.getHashtagId());

        assertEquals(allPostsForHashtagForAdminNewestFirst.size(), 2);
        assertFalse(allPostsForHashtagForAdminNewestFirst.contains(postById));
        assertTrue(allPostsForHashtagForAdminNewestFirst.contains(postById2));
        assertTrue(allPostsForHashtagForAdminNewestFirst.contains(postById3));
    }

    /**
     * Test of getAllPostsForBlogForHashtagNewestFirst method, of class
 PostDaoImpl.
     */
    @Test
    public void testGetestGetAllPostsForBlogForHashtagNewestFirst       Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post post3 = new Post();
        post3.setTitle("thirdTitle");
        post3.setCreatedAt(LocalDateTime.now().minusDays(15));
        post3.setPostAt(LocalDateTime.now().minusDays(16));
        post3.setExpireAt(null);
        post3.setLastEditedAt(LocalDateTime.now().minusDays(2));
        post3.setContent("This is the content of my post yet again. Again.");
        post3.setApprovalStatus(true);
        post3.setStaticPage(true);
        post3.setTitlePhoto("photoFileName3");
        post3.setUser(user2);

        post3.setHashtagsForPost(hashtags2);
        post3 = postDao.createPost(post3);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());
        Post postById3 = postDao.getPostById(post3.getPostId());

        List<Post> allPostsForHashtagForUserNewestFirst = postDao.getAllPostsForBlogForHashtagNewestFirst(hashtag2.getHashtagId());
        assertEquals(allPostsForHashtagForUserNewestFirst.size(), 1);
        assertFalse(allPostsForHashtagForUserNewestFirst.contains(postById));
        assertFalse(allPostsForHashtagForUserNewestFirst.contains(postById2));
        assertTrue(allPostsForHashtagForUserNewestFirst.contains(postById3));
    }

    /**
     * Test of getAllPostsNeedingApprovalWrittenByCreatorOldestFirst method, of
     * class PostDaoImpl.
     */
    @Test
    public void testGetAllPostsNeedingApprovalWrittenByCreatorOldestFirst() {
        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user2);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post post3 = new Post();
        post3.setTitle("thirdTitle");
        post3.setCreatedAt(LocalDateTime.now().minusDays(15));
        post3.setPostAt(LocalDateTime.now().minusDays(16));
        post3.setExpireAt(null);
        post3.setLastEditedAt(LocalDateTime.now().minusDays(2));
        post3.setContent("This is the content of my post yet again. Again.");
        post3.setApprovalStatus(true);
        post3.setStaticPage(true);
        post3.setTitlePhoto("photoFileName3");
        post3.setUser(user2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setTitle("myThirdHashtag");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        List<Hashtag> hashtags3 = new ArrayList<Hashtag>();
        hashtags3.add(hashtag3);

        post3.setHashtagsForPost(hashtags3);
        post3 = postDao.createPost(post3);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());
        Post postById3 = postDao.getPostById(post3.getPostId());

        List<Post> allPostsNeedingApprovalWrittenByCreatorOldestFirst = postDao.getAllPostsNeedingApprovalWrittenByCreatorOldestFirst(user2.getUserId());

        assertEquals(allPostsNeedingApprovalWrittenByCreatorOldestFirst.size(), 1);
        assertFalse(allPostsNeedingApprovalWrittenByCreatorOldestFirst.contains(postById));
        assertTrue(allPostsNeedingApprovalWrittenByCreatorOldestFirst.contains(postById2));
        assertFalse(allPostsNeedingApprovalWrittenByCreatorOldestFirst.contains(postById3));
    }

    /**
     * Test of getAllPostsWrittenByCreatorNewestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsWrittenByCreatorNewestFirst() {
        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user2);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post post3 = new Post();
        post3.setTitle("thirdTitle");
        post3.setCreatedAt(LocalDateTime.now().minusDays(15));
        post3.setPostAt(LocalDateTime.now().minusDays(16));
        post3.setExpireAt(null);
        post3.setLastEditedAt(LocalDateTime.now().minusDays(2));
        post3.setContent("This is the content of my post yet again. Again.");
        post3.setApprovalStatus(true);
        post3.setStaticPage(true);
        post3.setTitlePhoto("photoFileName3");
        post3.setUser(user2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setTitle("myThirdHashtag");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        List<Hashtag> hashtags3 = new ArrayList<Hashtag>();
        hashtags3.add(hashtag3);

        post3.setHashtagsForPost(hashtags3);
        post3 = postDao.createPost(post3);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());
        Post postById3 = postDao.getPostById(post3.getPostId());

        List<Post> allPostsWrittenByCreatorNewestFirst = postDao.getAllPostsWrittenByCreatorNewestFirst(user2.getUserId());

        assertEquals(allPostsWrittenByCreatorNewestFirst.size(), 2);
        assertFalse(allPostsWrittenByCreatorNewestFirst.contains(postById));
        assertTrue(allPostsWrittenByCreatorNewestFirst.contains(postById2));
        assertTrue(allPostsWrittenByCreatorNewestFirst.contains(postById3));
    }

    /**
     * Test of getAllPostsNeedingApprovalForAdminOldestFirst method, of class
     * PostDaoImpl.
     */
    @Test
    public void testGetAllPostsNeedingApprovalForAdminOldestFirst() {
        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user2);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post post3 = new Post();
        post3.setTitle("thirdTitle");
        post3.setCreatedAt(LocalDateTime.now().minusDays(15));
        post3.setPostAt(LocalDateTime.now().minusDays(16));
        post3.setExpireAt(null);
        post3.setLastEditedAt(LocalDateTime.now().minusDays(2));
        post3.setContent("This is the content of my post yet again. Again.");
        post3.setApprovalStatus(false);
        post3.setStaticPage(true);
        post3.setTitlePhoto("photoFileName3");
        post3.setUser(user2);

        Hashtag hashtag3 = new Hashtag();
        hashtag3.setTitle("myThirdHashtag");
        hashtag3 = hashtagDao.createHashtag(hashtag3);

        List<Hashtag> hashtags3 = new ArrayList<Hashtag>();
        hashtags3.add(hashtag3);

        post3.setHashtagsForPost(hashtags3);
        post3 = postDao.createPost(post3);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());
        Post postById3 = postDao.getPostById(post3.getPostId());

        List<Post> allPostsNeedingApprovalForAdminOldestFirst = postDao.getAllPostsNeedingApprovalForAdminOldestFirst();

        assertEquals(allPostsNeedingApprovalForAdminOldestFirst.size(), 2);

        assertFalse(allPostsNeedingApprovalForAdminOldestFirst.contains(postById));
        assertTrue(allPostsNeedingApprovalForAdminOldestFirst.contains(postById2));
        assertTrue(allPostsNeedingApprovalForAdminOldestFirst.contains(postById3));

        assertTrue(allPostsNeedingApprovalForAdminOldestFirst.indexOf(postById3) == 0);
        assertTrue(allPostsNeedingApprovalForAdminOldestFirst.indexOf(postById2) == 1);
    }

    /**
     * Test of updatePost method, of class PostDaoImpl.
     */
    @Test
    public void testUpdatePost() {

        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");
        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);

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

        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(true);
        user2.setLastLogin(LocalDateTime.now().minusHours(1));
        user2.setUsername("secondUsername");
        user2.setPassword("password2");
        user2.setProfilePicture("profilePicturePath2");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);
        Post createdPost = postDao.getPostById(post.getPostId());

        post.setTitle("secondTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(9));
        post.setPostAt(LocalDateTime.now().minusDays(9));
        post.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post.setContent("This is the content of my post yet again");
        post.setApprovalStatus(false);
        post.setStaticPage(true);
        post.setTitlePhoto("photoFileName2");
        post.setUser(user2);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post.setHashtagsForPost(hashtags2);

        assertNotEquals(createdPost, post);

        postDao.updatePost(post);

        Post updatedPost = postDao.getPostById(post.getPostId());
        assertEquals(updatedPost, post);

    }

    /**
     * Test of deletePost method, of class PostDaoImpl.
     */
    @Test
    public void testDeletePost() {

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
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
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
        post = postDao.createPost(post);

        Post post2 = new Post();
        post2.setTitle("secondTitle");
        post2.setCreatedAt(LocalDateTime.now().minusDays(9));
        post2.setPostAt(LocalDateTime.now().minusDays(9));
        post2.setExpireAt(LocalDateTime.now().minusDays(6).withNano(0));
        post2.setLastEditedAt(LocalDateTime.now().minusDays(7));
        post2.setContent("This is the content of my post yet again");
        post2.setApprovalStatus(false);
        post2.setStaticPage(true);
        post2.setTitlePhoto("photoFileName2");
        post2.setUser(user);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTitle("mySecondHashtag");
        hashtag2 = hashtagDao.createHashtag(hashtag2);

        List<Hashtag> hashtags2 = new ArrayList<Hashtag>();
        hashtags2.add(hashtag2);

        post2.setHashtagsForPost(hashtags2);
        post2 = postDao.createPost(post2);

        Post postById = postDao.getPostById(post.getPostId());
        Post postById2 = postDao.getPostById(post2.getPostId());

        List<Post> allPostsForAdminNewestFirst = postDao.getAllPostsForAdminNewestFirst();

        assertEquals(allPostsForAdminNewestFirst.size(), 2);

        assertTrue(allPostsForAdminNewestFirst.contains(postById));
        assertTrue(allPostsForAdminNewestFirst.contains(postById2));

        postDao.deletePost(post.getPostId());

        allPostsForAdminNewestFirst = postDao.getAllPostsForAdminNewestFirst();

        assertEquals(allPostsForAdminNewestFirst.size(), 1);

        assertFalse(allPostsForAdminNewestFirst.contains(postById));
        assertTrue(allPostsForAdminNewestFirst.contains(postById2));

        postDao.deletePost(post2.getPostId());

        allPostsForAdminNewestFirst = postDao.getAllPostsForAdminNewestFirst();

        assertEquals(allPostsForAdminNewestFirst.size(), 0);

        assertFalse(allPostsForAdminNewestFirst.contains(postById));
        assertFalse(allPostsForAdminNewestFirst.contains(postById2));
    }

}
