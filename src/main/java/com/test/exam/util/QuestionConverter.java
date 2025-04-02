// 转换器
package com.test.exam.util;

import com.test.exam.entity.Question;
import com.test.exam.model.QuestionModel;
import com.test.exam.dto.QuestionDTO;

public class QuestionConverter {

    // Model 转 Entity
    public static Question convertToEntity(QuestionModel model) {
        Question entity = new Question();
        entity.setQuestionText(model.getQuestionText());
        entity.setOptionA(model.getOptions().get(0));
        entity.setOptionB(model.getOptions().get(1));
        entity.setOptionC(model.getOptions().get(2));
        entity.setOptionD(model.getOptions().get(3));
        entity.setAnswer(model.getAnswer());
        return entity;
    }

    // Entity 转 DTO
    public static QuestionDTO convertToDTO(Question entity) {
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
