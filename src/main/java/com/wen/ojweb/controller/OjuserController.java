package com.wen.ojweb.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wen.ojweb.model.Ojuser;
import com.wen.ojweb.model.PageJson;
import com.wen.ojweb.model.ResultJSON;
import com.wen.ojweb.service.OjuserService;
import com.wen.ojweb.serviceimpl.OjuserServiceimpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



@Controller
@RequestMapping("/userinfo")
public class OjuserController {
    private static final Log log = LogFactory.getLog(OjuserController.class);
    @Autowired
    public OjuserService ojuserService;
    //增删改查在Restful风格中对应POST、DELETE、PUT、GET
    //对用户信息进行增删查改

    @PostMapping("file")
    @ResponseBody
    public ResultJSON insertFromFile(){
        System.out.println();
        return null;
    }


    /**
     * 增加某个用户
     * @param user
     * @param confirmpw
     * @return
     */
    @PostMapping
    @ResponseBody
    public ResultJSON insertOne(@ModelAttribute Ojuser user , @RequestParam(required = false) String confirmpw) {
        System.out.println(user);
        if(log.isTraceEnabled()) {

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
            if(ojuserService.selectByPrimaryKey(user.getUsername())!=null)
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
                ojuserService.insertSelective(user);
                return ResultJSON.success();
            }
        }
    }

    /**
     * 删除单个或多个用户信息
     * @param arr
     * @param deleteReason
     * @return
     */
    @ResponseBody
    @DeleteMapping("/deleteAll")
    public ResultJSON deleteOne(@RequestParam("arr") String arr, @RequestParam(value="delete_reason", required=false) String deleteReason){
        ResultJSON rs = new ResultJSON();
        if(log.isTraceEnabled()) {
            log.trace("deleteAllUsers ");
        }
        if(!arr.equals(""))
        {
            JSONArray jsonArray=JSONArray.fromObject(arr);//把前台接收的string数组转化为json数组
            List<Ojuser> list = ( List<Ojuser>)jsonArray.toCollection(jsonArray,Ojuser.class);
//        转换为目标对象list
            for(Ojuser ojuser:list){
                ojuserService.deleteByPrimaryKey(ojuser.getUsername());
            }
            rs.setCode(200);
            rs.setMsg("删除成功");
        }
        else
        {
            rs.setCode(500);
            rs.setMsg("删除失败");
        }
        return rs;
        }



    /**
     * 查询所有用户信息
     * @param page
     * @param limit
     * @return
     */
    @GetMapping
    @ResponseBody
    //同时实现表格数据的分页功能，page表示当前页码，limit表示每页显示的数据条目数
    public PageJson<Ojuser> getAllUser(@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int limit, Model model){
        if(log.isTraceEnabled()){
//            log.trace("LoginController被调用了传入的参数有："+ user.getUsername() + " & " + user.getPassword());
        }
        //在查询操作之前使用PageHelper进行分页，传入页码以及分页的大小
        PageHelper.startPage(page,limit);
        //在startPage后面紧跟的查询是一个分页查询
        List<Ojuser> list = ojuserService.selectAll();
        //查询之后使用PageInfo对象对数据进行包装
        PageInfo pageInfo = new PageInfo(list);
        PageJson <Ojuser> pageJson = new PageJson<Ojuser>();
        pageJson.setCode(0);
        pageJson.setMsg("操作成功");
        pageJson.setCount(pageInfo.getTotal());
        pageJson.setData(pageInfo.getList());
        System.out.println(pageJson);
        return pageJson;
    }

    /**
     * 查询单个用户信息
     * @param username
     * @param map
     * @return
     */
    @GetMapping("/{username}")
    public String getOne(@PathVariable String username,ModelMap map){
        if(log.isTraceEnabled()){
            log.trace("getOne被调用了传入的参数有："+ username);
        }
        ResultJSON rs = new ResultJSON();
        Ojuser ojuser = new Ojuser();
        ojuser = ojuserService.selectByPrimaryKey(username);
//        System.out.println(ojuser);
        if (ojuser != null)
        {
            rs.setCode(200);
            rs.setMsg("查询成功");
//           rs.add("userInfo",ojuser);
            map.put("userInfo",ojuser);
        }
        else
        {
            rs.setCode(500);
            rs.setMsg("查询成功");
        }
        return "admin/info/EditUserInfo";
    }

    /**
     * 模糊查询用户信息
     * @param content
     * @param page
     * @param limit
     * @param model
     * @return
     */
    @GetMapping("/Search")
    @ResponseBody
    public PageJson<Ojuser> search(@RequestParam(required = false,defaultValue = "0") String type,@RequestParam(required = false,defaultValue = "") String content,@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int limit, Model model){
        if(log.isTraceEnabled()){
            log.trace("search被调用了传入的参数有："+ type + " "+ content);
        }
        System.out.println("search被调用了传入的参数有："+ type + " "+ content);
        //在查询操作之前使用PageHelper进行分页，传入页码以及分页的大小
        PageHelper.startPage(page,limit);
        //在startPage后面紧跟的查询是一个分页查询
        List<Ojuser> list = new ArrayList<>();
        if(type.equals("0"))
             list = ojuserService.selectByUserName(content);
        else if(type.equals("1"))
        {
            if(content.equals("男"))
                list = ojuserService.selectBySex(true);
            else if(content.equals("女"))
                list = ojuserService.selectBySex(false);
        }
        //查询之后使用PageInfo对象对数据进行包装
        PageInfo pageInfo = new PageInfo(list);
        PageJson <Ojuser> pageJson = new PageJson<Ojuser>();
        pageJson.setCode(0);
        pageJson.setMsg("操作成功");
        pageJson.setCount(pageInfo.getTotal());
        pageJson.setData(pageInfo.getList());
        System.out.println(pageJson);
        return pageJson;
    }

    /**
     * 更新某个用户的信息
     * @param username
     * @param ojuser
     * @return
     */
    @ResponseBody
    @PutMapping("/{username}")
    public ResultJSON updateOne(@PathVariable String username, @ModelAttribute Ojuser ojuser){
        ResultJSON rs = new ResultJSON();
        Ojuser user = ojuserService.selectByPrimaryKey(username);
        System.out.println(user);
        System.out.println("test:  "+ojuser);
        if(log.isTraceEnabled()){
            log.trace("OjuserController的updateOne被调用了");
        }
        if(user == null)
        {
            return ResultJSON.fail();
        }else
        {
            //判断邮箱的正则表达式
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            if(ojuser.getPassword().length()<6 || ojuser.getPassword().length()>20){
                rs.setCode(500);
                rs.setMsg("请输入6~20位的密码");
                System.out.println("请输入6~20位的密码");
            }else if(!regex.matcher(ojuser.getEmail()).matches()){//如果输入的邮箱不符合格式
                rs.setCode(500);
                rs.setMsg("邮箱格式不正确");
                System.out.println("邮箱格式不正确");
            }else{
                user.setSex(ojuser.getSex());
                user.setEmail(ojuser.getEmail());
                user.setIntroduce(ojuser.getIntroduce());
                user.setNickname(ojuser.getNickname());
                user.setPassword(ojuser.getPassword());
                user.setSchool(ojuser.getSchool());
                user.setRole(ojuser.getRole());
                ojuserService.updateByPrimaryKey(user);
                rs.setCode(200);
                rs.setMsg("操作成功");
            }
        }
        return rs;
    }


    /**
     * 重置用户密码为123456
     * @param username
     * @param ojuser
     * @return
     */
    @ResponseBody
    @PutMapping("/ResetPW/{username}")
    public ResultJSON ResetPW(@PathVariable String username, @ModelAttribute Ojuser ojuser){
        ResultJSON rs = new ResultJSON();
        Ojuser user = ojuserService.selectByPrimaryKey(username);
        System.out.println(user);
        System.out.println("test:  "+ojuser);
        if(log.isTraceEnabled()){
            log.trace("OjuserController的ResetPW被调用了");
        }
        if(user == null)
        {
            return ResultJSON.fail();
        }else
        {
            user.setPassword("123456");
            ojuserService.updateByPrimaryKey(user);
            rs.setCode(200);
            rs.setMsg("操作成功");
        }
        return rs;
    }

}
