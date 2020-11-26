package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.*;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 20, 2020
 */
@Controller
public class HomeController {

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

    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        List<Post> posts = postDao.getAllPostsForBlogNonStaticNewestFirst();

        posts = service.processExcerpts(posts);

        List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();

        model.addAttribute("posts", posts);
        model.addAttribute("staticPosts", staticPosts);

        return "mainBlogPage";
    }

}
