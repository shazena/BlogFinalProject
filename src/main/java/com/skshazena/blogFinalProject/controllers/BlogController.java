package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.*;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.EnhancedSpringUser;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.Role;
import com.skshazena.blogFinalProject.dtos.User;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

    //TODO Add in the add Comment functionality
    @Autowired
    BlogFinalProjectService service;

    @Autowired
    ImageDao imageDao;

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

    @Autowired
    PasswordEncoder encoder;

    String USER_PHOTO_UPLOAD_DIRECTORY = "Users";

    Set<ConstraintViolation<Comment>> violationsCommentAdd = new HashSet<>();
    Set<ConstraintViolation<User>> violationsUserAdd = new HashSet<>();
    Set<ConstraintViolation<User>> violationsUserEdit = new HashSet<>();
    Set<ConstraintViolation<User>> violationsUserPasswordEdit = new HashSet<>();

    @GetMapping("/post")
    public String getPostDetailsOnPage(Integer id, Model model) {
        List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();
        Post post = postDao.getPostById(id);
        if (post.getExpireAt() == null || post.getExpireAt().isAfter(LocalDateTime.now())) {

            List<Comment> allCommentsForPost = commentDao.getAllCommentsForPost(id);

            List<Post> olderAndNewerPosts;
            Post olderPost = null;
            Post newerPost = null;

            if (!post.isStaticPage()) {
                olderAndNewerPosts = service.getOlderAndNewerPost(id);
                olderPost = olderAndNewerPosts.get(0);
                newerPost = olderAndNewerPosts.get(1);
            }

            model.addAttribute("staticPosts", staticPosts);
            model.addAttribute("comments", allCommentsForPost);
            model.addAttribute("post", post);
            model.addAttribute("olderPost", olderPost);
            model.addAttribute("newerPost", newerPost);
            return "mainBlogPostDetails";
        } else {
            String message = "That page could not be found";
            model.addAttribute("message", message);
            return "error";
        }
    }

    @GetMapping("/hashtag")
    public String getPostsWithThisHashtag(Integer id, Model model) {
        Hashtag hashtagById = hashtagDao.getHashtagById(id);
        List<Post> allPostsForBlogForHashtagNewestFirst = postDao.getAllPostsForBlogForHashtagNewestFirst(id);
        allPostsForBlogForHashtagNewestFirst = service.processExcerpts(allPostsForBlogForHashtagNewestFirst);

        List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();

        model.addAttribute("hashtag", hashtagById);
        model.addAttribute("posts", allPostsForBlogForHashtagNewestFirst);
        model.addAttribute("staticPosts", staticPosts);
        return "mainBlogPageHashtags";
    }

//    //TODO MAYBE MAKE A  USER CONTROLLER FOR BLOG?????
//    @GetMapping("/userDetails") //TODO needs to be added to a link on the page!!!!
//    public String getUserDetails(Integer id, Model model) {
//
//        //get the user that is currently logged in
//        EnhancedSpringUser user = (EnhancedSpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        //check to see if they are trying to view their own profile
//        if (id != user.getUserId()) { //if they are not!!!
//            return "error";//don't allow them to!
//        } else {
//
//            User userById = userDao.getUserById(id);
//            List<Comment> allCommentsWrittenByUser = commentDao.getAllCommentsWrittenByUser(id);
//
//            model.addAttribute("user", userById);
//            model.addAttribute("comments", allCommentsWrittenByUser);
//
//            return "adminDashboardUserDetails";
//        }
//    }
    @PostMapping("/submitComment")
    public String postAComment(HttpServletRequest request, Model model) {
        String userIdAsString = request.getParameter("userId");
        String postIdAsString = request.getParameter("postId");
        String commentTitle = request.getParameter("commentTitle");
        String commentContent = request.getParameter("commentContent");

        boolean approvalStatus = false;

        User user = userDao.getUserById(Integer.parseInt(userIdAsString));

        Role adminRole = roleDao.getRoleByRole("ROLE_ADMIN");

        if (user.getRoles().contains(adminRole)) {
            approvalStatus = true;
        }

        Post post = postDao.getPostById(Integer.parseInt(postIdAsString));

        Comment comment = new Comment();
        comment.setTitle(commentTitle);
        comment.setContent(commentContent);
        comment.setApprovalStatus(approvalStatus);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setPost(post);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsCommentAdd = validate.validate(comment);

        if (violationsCommentAdd.isEmpty()) {
            commentDao.createComment(comment);
            return "redirect:/blog/post?id=" + post.getPostId();
        } else {

            if (post.getExpireAt() == null || post.getExpireAt().isAfter(LocalDateTime.now())) {
                List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();
                List<Comment> allCommentsForPost = commentDao.getAllCommentsForPost(post.getPostId());

                List<Post> olderAndNewerPosts;
                Post olderPost = null;
                Post newerPost = null;

                if (!post.isStaticPage()) {
                    olderAndNewerPosts = service.getOlderAndNewerPost(post.getPostId());
                    olderPost = olderAndNewerPosts.get(0);
                    newerPost = olderAndNewerPosts.get(1);
                }

                model.addAttribute("staticPosts", staticPosts);
                model.addAttribute("comments", allCommentsForPost);
                model.addAttribute("post", post);
                model.addAttribute("olderPost", olderPost);
                model.addAttribute("newerPost", newerPost);
                model.addAttribute("errors", violationsCommentAdd);
                return "mainBlogPostDetails";
            } else {
                return "error";
            }

        }
    }

    @GetMapping("/createNewUserAccount")
    public String createNewUserAccount(Model model) {
        List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();
        model.addAttribute("staticPosts", staticPosts);

        violationsUserAdd.clear();
        model.addAttribute("errors", violationsUserAdd);
        return "mainBlogPageCreateNewUser";
    }

    @PostMapping("/createNewUserAccount")
    public String createNewUserAccountInDB(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/";
        }

        String usernameToAdd = request.getParameter("usernameToAdd");
        String passwordToAdd = request.getParameter("passwordToAdd");
        String confirmPasswordToAdd = request.getParameter("confirmPasswordToAdd");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        Set<Role> roles = new HashSet<>();
        Role userRole = roleDao.getRoleByRole("ROLE_USER");
        roles.add(userRole);

        Set<Error> customErrors = new HashSet<Error>();

        if (!passwordToAdd.equals(confirmPasswordToAdd)) {
            customErrors.add(new Error("Your passwords did not match. Please try again."));
            model.addAttribute("errors", customErrors);
            return "mainBlogPageCreateNewUser";
        }

        User user = new User();
        user.setUsername(usernameToAdd);
        user.setPassword(passwordToAdd);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setRoles(roles);
        user.setLastLogin(LocalDateTime.now());

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsUserAdd = validate.validate(user);

        if (violationsUserAdd.isEmpty()) {
            user.setPassword(encoder.encode(passwordToAdd));
            user = userDao.createUser(user);
            String accountSuccessfullyMade = "Your account has been made. You may now login with your chosen credentials.";

            List<Post> posts = postDao.getAllPostsForBlogNonStaticNewestFirst();

            posts = service.processExcerpts(posts);

            List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();

            model.addAttribute("posts", posts);
            model.addAttribute("staticPosts", staticPosts);
            model.addAttribute("successMessage", accountSuccessfullyMade);
            return "mainBlogPage";

        } else {

            List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();
            model.addAttribute("staticPosts", staticPosts);

            model.addAttribute("errors", violationsUserAdd);
            return "mainBlogPageCreateNewUser";

        }

    }

    @GetMapping("/userProfile")
    public String getUserProfilePage(Model model) {
        EnhancedSpringUser enhancedSpringUser = (EnhancedSpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Comment> allCommentsWrittenByUser = commentDao.getAllCommentsWrittenByUser(enhancedSpringUser.getUserId());

        model.addAttribute("user", enhancedSpringUser);
        model.addAttribute("comments", allCommentsWrittenByUser);
        return "mainBlogPageUserProfile";
    }

    @GetMapping("/userEdit")
    public String getUserEditPage(Model model) {
        EnhancedSpringUser user = (EnhancedSpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        violationsUserEdit.clear();
        model.addAttribute("errors", violationsUserEdit);

        return "mainBlogPageUserEdit";
    }

    @GetMapping("/passwordEdit")
    public String getUserPasswordEditPage(Model model) {
        EnhancedSpringUser user = (EnhancedSpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        violationsUserPasswordEdit.clear();
        model.addAttribute("errors", violationsUserPasswordEdit);

        return "mainBlogPagePasswordEdit";
    }

    @PostMapping("/userEdit")
    public String editUserInDB(HttpServletRequest request, Model model, @RequestParam("file") MultipartFile file, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/blog/userProfile";
        }

        EnhancedSpringUser enhancedSpringUser = (EnhancedSpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(enhancedSpringUser.getUserId());

        String usernameToEdit = request.getParameter("usernameToEdit");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        user.setUsername(usernameToEdit);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        boolean fileIsEmpty = file.isEmpty();

        if (!fileIsEmpty) {
            user.setProfilePicture(imageDao.updateImage(file, user.getProfilePicture(), USER_PHOTO_UPLOAD_DIRECTORY));
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsUserEdit = validate.validate(user);

        if (violationsUserEdit.isEmpty()) {
            userDao.updateUser(user);

            enhancedSpringUser.setFirstName(user.getFirstName());
            enhancedSpringUser.setLastName(user.getLastName());
            enhancedSpringUser.setProfilePicture(user.getProfilePicture());

            return "redirect:/blog/userProfile";
        } else {
            model.addAttribute("errors", violationsUserEdit);
            model.addAttribute("user", user);
            model.addAttribute("errors", violationsUserEdit);

            return "mainBlogPageUserEdit";
        }

    }

    @PostMapping("/passwordEdit")
    public String editUserPasswordInDB(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/blog/userProfile";
        }

        //TODO check for all the times this is used on this page, make sure I get the userId and get the user object frmo the dau
        EnhancedSpringUser enhancedSpringUser = (EnhancedSpringUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(enhancedSpringUser.getUserId());

        String passwordToEdit = request.getParameter("passwordToEdit");
        String confirmPasswordToEdit = request.getParameter("confirmPasswordToEdit");

        Set<Error> customErrors = new HashSet<Error>();

        if (!passwordToEdit.equals(confirmPasswordToEdit)) {
            customErrors.add(new Error("Your passwords did not match. Please try again."));
        }

        if (customErrors.size() > 0) {

            model.addAttribute("user", user);

            model.addAttribute("errors", customErrors);
            return "mainBlogPagePasswordEdit";
        }

        user.setPassword(passwordToEdit);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsUserPasswordEdit = validate.validate(user);

        if (violationsUserPasswordEdit.isEmpty()) {
            user.setPassword(encoder.encode(passwordToEdit));
            userDao.updateUser(user);

            return "redirect:/blog/userProfile";
        } else {

            model.addAttribute("user", user);

            model.addAttribute("errors", violationsUserPasswordEdit);
            return "mainBlogPagePasswordEdit";

        }

    }
}
