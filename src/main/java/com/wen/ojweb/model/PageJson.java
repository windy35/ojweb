package com.wen.ojweb.model;

import java.util.List;

public class PageJson<T> {
    private int code;
    private String msg;
    private long count;
    private List<T> data;

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setCount(long count) {
        this.count = count;
    }
    public long getCount() {
        return count;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
    public List<T> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PageJson{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
