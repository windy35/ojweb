package com.wen.ojweb.service;

import com.wen.ojweb.model.Ojuser;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OjuserService {
    int insertSelective(Ojuser ojuser);

    Ojuser selectByPrimaryKey(String username);

    int updateByPrimaryKey(Ojuser ojuser);

    int deleteByPrimaryKey(String username);

    //模糊查询
    List<Ojuser> selectByUserName(String username);

    List<Ojuser> selectBySex(boolean sex);

    List<Ojuser> selectAll();
}
