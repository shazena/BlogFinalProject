package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.CommentDao;
import com.skshazena.blogFinalProject.daos.HashtagDao;
import com.skshazena.blogFinalProject.daos.ImageDao;
import com.skshazena.blogFinalProject.daos.PostDao;
import com.skshazena.blogFinalProject.daos.RoleDao;
import com.skshazena.blogFinalProject.daos.UserDao;
import com.skshazena.blogFinalProject.dtos.Hashtag;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
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
public class AdminHashtagController {

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

    Set<ConstraintViolation<Hashtag>> violationsHashtagEdit = new HashSet<>();

    @GetMapping("/hashtags")
    public String getAllHashtags(Model model) {
        List<Hashtag> allHashtags = hashtagDao.getAllHashtags();
        model.addAttribute("hashtags", allHashtags);
        return "adminDashboardHashtags";
    }

    @GetMapping("/hashtagDetails")
    public String getHashtagDetails(Integer id, Model model) {
        Hashtag hashtagById = hashtagDao.getHashtagById(id);

        List<Post> allPostsForHashtagForAdminNewestFirst = postDao.getAllPostsForHashtagForAdminNewestFirst(id);

        model.addAttribute("hashtag", hashtagById);
        model.addAttribute("posts", allPostsForHashtagForAdminNewestFirst);

        return "adminDashboardHashtagDetails";
    }

    @GetMapping("/hashtagEdit")
    public String getHashtagToEdit(Integer id, Model model) {
        Hashtag hashtagById = hashtagDao.getHashtagById(id);

        model.addAttribute("hashtag", hashtagById);

        violationsHashtagEdit.clear();
        model.addAttribute("errors", violationsHashtagEdit);
        return "adminDashboardHashtagEdit";
    }

    @PostMapping("/hashtagEdit")
    public String editHashtag(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {
        if (action.equals("cancel")) {
            return "redirect:/admin/hashtags";
        }

        String hashtagTitleFromEdit = request.getParameter("title");
        String hashtagIdAsString = request.getParameter("hashtagId");

        //find out if the hashtag already exists
        Hashtag theNewHashtag = new Hashtag();
        Hashtag hashtagByTitle = hashtagDao.getHashtagByTitle(hashtagTitleFromEdit);

        if (hashtagByTitle != null) { //if it does Exist... show error
            String message = "That hashtag already exists. No changes were made.";

            model.addAttribute("message", message);

            List<Hashtag> allHashtags = hashtagDao.getAllHashtags();
            model.addAttribute("hashtags", allHashtags);

            return "adminDashboardHashtags";

        } else { //if it does not exist, then validate
            theNewHashtag.setHashtagId(Integer.parseInt(hashtagIdAsString));
            theNewHashtag.setTitle(hashtagTitleFromEdit);

            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violationsHashtagEdit = validate.validate(theNewHashtag);

            if (violationsHashtagEdit.isEmpty()) { //if there are not violations

                hashtagDao.updateHashtag(theNewHashtag);

                return "redirect:/admin/hashtags";
            } else {
                //return the value to the page, return the page

                model.addAttribute("hashtag", theNewHashtag);
                model.addAttribute("errors", violationsHashtagEdit);

                return "adminDashboardHashtagEdit";
            }

        }

    }
}
