package com.skshazena.blogFinalProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 20, 2020
 */
@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String displayHomePage() {

        return "mainBlogPageTemplate";
    }
}
