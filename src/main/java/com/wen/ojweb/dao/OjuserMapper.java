package com.wen.ojweb.dao;

import com.wen.ojweb.model.Ojuser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OjuserMapper {
    int deleteByPrimaryKey(String username);

    int insert(Ojuser record);

    int insertSelective(Ojuser record);

    Ojuser selectByPrimaryKey(String username);

    int updateByPrimaryKeySelective(Ojuser record);

    int updateByPrimaryKeyWithBLOBs(Ojuser record);

    int updateByPrimaryKey(Ojuser record);

    List<Ojuser> selectByUserName(String username);

    List<Ojuser> selectBySex(boolean sex);

    List<Ojuser> selectAll();
}