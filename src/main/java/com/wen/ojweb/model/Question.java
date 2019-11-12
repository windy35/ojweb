package com.wen.ojweb.model;

import java.util.Date;

public class Question {
    private Integer questionid;

    private Float timelimit;

    private Float memorylimit;

    private String title;

    private Integer submittime;

    private Integer accepttime;

    private String questionsetter;

    private Boolean status;

    private Date updatetime;

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public Float getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Float timelimit) {
        this.timelimit = timelimit;
    }

    public Float getMemorylimit() {
        return memorylimit;
    }

    public void setMemorylimit(Float memorylimit) {
        this.memorylimit = memorylimit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getSubmittime() {
        return submittime;
    }

    public void setSubmittime(Integer submittime) {
        this.submittime = submittime;
    }

    public Integer getAccepttime() {
        return accepttime;
    }

    public void setAccepttime(Integer accepttime) {
        this.accepttime = accepttime;
    }

    public String getQuestionsetter() {
        return questionsetter;
    }

    public void setQuestionsetter(String questionsetter) {
        this.questionsetter = questionsetter == null ? null : questionsetter.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionid=" + questionid +
                ", timelimit=" + timelimit +
                ", memorylimit=" + memorylimit +
                ", title='" + title + '\'' +
                ", submittime=" + submittime +
                ", accepttime=" + accepttime +
                ", questionsetter='" + questionsetter + '\'' +
                ", status=" + status +
                ", updatetime=" + updatetime +
                '}';
    }
}