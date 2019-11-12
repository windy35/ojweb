package com.wen.ojweb.model;

public class QuestionWithBLOBs extends Question {
    private String topicdescribes;

    private String input;

    private String output;

    private String sampleinput;

    private String sampleoutput;

    private String classification;

    public String getTopicdescribes() {
        return topicdescribes;
    }

    public void setTopicdescribes(String topicdescribes) {
        this.topicdescribes = topicdescribes == null ? null : topicdescribes.trim();
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input == null ? null : input.trim();
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output == null ? null : output.trim();
    }

    public String getSampleinput() {
        return sampleinput;
    }

    public void setSampleinput(String sampleinput) {
        this.sampleinput = sampleinput == null ? null : sampleinput.trim();
    }

    public String getSampleoutput() {
        return sampleoutput;
    }

    public void setSampleoutput(String sampleoutput) {
        this.sampleoutput = sampleoutput == null ? null : sampleoutput.trim();
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification == null ? null : classification.trim();
    }

    @Override
    public String toString() {
        return  super.toString() +"QuestionWithBLOBs{" +
                "topicdescribes='" + topicdescribes + '\'' +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", sampleinput='" + sampleinput + '\'' +
                ", sampleoutput='" + sampleoutput + '\'' +
                ", classification='" + classification + '\'' +
                '}';
    }
}