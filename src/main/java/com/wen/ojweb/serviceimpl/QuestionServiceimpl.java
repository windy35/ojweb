package com.wen.ojweb.serviceimpl;

import com.wen.ojweb.dao.QuestionMapper;
import com.wen.ojweb.model.Question;
import com.wen.ojweb.model.QuestionWithBLOBs;
import com.wen.ojweb.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceimpl implements QuestionService {
    @Autowired
    public QuestionMapper questionMapper;


    @Override
    public int insertSelective(QuestionWithBLOBs question) {
        return questionMapper.insertSelective(question);
    }

    @Override
    public QuestionWithBLOBs selectByPrimaryKey(int questionid) {
        return questionMapper.selectByPrimaryKey(questionid);
    }

    @Override
    public int updateByPrimaryKey(QuestionWithBLOBs question) {
        return questionMapper.updateByPrimaryKey(question);
    }

    @Override
    public int deleteByPrimaryKey(int questionid) {
        return questionMapper.deleteByPrimaryKey(questionid);
    }

    @Override
    public List<QuestionWithBLOBs> selectByTitle(String title) {
        return questionMapper.selectByTitle(title);
    }

    @Override
    public List<QuestionWithBLOBs> selectByQuesitonId(int questionid) {
        return questionMapper.selectByQuestionid(questionid);
    }
}
