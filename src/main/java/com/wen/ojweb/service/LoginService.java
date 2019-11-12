package com.wen.ojweb.service;
import com.wen.ojweb.model.Ojuser;
import org.springframework.stereotype.Service;


public interface LoginService {
    Ojuser UserNameLogin(String username);

    Ojuser EmailLogin(String email,String password);
}
