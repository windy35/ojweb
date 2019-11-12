package com.wen.ojweb.serviceimpl;
import com.wen.ojweb.dao.OjuserMapper;
import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceimpl implements LoginService {
    @Autowired
    public OjuserMapper ojuserMapper;
    public Ojuser UserNameLogin(String username) {
        return ojuserMapper.selectByPrimaryKey(username);
    }

    public Ojuser EmailLogin(String email,String password) {
//        return ojuserMapper.EmailLogin(email,password);
        return null;
    }
}
