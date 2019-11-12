package com.wen.ojweb.serviceimpl;
import com.wen.ojweb.dao.OjuserMapper;
import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceimpl implements RegisterService {
    @Autowired
    public OjuserMapper ojuserMapper;
    public void insertOne (Ojuser ojuser){
        ojuserMapper.insert(ojuser);
    }

    public Ojuser selectByPrimaryKey(String username) {
        return ojuserMapper.selectByPrimaryKey(username);
    }

}
