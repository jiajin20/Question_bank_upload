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

            // 调试输出解析的段落内容
            System.out.println("解析段落内容: " + text);

            // 判断题目类别，匹配更灵活
            if (text.contains("选择题")) {  // 匹配“选择题”
                currentCategory = "single_choice";
                System.out.println("题目类别: 选择题");
                continue;  // 跳过当前行，继续处理下一个段落
            } else if (text.contains("多选题")) {  // 匹配“多选题”
                currentCategory = "multiple_choice";
                System.out.println("题目类别: 多选题");
                continue;
            } else if (text.contains("判断题")) {  // 匹配“判断题”
                currentCategory = "true_false";
                System.out.println("题目类别: 判断题");
                continue;
            } else if (text.contains("问答题")) {  // 匹配“问答题”
                currentCategory = "short_answer";
                System.out.println("题目类别: 问答题");
                continue;
            }

            // 确保 currentCategory 不为 null
            if (currentCategory == null) {
                System.out.println("警告：没有设置题目类别，跳过解析！");
                continue;  // 跳过当前行
            }

            // 解析题目文本
            if (text.matches("^[0-9]+\\..*")) {  // 题目
                if (currentModelQuestion != null) {
                    modelQuestions.add(currentModelQuestion);
                    System.out.println("添加题目到 modelQuestions: " + currentModelQuestion.getQuestionText());
                }
                currentModelQuestion = new QuestionModel();
                currentModelQuestion.setOptions(new ArrayList<>());
                currentModelQuestion.setQuestionText(text);
                currentModelQuestion.setQuestionType(currentCategory);  // 设置当前题目类型
            } else if (text.matches("^[A-D]\\..*")) {  // 选项
                if (currentModelQuestion != null) {
                    currentModelQuestion.getOptions().add(text);
                    System.out.println("添加选项: " + text);
                }
            } else if (text.startsWith("答案：")) {  // 解析答案
                if (currentModelQuestion != null) {
                    String answer = text.replace("答案：", "").trim();
                    if (currentModelQuestion.getQuestionType().equals("multiple_choice")) {
                        // 对多选题的答案进行处理，例如将 "A.B.C.D." 转换为数组
                        currentModelQuestion.setAnswer(answer.replace(".", "").replace(" ", ""));
                    } else {
                        currentModelQuestion.setAnswer(answer);
                    }
                    System.out.println("设置答案: " + answer);
                }
            } else if (text.startsWith("解析：")) {  // 解析
                if (currentModelQuestion != null) {
                    currentModelQuestion.setAnalysis(text.replace("解析：", "").trim());
                    modelQuestions.add(currentModelQuestion);
                    System.out.println("设置解析: " + currentModelQuestion.getAnalysis());
                    currentModelQuestion = null;
                }
            }
        }

        // 最后一次添加 question（防止最后一个题目未被添加）
        if (currentModelQuestion != null) {
            modelQuestions.add(currentModelQuestion);
        }

        // 打印 modelQuestions 确认数据
        if (modelQuestions.isEmpty()) {
            System.out.println("警告：没有解析到任何题目！");
        } else {
            System.out.println("解析到的题目数量：" + modelQuestions.size());
        }

        // 插入数据库前的检查
        for (QuestionModel modelQuestion : modelQuestions) {
            if (modelQuestion.getQuestionType() == null || modelQuestion.getQuestionType().isEmpty()) {
                System.out.println("错误：题目类型不能为空，跳过存储！题目：" + modelQuestion.getQuestionText());
                continue;  // 跳过这个问题
            }

            Question entityQuestion = QuestionConverter.convertToEntity(modelQuestion);
            System.out.println("准备插入数据库的题目：" + entityQuestion.getQuestionText());
            questionMapper.insertQuestion(entityQuestion);
        }
    }

}
