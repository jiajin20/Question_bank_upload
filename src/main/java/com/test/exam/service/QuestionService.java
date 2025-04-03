package com.test.exam.service;

import com.test.exam.mapper.QuestionMapper;
import com.test.exam.model.QuestionModel;
import com.test.exam.entity.Question;
import com.test.exam.util.QuestionConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public void parseAndSaveQuestions(MultipartFile file) throws IOException {
        // 获取文件名并去掉 .docx 后缀
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            fileName = fileName.replaceAll("\\.(docx|doc)$", "");  // 去除 .docx 或 .doc 后缀
        }

        XWPFDocument document = new XWPFDocument(file.getInputStream());
        List<QuestionModel> modelQuestions = new ArrayList<>();
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        QuestionModel currentModelQuestion = null;
        String currentCategory = null;  // 用于存储当前题目类别

        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText().trim();

            if (text.isEmpty()) {
                continue;
            }

            // 判断题目类别，匹配更灵活
            if (text.contains("选择题")) {  // 匹配“选择题”
                currentCategory = "single_choice";
                continue;
            } else if (text.contains("多选题")) {  // 匹配“多选题”
                currentCategory = "multiple_choice";
                continue;
            } else if (text.contains("判断题")) {  // 匹配“判断题”
                currentCategory = "true_false";
                continue;
            } else if (text.contains("问答题")) {  // 匹配“问答题”
                currentCategory = "short_answer";
                continue;
            }

            // 确保 currentCategory 不为 null
            if (currentCategory == null) {
                continue;  // 跳过当前行
            }

            // 解析题目文本
            if (text.matches("^[0-9]+\\..*")) {  // 题目
                if (currentModelQuestion != null) {
                    modelQuestions.add(currentModelQuestion);
                }
                currentModelQuestion = new QuestionModel();
                currentModelQuestion.setOptions(new ArrayList<>());
                currentModelQuestion.setQuestionText(text);
                currentModelQuestion.setQuestionType(currentCategory);  // 设置当前题目类型
            } else if (text.matches("^[A-D]\\..*")) {  // 选项
                if (currentModelQuestion != null) {
                    currentModelQuestion.getOptions().add(text);
                }
            } else if (text.startsWith("答案：")) {  // 解析答案
                if (currentModelQuestion != null) {
                    String answer = text.replace("答案：", "").trim();
                    if (currentModelQuestion.getQuestionType().equals("multiple_choice")) {
                        currentModelQuestion.setAnswer(answer.replace(".", "").replace(" ", ""));
                    } else {
                        currentModelQuestion.setAnswer(answer);
                    }
                }
            } else if (text.startsWith("解析：")) {  // 解析
                if (currentModelQuestion != null) {
                    currentModelQuestion.setAnalysis(text.replace("解析：", "").trim());
                    modelQuestions.add(currentModelQuestion);
                    currentModelQuestion = null;
                }
            }
        }

        // 最后一次添加 question（防止最后一个题目未被添加）
        if (currentModelQuestion != null) {
            modelQuestions.add(currentModelQuestion);
        }

        // 插入数据库前的检查
        for (QuestionModel modelQuestion : modelQuestions) {
            if (modelQuestion.getQuestionType() == null || modelQuestion.getQuestionType().isEmpty()) {
                continue;  // 跳过这个问题
            }

            Question entityQuestion = QuestionConverter.convertToEntity(modelQuestion);
            entityQuestion.setFileName(fileName);  // 设置文件名
            questionMapper.insertQuestion(entityQuestion);  // 将题目插入数据库
        }
    }

}
