package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Post;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 21, 2020
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogFinalProjectService service;

    @GetMapping("/post")
    public String getPostDetailsOnPage(Integer id, Model model) {
        List<Post> staticPosts = service.getAllPostsForBlogThatAreStaticNewestFirst();
        Post post = service.getPostById(id);
        List<Comment> allCommentsForPost = service.getAllCommentsForPost(id);

        List<Post> olderAndNewerPosts = service.getOlderAndNewerPost(id);
        Post olderPost = olderAndNewerPosts.get(0);
        Post newerPost = olderAndNewerPosts.get(1);

        model.addAttribute("staticPosts", staticPosts);
        model.addAttribute("comments", allCommentsForPost);
        model.addAttribute("post", post);
        model.addAttribute("olderPost", olderPost);
        model.addAttribute("newerPost", newerPost);
        return "blogPost";
    }
}
