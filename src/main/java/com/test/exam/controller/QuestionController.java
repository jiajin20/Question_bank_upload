package com.test.exam.controller;

import com.test.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/uploadQuestions")
    public String uploadQuestions(@RequestParam("file") MultipartFile file) {
        try {
            questionService.parseAndSaveQuestions(file);
            return "文件上传并处理成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败：" + e.getMessage();
        }
    }
}
