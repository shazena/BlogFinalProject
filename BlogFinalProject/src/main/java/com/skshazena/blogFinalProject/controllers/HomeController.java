package com.skshazena.blogFinalProject.controllers;

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

    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        List<Post> posts = service.getAllPostsForBlogNonStaticNewestFirst();
        List<Post> staticPosts = service.getAllPostsForBlogThatAreStaticNewestFirst();
        model.addAttribute("posts", posts);
        model.addAttribute("staticPosts", staticPosts);

        return "mainBlogPageTemplate";
    }

    @GetMapping("/post")
    public String getFullPost(Integer id, Model model) {
        return "blogPost";
    }
}
