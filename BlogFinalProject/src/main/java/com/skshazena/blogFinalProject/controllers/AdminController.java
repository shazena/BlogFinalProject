package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.dtos.User;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 20, 2020
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    BlogFinalProjectService service;

    Set<ConstraintViolation<Post>> violations = new HashSet<>();

    @GetMapping("/dashboard")
    public String displayAdminPage(Model model) {
        int numberOfEnabledUsers = service.getNumberOfEnabledUsers();
        int postsNeedingApproval = service.getAllPostsNeedingApprovalForAdminOldestFirst().size();
        int commentsNeedingApproval = service.getAllCommentsNeedingApproval().size();

        model.addAttribute("numberOfEnabledUsers", numberOfEnabledUsers);
        model.addAttribute("postsNeedingApproval", postsNeedingApproval);
        model.addAttribute("commentsNeedingApproval", commentsNeedingApproval);
        return "adminDashboardTemplate";
    }

    @GetMapping("/posts")
    public String displayAdminPostsPage(Model model) {
        List<Post> allPostsForAdminNewestFirst = service.getAllPostsForAdminNewestFirst();

        model.addAttribute("posts", allPostsForAdminNewestFirst);

        return "adminDashboardPosts";
    }

    @GetMapping("/postCreate")
    public String displayCreateNewPostPage(Model model) {
        List<Hashtag> allHashtags = service.getAllHashtags();
        model.addAttribute("hashtags", allHashtags);
        violations.clear();
        model.addAttribute("errors", violations);

        return "adminDashboardPostsCreate";
    }

    @PostMapping("/postCreate")
    public String createNewPost(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/admin/posts";
        }

        String title = request.getParameter("title");
        String postAtAsString = request.getParameter("postAt");
        String expireAtAsString = request.getParameter("expireAt");
        String hashtagsForPostAsString = request.getParameter("hashtagsForPost");
        String staticPageAsString = request.getParameter("staticPage");
        String fileAsString = request.getParameter("file");
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

        List<Hashtag> hashtagsForPost = service.parseStringIntoHashtags(hashtagsForPostAsString);

        boolean staticPage = false;
        if (staticPageAsString == null) {
            staticPage = false;
        } else {
            staticPage = true;
        }

        User user = service.getUserById(Integer.parseInt(userId));

        Post post = new Post();
        post.setTitle(title);
        post.setCreatedAt(LocalDateTime.now());
        post.setPostAt(postAt);
        post.setExpireAt(expireAt);
        post.setLastEditedAt(LocalDateTime.now());
        post.setContent(contentFromTinyMCE);
        post.setApprovalStatus(true);
        post.setStaticPage(staticPage);
        post.setTitlePhoto(fileAsString);
        post.setUser(user);
        post.setHashtagsForPost(hashtagsForPost);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(post);

        if (violations.isEmpty()) {
            for (Hashtag hashtag : hashtagsForPost) {
                if (hashtag.getHashtagId() == 0) {
                    service.createHashtag(hashtag);
                }
            }
            service.createPost(post);
            return "redirect:/admin/posts";
        } else {

            List<Hashtag> allHashtags = service.getAllHashtags();
            model.addAttribute("hashtags", allHashtags);
            model.addAttribute("errors", violations);

            //consider making new version of this page where the user gets back their post
            //formatted like the edit page
            return "adminDashboardPostCreate";
        }

        //At the end you will need to implement theimage dao to save the main picture from the page.
        //need to send attributes to this page.
    }
}
