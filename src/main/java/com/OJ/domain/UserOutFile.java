package com.OJ.domain;

public class UserOutFile {

    private String no;
    private String result;
    private ResultType resultType;

    public UserOutFile(String no, String result, int type) {
        this.no = no;
        this.result = result;
        this.resultType = ResultType.findByType(type);
    }

    public String getNo() {
        return no;
    }

    public String getResult() {
        return result;
    }

    public ResultType getResultType() {
        return resultType;
    }
}
