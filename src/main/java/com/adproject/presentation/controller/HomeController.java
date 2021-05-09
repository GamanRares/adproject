package com.adproject.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for 'home' page
 *
 * @author Gaman Rares-Constantin on 5/9/21
 * Copyright © 2021 Gaman Rares-Constantin. All rights reserved.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ModelAndView home(Model model) {
        return new ModelAndView("index");
    }
}