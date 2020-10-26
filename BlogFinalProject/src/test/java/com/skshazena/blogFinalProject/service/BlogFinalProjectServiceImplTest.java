package com.skshazena.blogFinalProject.service;

import com.skshazena.blogFinalProject.TestApplicationConfiguration;
import com.skshazena.blogFinalProject.daos.CommentDao;
import com.skshazena.blogFinalProject.daos.HashtagDao;
import com.skshazena.blogFinalProject.daos.PostDao;
import com.skshazena.blogFinalProject.daos.RoleDao;
import com.skshazena.blogFinalProject.daos.UserDao;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 25, 2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class BlogFinalProjectServiceImplTest {

    @Autowired
    BlogFinalProjectService service;

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

    public BlogFinalProjectServiceImplTest() {
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
     * Test of processExcerpts method, of class BlogFinalProjectServiceImpl.
     */
    @Test
    public void testProcessExcerpts() {
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
        List<Hashtag> hashtags = new ArrayList<Hashtag>();

        Post post = new Post();
        post.setTitle("firstTitle");
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        post.setPostAt(LocalDateTime.now().minusDays(2).withNano(0));
        post.setExpireAt(LocalDateTime.now().plusDays(5).withNano(0));
        post.setLastEditedAt(LocalDateTime.now().minusHours(3).withNano(0));
        post.setApprovalStatus(true);
        post.setStaticPage(false);
        post.setTitlePhoto("photoFileName");
        post.setUser(user);
        post.setHashtagsForPost(hashtags);
        post.setContent("<p>Less than 255 characters.</p>");
        List<Post> postList = new ArrayList<Post>();
        postList.add(post);
        //give a post that has less than 255 characters

        //service here
        postList = service.processExcerpts(postList);
        assertEquals(post.getContent(), "Less than 255 characters.");
        postList.clear();

        //give a post that has no p tags, no excerpt
        post.setContent("<h1>No p tag.</h1>");
        postList.add(post);

        //service here
        postList = service.processExcerpts(postList);
        assertEquals(post.getContent(), "");
        postList.clear();

        //give a post that has more than 200 characters in p tag
        post.setContent("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent ut quam at diam ornare pellentesque sed sed massa. Pellentesque tincidunt sem ac sollicitudin facilisis. Curabitur lectus libero, mattis ut arcu vel, consequat imperdiet mi. Donec blandit to</p>");
        postList.add(post);

        //service here
        postList = service.processExcerpts(postList);
        assertEquals(post.getContent(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent ut quam at diam ornare pellentesque sed sed massa. Pellentesque tincidunt sem ac sollicitudin facilisis. Curabitur lectus libero, matt");
        postList.clear();

        //give a post that has less than 200 characters in p tag and image
        post.setContent("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent ut quam at diam ornare pellentesque sed sed massa.</p>\n"
                + "<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" title=\"beesmarch.jpg\" src=\"../images/2020/10/blobid1603689225797.jpg\" alt=\"\" width=\"200\" height=\"119\" /></p>\n"
                + "<p>&nbsp;</p>");
        postList.add(post);
        //service here
        postList = service.processExcerpts(postList);
        assertEquals(post.getContent(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent ut quam at diam ornare pellentesque sed sed massa.  ");

    }

    /**
     * Test of parseStringIntoHashtags method, of class
     * BlogFinalProjectServiceImpl.
     */
    @Test
    public void testParseStringIntoHashtags() {
        Hashtag hashtag1 = new Hashtag();
        hashtag1.setTitle("originalHashtag1");
        hashtag1 = hashtagDao.createHashtag(hashtag1);

        Hashtag newHashtag1 = new Hashtag();
        newHashtag1.setTitle("newHashtag");

        Hashtag newHashtag2 = new Hashtag();
        newHashtag2.setTitle("newHashtag2");

        List<Hashtag> hashtagList = new ArrayList<>();
        List<Hashtag> expectedList = new ArrayList<>();

        ///////////////////////////////////////////////////////////////////////
        //expected
        expectedList.add(newHashtag1);

        String hashtagsToParse = "newHashtag";
        hashtagList = service.parseStringIntoHashtags(hashtagsToParse);

        assertEquals(hashtagList, expectedList);

        ///////////////////////////////////////////////////////////////////////
        hashtagList.clear();
        expectedList.clear();
        ///////////////////////////////////////////////////////////////////////
        //expected
        expectedList.add(newHashtag1);

        hashtagsToParse = "newHashtag, ";

        hashtagList = service.parseStringIntoHashtags(hashtagsToParse);

        assertEquals(hashtagList, expectedList);

        ///////////////////////////////////////////////////////////////////////
        hashtagList.clear();
        expectedList.clear();
        ///////////////////////////////////////////////////////////////////////
        //expected
        expectedList.add(hashtag1);
        expectedList.add(newHashtag1);

        hashtagsToParse = "newHashtag, originalHashtag1";

        hashtagList = service.parseStringIntoHashtags(hashtagsToParse);

        assertEquals(hashtagList, expectedList);

        ///////////////////////////////////////////////////////////////////////
        hashtagList.clear();
        expectedList.clear();
        ///////////////////////////////////////////////////////////////////////
        //expected
        expectedList.add(hashtag1);
        expectedList.add(newHashtag1);

        hashtagsToParse = "newHashtag, originalHashtag1, ";

        hashtagList = service.parseStringIntoHashtags(hashtagsToParse);

        assertEquals(hashtagList, expectedList);

        ///////////////////////////////////////////////////////////////////////
        hashtagList.clear();
        expectedList.clear();
        ///////////////////////////////////////////////////////////////////////
        //expected
        expectedList.add(newHashtag2);

        hashtagsToParse = "newHashtag2";

        hashtagList = service.parseStringIntoHashtags(hashtagsToParse);

        assertEquals(hashtagList, expectedList);

        ///////////////////////////////////////////////////////////////////////
        hashtagList.clear();
        expectedList.clear();
        ///////////////////////////////////////////////////////////////////////
        //expected
        //empty list

        hashtagsToParse = "";

        hashtagList = service.parseStringIntoHashtags(hashtagsToParse);

        assertEquals(hashtagList, expectedList);
    }

    /**
     * Test of getOlderAndNewerPost method, of class
     * BlogFinalProjectServiceImpl.
     */
    @Test
    public void testGetOlderAndNewerPost() {
        //TODO This might be able to be done using the DAO.
        //might use SQL UNION or UNION ALL
    }

}
