package com.wen.ojweb.service;
import com.wen.ojweb.model.Ojuser;

public interface RegisterService {
    void insertOne (Ojuser ojuser);

    Ojuser selectByPrimaryKey(String username);

}
