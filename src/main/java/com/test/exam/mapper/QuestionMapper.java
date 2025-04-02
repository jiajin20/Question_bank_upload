package com.test.exam.mapper;


import com.test.exam.entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, answer) " +
            "VALUES (#{questionText}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, #{answer})")
    void insertQuestion(Question question);
}
