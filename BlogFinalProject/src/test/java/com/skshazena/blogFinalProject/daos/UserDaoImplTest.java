package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.time.LocalDateTime;
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
public class UserDaoImplTest {

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

    public UserDaoImplTest() {

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
     * Test of getUserById method, of class UserDaoImpl.
     */
    @Test
    public void testAddGetUserById() {
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

        User userById = userDao.getUserById(user.getUserId());

        assertEquals(user, userById);

    }

    /**
     * Test of getUserByUsername method, of class UserDaoImpl.
     */
    @Test
    public void testAddGetUserByUsername() {
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

        User userByUsername = userDao.getUserByUsername(user.getUsername());

        assertEquals(user, userByUsername);
    }

    /**
     * Test of getAllUsers method, of class UserDaoImpl.
     */
    @Test
    public void testGetAllUsers() {
        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Role role2 = new Role();
        role2.setRole("firstRole");

        role2 = roleDao.createRole(role2);

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

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);
        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(false);
        user2.setLastLogin(LocalDateTime.now().minusHours(4));
        user2.setUsername("secondUsername");
        user2.setPassword("passwordTheSecond");
        user2.setProfilePicture("profilePicturePathTheSecond");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        List<User> allUsers = userDao.getAllUsers();
        assertEquals(allUsers.size(), 2);
        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));

    }

    /**
     * Test of updateUser method, of class UserDaoImpl.
     */
    @Test
    public void testUpdateUser() {

        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Set<Role> roles = new HashSet<Role>();
        roles.add(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        role2 = roleDao.createRole(role2);

        Set<Role> roles2 = new HashSet<Role>();
        roles.add(role2);

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

        User userById = userDao.getUserById(user.getUserId());
        assertEquals(user, userById);

        user.setFirstName("firstName2");
        user.setLastName("lastName2");
        user.setEnabled(false);
        user.setLastLogin(LocalDateTime.now().minusHours(4));
        user.setUsername("secondUsername");
        user.setPassword("passwordTheSecond");
        user.setProfilePicture("profilePicturePathTheSecond");
        user.setRoles(roles2);

        userDao.updateUser(user);

        User updatedUser = userDao.getUserById(user.getUserId());

        assertEquals(updatedUser, user);
        assertNotEquals(userById, user);

    }

    /**
     * Test of deleteUser method, of class UserDaoImpl.
     */
    @Test
    public void testDeleteUser() {

        Role role = new Role();
        role.setRole("firstRole");

        role = roleDao.createRole(role);

        Role role2 = new Role();
        role2.setRole("firstRole");

        role2 = roleDao.createRole(role2);

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

        Set<Role> roles2 = new HashSet<Role>();
        roles2.add(role2);
        User user2 = new User();
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setEnabled(false);
        user2.setLastLogin(LocalDateTime.now().minusHours(4));
        user2.setUsername("secondUsername");
        user2.setPassword("passwordTheSecond");
        user2.setProfilePicture("profilePicturePathTheSecond");
        user2.setRoles(roles2);

        user2 = userDao.createUser(user2);

        List<User> allUsers = userDao.getAllUsers();
        assertEquals(allUsers.size(), 2);
        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));

        userDao.deleteUser(user.getUserId());

        allUsers = userDao.getAllUsers();
        assertEquals(allUsers.size(), 1);
        assertFalse(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));

        userDao.deleteUser(user2.getUserId());

        allUsers = userDao.getAllUsers();
        assertEquals(allUsers.size(), 0);
        assertFalse(allUsers.contains(user));
        assertFalse(allUsers.contains(user2));
    }

}
