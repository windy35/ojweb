package com.wen.ojweb.service;

import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.model.Question;
import com.wen.ojweb.model.QuestionWithBLOBs;
import org.springframework.stereotype.Service;

import java.util.List;


public interface QuestionService {
    int insertSelective(QuestionWithBLOBs question);

    QuestionWithBLOBs selectByPrimaryKey(int questionid);

    int updateByPrimaryKey(QuestionWithBLOBs question);

    int deleteByPrimaryKey(int questionid);

    //模糊查询
    List<QuestionWithBLOBs> selectByTitle(String title);

    List<QuestionWithBLOBs> selectByQuesitonId(int questionid);
}
