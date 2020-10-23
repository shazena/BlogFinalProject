package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.CommentDao;
import com.skshazena.blogFinalProject.daos.HashtagDao;
import com.skshazena.blogFinalProject.daos.ImageDao;
import com.skshazena.blogFinalProject.daos.PostDao;
import com.skshazena.blogFinalProject.daos.RoleDao;
import com.skshazena.blogFinalProject.daos.UserDao;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.Hashtag;
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

    @GetMapping("/post")
    public String getPostDetailsOnPage(Integer id, Model model) {
        List<Post> staticPosts = postDao.getAllPostsForBlogThatAreStaticNewestFirst();
        Post post = postDao.getPostById(id);
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
    }

    @GetMapping("/hashtag")
    public String getPostsWithThisHashtag(Integer id, Model model) {
        Hashtag hashtagById = hashtagDao.getHashtagById(id);
        List<Post> allPostsForBlogForHashtagNewestFirst = postDao.getAllPostsForBlogForHashtagNewestFirst(id);
        allPostsForBlogForHashtagNewestFirst = service.processExcerpts(allPostsForBlogForHashtagNewestFirst);

        model.addAttribute("hashtag", hashtagById);
        model.addAttribute("posts", allPostsForBlogForHashtagNewestFirst);
        return "mainBlogPageHashtags";
    }

}
