package com.skshazena.blogFinalProject.controllers;

import com.skshazena.blogFinalProject.service.BlogFinalProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 20, 2020
 */
@Controller
public class ContentController {

    @Autowired
    BlogFinalProjectService service;
    
    @GetMapping("/content")
    public String displayContentPage() {
        return "creatorDashboardTemplate";
    }
}
