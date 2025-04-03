package com.test.exam.mapper;

import com.test.exam.entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuestionMapper {

      void insertQuestion(Question question);


    @Select("SELECT * FROM questions WHERE id = #{id}")
    Question findById(Long id);
}
