package com.test.exam.service;

import com.test.exam.entity.Question;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class FileParser {

    public static List<Question> parseDocx(MultipartFile file) throws Exception {
        XWPFDocument document = new XWPFDocument(file.getInputStream());
        List<Question> questions = new ArrayList<>();
        Pattern questionPattern = Pattern.compile("(\\d+\\.\\s*)(.*?)\\s*A\\)"); // 匹配问题文本
        Pattern optionPattern = Pattern.compile("([A-D])\\)\\s*(.*?)\\s*(?=(?:[A-D]\\)|$))"); // 匹配选项 A) B) C) D)

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            Matcher questionMatcher = questionPattern.matcher(paragraph.getText());
            if (questionMatcher.find()) {
                // 创建一个新的问题实体
                Question question = new Question();
                question.setQuestionText(questionMatcher.group(2).trim());

                // 初始化选项（默认为空）
                String optionA = null, optionB = null, optionC = null, optionD = null;

                // 通过正则匹配选项 A) B) C) D)
                Matcher optionMatcher = optionPattern.matcher(paragraph.getText());
                while (optionMatcher.find()) {
                    switch (optionMatcher.group(1)) {
                        case "A":
                            optionA = optionMatcher.group(2).trim();
                            break;
                        case "B":
                            optionB = optionMatcher.group(2).trim();
                            break;
                        case "C":
                            optionC = optionMatcher.group(2).trim();
                            break;
                        case "D":
                            optionD = optionMatcher.group(2).trim();
                            break;
                    }
                }

                // 设置选项
                question.setOptionA(optionA);
                question.setOptionB(optionB);
                question.setOptionC(optionC);
                question.setOptionD(optionD);

                // 设置答案（假设答案为 A，后续可以根据需求调整）
                question.setAnswer("A");

                questions.add(question);
            }
        }
        return questions;
    }
}
