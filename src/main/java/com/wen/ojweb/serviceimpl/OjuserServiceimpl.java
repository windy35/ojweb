package com.wen.ojweb.serviceimpl;

import com.wen.ojweb.dao.OjuserMapper;
import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.service.OjuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OjuserServiceimpl implements OjuserService {
    @Autowired
    public OjuserMapper ojuserMapper;

    public int insertSelective(Ojuser ojuser) {
        return ojuserMapper.insertSelective(ojuser);
    }

    public Ojuser selectByPrimaryKey(String username) {
        return ojuserMapper.selectByPrimaryKey(username);
    }

    public  int updateByPrimaryKey(Ojuser ojuser){
        return ojuserMapper.updateByPrimaryKey(ojuser);
    }

    public  int deleteByPrimaryKey(String username){
        return ojuserMapper.deleteByPrimaryKey(username);
    }

    //模糊查询
    public List<Ojuser> selectByUserName(String username){
        return  ojuserMapper.selectByUserName(username);
    }
    public List<Ojuser> selectBySex(boolean sex){
        return  ojuserMapper.selectBySex(sex);
    }

    @Override
    public List<Ojuser> selectAll() {
        return ojuserMapper.selectAll();
    }
}
