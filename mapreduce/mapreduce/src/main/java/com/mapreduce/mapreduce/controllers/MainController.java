package com.mapreduce.mapreduce.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mapreduce.mapreduce.services.AkkaServiceItf;

@Controller
@RequestMapping("/mapreduce")
public class MainController {
    @Autowired
    AkkaServiceItf akkaService;

    @GetMapping("/")
    String getHomePage() {
        return "home";
    }

    @GetMapping("/init-akka")
    String init() {
        akkaService.initSystems();
        return "redirect:/mapreduce/";
    }

    // Add a REST endpoint to retrieve word counts
    @PostMapping("/count")
    public String getWordCount(@RequestParam String mot, Model model) {
        System.out.println("mot: " + mot);
        model.addAttribute("mot", mot);
        model.addAttribute("count", akkaService.getWordCount(mot));
        return "home";
    }

    @PostMapping("/file_upload_handler")
    String fileUploadHandler(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String fileContent = new String(bytes);
            String[] lines = fileContent.split("\n");
            akkaService.digestFile(lines);
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e);
            throw new RuntimeException("File upload failed");
        }

        return "home";
    }
}
