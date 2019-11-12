package com.wen.ojweb.controller;

import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.model.ResultJSON;
import com.wen.ojweb.service.LoginService;
import com.wen.ojweb.service.RegisterService;
import com.wen.ojweb.serviceimpl.LoginServiceimpl;
import com.wen.ojweb.serviceimpl.RegisterServiceimpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

@Controller
public class RegisterController {

    private static final Log log = LogFactory.getLog(RegisterController.class);
    @Autowired
    public RegisterService registerService;
    @Autowired
    public LoginService loginService;

    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResultJSON register(@ModelAttribute Ojuser user , @RequestParam(required = false) String confirmpw, HttpServletRequest request){
        HttpSession session = request.getSession();
        user.setRole(0);//0表示普通用户权限
        if(log.isTraceEnabled()){
            log.trace("register被调用了传入的参数有："+ user.getUsername() + " & " + user.getPassword());
        }
        if(user == null)
        {
            return ResultJSON.fail();
        }
        else
        {
            ResultJSON rs = new ResultJSON();
            //判断邮箱的正则表达式
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);

            System.out.println(user + "  " + confirmpw);
            //判断用户名是否出现重复
            if(registerService.selectByPrimaryKey(user.getUsername())!=null)
            {
                rs.setCode(500);
                rs.setMsg("该用户名已被注册");
                System.out.println("该用户名已被注册");
                return rs;
            }else
            if(user.getPassword().length()<6 || user.getPassword().length()>20){
                rs.setCode(500);
                rs.setMsg("请输入6~20位的密码");
                System.out.println("请输入6~20位的密码");
                return rs;
            }else if(!user.getPassword().equals(confirmpw)){
                rs.setCode(500);
                rs.setMsg("两次输入的密码不一致");
                System.out.println("两次输入的密码不一致");
                return rs;
            }else if(!regex.matcher(user.getEmail()).matches()){//如果输入的邮箱不符合格式
                rs.setCode(500);
                rs.setMsg("邮箱格式不正确");
                System.out.println("邮箱格式不正确");
                return rs;
            }else{
                session.setAttribute("user",user);
                registerService.insertOne(user);
                return ResultJSON.success().add("url","/");
            }
        }
    }

//    @PostMapping
//    public void insert(Ojuser record)
//    {
//        if(log.isTraceEnabled()){
//            log.trace("这里应该写将用户数据插入数据库中的代码，传过来的参数是："+ record);
//        }
//         System.out.println(record);
//    }
}
