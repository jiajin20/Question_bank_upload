package com.test.exam.util;

import com.test.exam.entity.Question;
import com.test.exam.model.QuestionModel;
import com.test.exam.dto.QuestionDTO;
import java.util.List;

public class QuestionConverter {

    // 将 QuestionModel 转换为 Question 实体类
    public static Question convertToEntity(QuestionModel model) {
        Question entity = new Question();
        entity.setQuestionText(model.getQuestionText());
        entity.setQuestionType(model.getQuestionType()); // 题目类型


        // 只有单选、多选才需要选项
        if ("single_choice".equals(model.getQuestionType()) || "multiple_choice".equals(model.getQuestionType())) {
            List<String> options = model.getOptions();
            entity.setOptionA(options.size() > 0 ? options.get(0) : null);
            entity.setOptionB(options.size() > 1 ? options.get(1) : null);
            entity.setOptionC(options.size() > 2 ? options.get(2) : null);
            entity.setOptionD(options.size() > 3 ? options.get(3) : null);
        } else {
            // 其他类型题目，选项为空
            entity.setOptionA(null);
            entity.setOptionB(null);
            entity.setOptionC(null);
            entity.setOptionD(null);
        }

        entity.setAnswer(model.getAnswer());
        entity.setAnalysis(model.getAnalysis());

        return entity;
    }

    // 将 Question 实体类转换为 DTO
    public static QuestionDTO convertToDTO(Question entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        QuestionDTO dto = new QuestionDTO();
        dto.setQuestionText(entity.getQuestionText());
        dto.setOptionA(entity.getOptionA());
        dto.setOptionB(entity.getOptionB());
        dto.setOptionC(entity.getOptionC());
        dto.setOptionD(entity.getOptionD());
        dto.setAnswer(entity.getAnswer());
        return dto;
    }
}
