package com.mapreduce.mapreduce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mapreduce")
public class MainController {

    @GetMapping("/index")
    String getHomePage() {
        return "home";
    }
}
