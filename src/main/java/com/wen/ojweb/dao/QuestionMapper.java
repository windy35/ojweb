package com.wen.ojweb.dao;

import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.model.Question;
import com.wen.ojweb.model.QuestionWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    int deleteByPrimaryKey(Integer questionid);

    int insert(QuestionWithBLOBs record);

    int insertSelective(QuestionWithBLOBs record);

    QuestionWithBLOBs selectByPrimaryKey(Integer questionid);

    int updateByPrimaryKeySelective(QuestionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(QuestionWithBLOBs record);

    int updateByPrimaryKey(Question record);

    //模糊查询
    List<QuestionWithBLOBs> selectByTitle(String title);
    List<QuestionWithBLOBs> selectByQuestionid(int questionid);
}