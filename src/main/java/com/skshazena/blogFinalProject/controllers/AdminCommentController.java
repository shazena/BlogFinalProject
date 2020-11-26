package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.daos.*;
import com.skshazena.blogFinalProject.dtos.Comment;
import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
public class AdminCommentController {

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

    @GetMapping("/commentApproval")
    public String getAllComments(Model model) {
        List<Comment> allCommentsNeedingApproval = commentDao.getAllCommentsNeedingApproval();

        model.addAttribute("comments", allCommentsNeedingApproval);
        return "adminDashboardCommentApproval";
    }

    @GetMapping("/commentDetails")
    public String getCommentDetails(Integer id, Model model) {
        Comment commentById = commentDao.getCommentById(id);

        model.addAttribute("comment", commentById);

        //TODO page can have the preview post on blog button
        return "adminDashboardCommentDetails";
    }

    @PostMapping("/commentDetails")
    public String commentApproval(HttpServletRequest request, Model model, @RequestParam(value = "action", required = true) String action) {

        if (action.equals("cancel")) {
            return "redirect:/admin/commentApproval";
        }

        String commentIdAsString = request.getParameter("commentId");
        Comment commentById = commentDao.getCommentById(Integer.parseInt(commentIdAsString));

        if (action.equals("delete")) {
            commentDao.deleteComment(commentById.getCommentId());
            return "redirect:/admin/commentApproval";
        } else {

            commentById.setApprovalStatus(true);
            commentDao.updateComment(commentById);
            return "redirect:/admin/commentApproval";
        }

    }

}
