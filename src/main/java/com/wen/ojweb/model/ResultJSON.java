package com.wen.ojweb.model;

import java.util.HashMap;
import java.util.Map;

public class ResultJSON {
    private int code;  //状态码，200为成功，500表示失败
    private String msg;//返回信息
    private Map<String,Object> extend = new HashMap<String, Object>();

    public ResultJSON() {
    }

    public ResultJSON(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultJSON add(String key,Object value){
        this.getExtend().put(key,value);
        return this;
    }

    public static ResultJSON success(){
        ResultJSON result = new ResultJSON();
        result.setCode(200);
        result.setMsg("处理成功");
        return result;
    }

    public static ResultJSON fail(){
        ResultJSON result = new ResultJSON();
        result.setCode(500);
        result.setMsg("处理失败");
        return result;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
