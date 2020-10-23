package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.CommentDao;
import com.skshazena.blogFinalProject.daos.HashtagDao;
import com.skshazena.blogFinalProject.daos.ImageDao;
import com.skshazena.blogFinalProject.daos.PostDao;
import com.skshazena.blogFinalProject.daos.RoleDao;
import com.skshazena.blogFinalProject.daos.UserDao;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.dtos.User;
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
 * Date Created: Oct 20, 2020
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

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

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> allUsers = userDao.getAllUsers();
        model.addAttribute("users", allUsers);

        return "adminDashboardUsers";
    }

  
}
