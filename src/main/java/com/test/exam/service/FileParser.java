package com.test.exam.service;


import com.example.examsystem.entity.Question;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class FileParser {

    public static List<Question> parseDocx(MultipartFile file) throws Exception {
        XWPFDocument document = new XWPFDocument(file.getInputStream());
        List<Question> questions = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+\\.\\s*)(.*?)\\s*A\\)");

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            Matcher matcher = pattern.matcher(paragraph.getText());
            if (matcher.find()) {
                String questionText = matcher.group(2).trim();
                String answer = "A";  // 这里可以扩展逻辑，解析答案
                questions.add(new Question(questionText, answer));
            }
        }
        return questions;
    }
}
