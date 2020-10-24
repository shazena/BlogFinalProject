package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.*;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.User;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Date Created: Oct 20, 2020
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardAndPostController {

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

    private final String POST_TITLE_PHOTO_UPLOAD_DIRECTORY = "PostTitlePhotos";

    Set<ConstraintViolation<Post>> violationsPostAdd = new HashSet<>();
    Set<ConstraintViolation<Post>> violationsPostEdit = new HashSet<>();

    @GetMapping("/dashboard")
    public String displayAdminPage(Model model) {

        List<User> allUsers = userDao.getAllUsers();
        ArrayList<User> enabledUsers = new ArrayList<User>();
        for (User user : allUsers) {
            if (user.isEnabled()) {
                enabledUsers.add(user);
            }
        }
        int numberOfEnabledUsers = enabledUsers.size();

        int postsNeedingApproval = postDao.getAllPostsNeedingApprovalForAdminOldestFirst().size();
        int commentsNeedingApproval = commentDao.getAllCommentsNeedingApproval().size();

        model.addAttribute("numberOfEnabledUsers", numberOfEnabledUsers);
        model.addAttribute("postsNeedingApproval", postsNeedingApproval);
        model.addAttribute("commentsNeedingApproval", commentsNeedingApproval);
        return "adminDashboard";
    }

    @GetMapping("/posts")
    public String displayAdminPostsPage(Model model) {
        List<Post> allPostsForAdminNewestFirst = postDao.getAllPostsForAdminNewestFirst();

        model.addAttribute("posts", allPostsForAdminNewestFirst);

        String approval = "";
        model.addAttribute("approval", approval);

        return "adminDashboardPosts";
    }

    @GetMapping("/postDetails")
    public String getPostDetailsOnPage(Integer id, Model model) {

        Post post = postDao.getPostById(id);

        model.addAttribute("post", post);
        return "adminDashboardPostDetails";
    }

    @GetMapping("/postCreate")
    public String displayCreateNewPostPage(Model model) {
        List<Hashtag> allHashtags = hashtagDao.getAllHashtags();
        model.addAttribute("hashtags", allHashtags);
        violationsPostAdd.clear();
        model.addAttribute("errors", violationsPostAdd);

        return "adminDashboardPostsCreate";
    }

    @PostMapping("/postCreate")
    public String createNewPost(HttpServletRequest request, Model model, @RequestParam("file") MultipartFile file, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/admin/posts";
        }

        String fileLocation = imageDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), POST_TITLE_PHOTO_UPLOAD_DIRECTORY);

        String title = request.getParameter("title");
        String postAtAsString = request.getParameter("postAt");
        String expireAtAsString = request.getParameter("expireAt");
        String hashtagsForPostAsStringToParse = request.getParameter("hashtagsForPost");
        String staticPageAsString = request.getParameter("staticPage");
        String contentFromTinyMCE = request.getParameter("content");
        String userId = request.getParameter("userId");

        LocalDateTime postAt = LocalDateTime.now();
        if (postAtAsString.isBlank()) {
            postAt = null;
        } else {
            postAt = LocalDateTime.parse(postAtAsString, DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        }

        LocalDateTime expireAt = LocalDateTime.now();
        if (expireAtAsString.isBlank()) {
            expireAt = null;
        } else {
            expireAt = LocalDateTime.parse(expireAtAsString, DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        }

        List<Hashtag> hashtagsForPost = service.parseStringIntoHashtags(hashtagsForPostAsStringToParse);

        boolean staticPage = false;
        if (staticPageAsString == null) {
            staticPage = false;
        } else {
            staticPage = true;
        }

        User user = userDao.getUserById(Integer.parseInt(userId));

        Post post = new Post();
        post.setTitle(title);
        post.setCreatedAt(LocalDateTime.now());
        post.setPostAt(postAt);
        post.setExpireAt(expireAt);
        post.setLastEditedAt(LocalDateTime.now());
        post.setContent(contentFromTinyMCE);
        post.setApprovalStatus(true);
        post.setStaticPage(staticPage);
        post.setTitlePhoto(fileLocation);
        post.setUser(user);
        post.setHashtagsForPost(hashtagsForPost);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsPostAdd = validate.validate(post);

        if (violationsPostAdd.isEmpty()) {
            if (hashtagsForPost != null || !hashtagsForPost.isEmpty()) {
                for (Hashtag hashtag : hashtagsForPost) {
                    if (hashtag.getHashtagId() == 0) {
                        hashtag = hashtagDao.createHashtag(hashtag);
                    }
                }
            } else {
                hashtagsForPost = new ArrayList<Hashtag>();
                post.setHashtagsForPost(hashtagsForPost);
            }
            post = postDao.createPost(post);

            return "redirect:/admin/postDetails?id=" + post.getPostId();
        } else {

            model.addAttribute("post", post);

            String hashtagsForPostAsString = "";
            if (hashtagsForPost != null) {
                for (Hashtag hashtag : hashtagsForPost) {
                    hashtagsForPostAsString += hashtag.getTitle() + ", ";
                }
            }

            List<Hashtag> allHashtags = hashtagDao.getAllHashtags();

            model.addAttribute("hashtagsForPostAsString", hashtagsForPostAsString);
            model.addAttribute("hashtags", allHashtags);
            model.addAttribute("errors", violationsPostAdd);

            //consider making new version of this page where the user gets back their post
            //formatted like the edit page
            return "adminDashboardPostsCreate";
        }

        //TODO At the end you will need to implement theimage dao to save the main picture from the page.
        //need to send attributes to this page.
    }

    @GetMapping("/postEdit")
    public String getPostToEdit(Integer id, Model model) {

        Post postById = postDao.getPostById(id);

        List<Hashtag> hashtagsForPost = postById.getHashtagsForPost();
        String hashtagsForPostAsString = "";
        for (Hashtag hashtag : hashtagsForPost) {
            hashtagsForPostAsString += hashtag.getTitle() + ", ";
        }

        List<Hashtag> allHashtags = hashtagDao.getAllHashtags();

        model.addAttribute("hashtagsForPostAsString", hashtagsForPostAsString);
        model.addAttribute("hashtags", allHashtags);
        violationsPostEdit.clear();
        model.addAttribute("errors", violationsPostEdit);

        model.addAttribute("post", postById);
        return "adminDashboardPostsEdit";
    }

    @PostMapping("/postEdit")
    public String editPost(HttpServletRequest request, Model model, @RequestParam("file") MultipartFile file, @RequestParam(value = "action", required = true) String action) {

        if (action.equals("cancel")) {
            return "redirect:/admin/posts";
        }

        String title = request.getParameter("title");
        String postAtAsString = request.getParameter("postAt");
        String expireAtAsString = request.getParameter("expireAt");
        String hashtagsForPostAsStringToParse = request.getParameter("hashtagsForPost");
        String staticPageAsString = request.getParameter("staticPage");
        String contentFromTinyMCE = request.getParameter("content");
        String userId = request.getParameter("userId");

        LocalDateTime postAt = LocalDateTime.now();
        if (postAtAsString.isBlank()) {
            postAt = null;
        } else {
            postAt = LocalDateTime.parse(postAtAsString, DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        }

        LocalDateTime expireAt = LocalDateTime.now();
        if (expireAtAsString.isBlank()) {
            expireAt = null;
        } else {
            expireAt = LocalDateTime.parse(expireAtAsString, DateTimeFormatter.ISO_LOCAL_DATE_TIME).withNano(0);
        }

        List<Hashtag> hashtagsForPost = service.parseStringIntoHashtags(hashtagsForPostAsStringToParse);

        boolean staticPage = false;
        if (staticPageAsString == null) {
            staticPage = false;
        } else {
            staticPage = true;
        }

        User user = userDao.getUserById(Integer.parseInt(userId));

        String postIdAsString = request.getParameter("postId");

        Post post = postDao.getPostById(Integer.parseInt(postIdAsString));

        post.setTitle(title);
        post.setPostAt(postAt);
        post.setExpireAt(expireAt);
        post.setLastEditedAt(LocalDateTime.now());
        post.setContent(contentFromTinyMCE);
        post.setApprovalStatus(true);
        post.setStaticPage(staticPage);

        boolean fileIsEmpty = file.isEmpty();

        if (!fileIsEmpty) {
            post.setTitlePhoto(imageDao.updateImage(file, post.getTitlePhoto(), POST_TITLE_PHOTO_UPLOAD_DIRECTORY));
        }

        post.setUser(user);
        post.setHashtagsForPost(hashtagsForPost);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsPostEdit = validate.validate(post);

        if (violationsPostEdit.isEmpty()) {
            if (hashtagsForPost != null || !hashtagsForPost.isEmpty()) {
                for (Hashtag hashtag : hashtagsForPost) {
                    if (hashtag.getHashtagId() == 0) {
                        hashtag = hashtagDao.createHashtag(hashtag);
                    }
                }
            } else { //if the post has no hashtags
                hashtagsForPost = new ArrayList<Hashtag>();
                post.setHashtagsForPost(hashtagsForPost);
            }
            postDao.updatePost(post);

            return "redirect:/admin/postDetails?id=" + post.getPostId();
        } else {

            model.addAttribute("post", post);

            String hashtagsForPostAsString = "";
            if (hashtagsForPost != null) {
                for (Hashtag hashtag : hashtagsForPost) {
                    hashtagsForPostAsString += hashtag.getTitle() + ", ";
                }
            }

            List<Hashtag> allHashtags = hashtagDao.getAllHashtags();

            model.addAttribute("hashtagsForPostAsString", hashtagsForPostAsString);
            model.addAttribute("hashtags", allHashtags);
            model.addAttribute("errors", violationsPostEdit);

            return "adminDashboardPostsCreate";
        }

    }

    @GetMapping("/postApproval")
    public String displayPostApprovalPage(Model model) {
        List<Post> allPostsNeedingApprovalForAdminOldestFirst = postDao.getAllPostsNeedingApprovalForAdminOldestFirst();

        String approval = "approval";
        model.addAttribute("approval", approval);
        model.addAttribute("posts", allPostsNeedingApprovalForAdminOldestFirst);

        return "adminDashboardPosts";
    }

    @PostMapping("/postDelete")
    public String deletePost(HttpServletRequest request) {
        String idToDelete = request.getParameter("postIdToDelete");//reads the name, not id

        postDao.deletePost(Integer.parseInt(idToDelete));

        return "redirect:/admin/posts";
    }

}
