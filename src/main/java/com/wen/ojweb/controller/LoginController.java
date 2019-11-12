package com.wen.ojweb.controller;

import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.model.ResultJSON;
import com.wen.ojweb.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    private static final Log log = LogFactory.getLog(LoginController.class);
    @Autowired
    public LoginService loginService;

    @ResponseBody
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultJSON getOne(@ModelAttribute Ojuser user, HttpServletRequest request,Model model){
        ResultJSON rs = new ResultJSON();
        if(log.isTraceEnabled()){
            log.trace("LoginController被调用了传入的参数有："+ user.getUsername() + " & " + user.getPassword());
        }
        Ojuser ojuser = loginService.UserNameLogin(user.getUsername());
        if(ojuser==null){
            rs.setCode(500);
            rs.setMsg("该用户不存在");
            return  rs;
        }else{
            if(!ojuser.getPassword().equals(user.getPassword())){
                rs.setCode(500);
                rs.setMsg("密码错误");
                return  rs;
            }
            else{
                rs.setCode(200);
                rs.setMsg("登陆成功！");
                rs.add("url","/admin/main").add("user",ojuser);
                HttpSession session = request.getSession(true);
                session.setAttribute("user",ojuser);
                System.out.println(ojuser);
                return  rs;
            }
        }
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST, RequestMethod.GET})
    public String logout(HttpServletRequest request){
        ResultJSON rs = new ResultJSON();
        HttpSession session = request.getSession();
        Ojuser student = (Ojuser) session.getAttribute("user");
        if(null != student) {
            session.removeAttribute("user");//清除session中的内容
        }
        return "redirect:/";
    }


}
