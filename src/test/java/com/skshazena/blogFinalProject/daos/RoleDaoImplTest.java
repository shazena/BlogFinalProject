package com.skshazena.blogFinalProject.daos;

import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import java.util.List;
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
public class RoleDaoImplTest {

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

    public RoleDaoImplTest() {
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
     * Test of getRoleById method, of class RoleDaoImpl.
     */
    @Test
    public void testAddGetRoleById() {
        Role role = new Role();
        role.setRole("firstRole");

        Role createdRole = roleDao.createRole(role);

        Role roleById = roleDao.getRoleById(createdRole.getRoleId());
        assertEquals(createdRole, roleById);
    }

    /**
     * Test of getRoleByRole method, of class RoleDaoImpl.
     */
    @Test
    public void testGetRoleByRole() {
        Role role = new Role();
        role.setRole("firstRole");

        Role createdRole = roleDao.createRole(role);

        Role roleByRole = roleDao.getRoleByRole(createdRole.getRole());
        assertEquals(createdRole, roleByRole);
    }

    /**
     * Test of getAllRoles method, of class RoleDaoImpl.
     */
    @Test
    public void testGetAllRoles() {
        Role role = new Role();
        role.setRole("firstRole");

        Role createdRole = roleDao.createRole(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        Role createdRole2 = roleDao.createRole(role2);

        List<Role> allRoles = roleDao.getAllRoles();
        assertEquals(allRoles.size(), 2);
        assertTrue(allRoles.contains(role));
        assertTrue(allRoles.contains(role2));
    }

    /**
     * Test of deleteRole method, of class RoleDaoImpl.
     */
    @Test
    public void testDeleteRole() {
        Role role = new Role();
        role.setRole("firstRole");

        Role createdRole = roleDao.createRole(role);

        Role role2 = new Role();
        role2.setRole("secondRole");

        Role createdRole2 = roleDao.createRole(role2);

        List<Role> allRoles = roleDao.getAllRoles();
        assertEquals(allRoles.size(), 2);
        assertTrue(allRoles.contains(role));
        assertTrue(allRoles.contains(role2));

        roleDao.deleteRole(role.getRoleId());

        List<Role> allRoles2 = roleDao.getAllRoles();
        assertEquals(allRoles2.size(), 1);
        assertFalse(allRoles2.contains(role));
        assertTrue(allRoles2.contains(role2));

        roleDao.deleteRole(role2.getRoleId());

        List<Role> allRoles3 = roleDao.getAllRoles();
        assertEquals(allRoles3.size(), 0);
        assertFalse(allRoles3.contains(role));
        assertFalse(allRoles3.contains(role2));

    }

    /**
     * Test of updateRole method, of class RoleDaoImpl.
     */
    @Test
    public void testUpdateRole() {
        Role role = new Role();
        role.setRole("firstRole");

        Role createdRole = roleDao.createRole(role);

        Role roleById = roleDao.getRoleById(createdRole.getRoleId());

        assertEquals(createdRole, roleById);

        createdRole.setRole("secondRole");
        
        assertNotEquals(createdRole, roleById);

        roleDao.updateRole(createdRole);

        Role updatedRole = roleDao.getRoleById(createdRole.getRoleId());

        assertEquals(createdRole, updatedRole);
    }

}
