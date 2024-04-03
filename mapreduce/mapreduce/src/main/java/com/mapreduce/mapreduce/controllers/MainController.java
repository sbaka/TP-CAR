package com.mapreduce.mapreduce.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/count/{mot}")
    public int getWordCount(@PathVariable String mot) {
        return akkaService.getWordCount(mot);
    }

    @PostMapping("/file_upload_handler")
    String fileUploadHandler(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String fileContent = new String(bytes);
            String[] lines = fileContent.split("\n");
            System.out.println(" content: " + lines.length);
            akkaService.digestFile(lines);
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e);
            throw new RuntimeException("File upload failed");
        }

        return "redirect:/mapreduce/";
    }
}
