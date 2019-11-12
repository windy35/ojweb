package com.wen.ojweb.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wen.ojweb.model.*;
import com.wen.ojweb.service.QuestionService;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/questioninfo")
public class QuestionController {
    private static final Log log = LogFactory.getLog(QuestionController.class);
    @Autowired
    public QuestionService questionService;
    //增删改查在Restful风格中对应POST、DELETE、PUT、GET

    /**
     * 添加题目
     * @param question
     * @return
     */
    @PostMapping
    @ResponseBody
    public ResultJSON AddQuestion(@ModelAttribute QuestionWithBLOBs question,  HttpServletRequest request){
        HttpSession session = request.getSession(true);
        Ojuser user = (Ojuser) session.getAttribute("user");

        ResultJSON rs = new ResultJSON();
        if(question!=null){
            question.setQuestionsetter(user.getUsername());
            question.setUpdatetime(new Date());
            questionService.insertSelective(question);
            rs.setCode(200);
            rs.setMsg("添加成功");
            System.out.println(question);
            rs.add("q",question);
            return rs;
        }
        rs.setCode(500);
        rs.setMsg("操作失败");
        return rs;
    }


    /**
     * 删除单个或多个题目信息
     * @param arr
     * @param deleteReason
     * @return
     */
    @ResponseBody
    @DeleteMapping("/deleteAll")
    public ResultJSON deleteOne(@RequestParam("arr") String arr, @RequestParam(value="delete_reason", required=false) String deleteReason){
        ResultJSON rs = new ResultJSON();
        if(log.isTraceEnabled()) {
            log.trace("deleteAllQuestions ");
        }
        if(!arr.equals(""))
        {
            JSONArray jsonArray=JSONArray.fromObject(arr);//把前台接收的string数组转化为json数组
            List<QuestionWithBLOBs> list = ( List<QuestionWithBLOBs>)jsonArray.toCollection(jsonArray,QuestionWithBLOBs.class);
//        转换为目标对象list
            for(QuestionWithBLOBs question:list){
                questionService.deleteByPrimaryKey(question.getQuestionid());
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
     * 查询单个题目信息
     * @param questionid
     * @param map
     * @return
     */
    @GetMapping("/{questionid}")
    public String getOne(@PathVariable int questionid,ModelMap map){
        if(log.isTraceEnabled()){
            log.trace("getOne被调用了传入的参数有："+ questionid);
        }
        ResultJSON rs = new ResultJSON();
        QuestionWithBLOBs question = new QuestionWithBLOBs();
        question = questionService.selectByPrimaryKey(questionid);
        if (question != null)
        {
            rs.setCode(200);
            rs.setMsg("查询成功");
            map.put("questionInfo",question);
        }
        else
        {
            rs.setCode(500);
            rs.setMsg("查询成功");
        }
        return "admin/info/EditQuestionInfo";
    }
    /**
     * 模糊查询题目信息
     * @param content
     * @param page
     * @param limit
     * @param model
     * @return
     */
    @GetMapping("/SearchQuestion")
    @ResponseBody
    public PageJson<QuestionWithBLOBs> search(@RequestParam(required = false,defaultValue = "0") String type, @RequestParam(required = false,defaultValue = "") String content, @RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int limit, @RequestParam(required = false,defaultValue = "") String timestamp, Model model){
        if(log.isTraceEnabled()){
            log.trace("search被调用了传入的参数有："+ type + " "+ content);
        }
        System.out.println("search被调用了传入的参数有："+ type + " "+ content);
        //在查询操作之前使用PageHelper进行分页，传入页码以及分页的大小
        PageHelper.startPage(page,limit);
        //在startPage后面紧跟的查询是一个分页查询
        List<QuestionWithBLOBs> list = new ArrayList<>();
        if(type.equals("0"))
            list = questionService.selectByTitle(content);
        else if(type.equals("1"))
        {
            list = questionService.selectByQuesitonId(Integer.parseInt(content));
        }
        //查询之后使用PageInfo对象对数据进行包装
        PageInfo pageInfo = new PageInfo(list);
        PageJson <QuestionWithBLOBs> pageJson = new PageJson<QuestionWithBLOBs>();
        pageJson.setCode(0);
        pageJson.setMsg("操作成功");
        pageJson.setCount(pageInfo.getTotal());
        pageJson.setData(pageInfo.getList());
        System.out.println(pageJson);
        return pageJson;
    }

    /**
     * 修改题目状态
     * @param questionid
     * @return
     */
    @PutMapping("/ChangeStatus")
    @ResponseBody
    public ResultJSON changeStatus(@RequestParam int questionid){
        ResultJSON rs = new ResultJSON();
        if(questionid!=0)
        {
            QuestionWithBLOBs question = new QuestionWithBLOBs();
            question = questionService.selectByPrimaryKey(questionid);
            question.setStatus(!question.getStatus());
            questionService.updateByPrimaryKey(question);
            rs.setCode(200);
            rs.setMsg("修改成功");
            System.out.println(question.getStatus());
            rs.add("status",question.getStatus());
            return rs;
        }
        rs.setCode(500);
        rs.setMsg("修改失败");
        return rs;
    }

    /**
     * 更新某个题目的信息
     * @param questionid
     * @param question
     * @return
     */
    @ResponseBody
    @PutMapping("/{questionid}")
    public ResultJSON updateOne(@PathVariable int questionid, @ModelAttribute QuestionWithBLOBs question,@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int limit){
        ResultJSON rs = new ResultJSON();
        QuestionWithBLOBs q = questionService.selectByPrimaryKey(questionid);
        System.out.println(question);
        System.out.println("test:  "+q);
        if(log.isTraceEnabled()){
            log.trace("QuestionController的updateOne被调用了");
        }
        if(question == null)
        {
            return ResultJSON.fail();
        }else
        {
            q.setTimelimit(question.getTimelimit());
            q.setMemorylimit(question.getMemorylimit());
            q.setTitle(question.getTitle());
            q.setSubmittime(question.getSubmittime());
            q.setTopicdescribes(question.getTopicdescribes());
            q.setInput(question.getInput());
            q.setOutput(question.getOutput());
            q.setSampleinput(question.getSampleinput());
            q.setSampleoutput(question.getSampleoutput());
            q.setClassification(question.getClassification());
            q.setUpdatetime(new Date());
            questionService.updateByPrimaryKey(q);
            rs.setCode(200);
            rs.setMsg("操作成功");
            rs.add("q",q);
        }
        return rs;
    }
}
