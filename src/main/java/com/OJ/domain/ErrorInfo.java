package com.OJ.domain;

public class ErrorInfo {

    private int submissionId;
    private String errorInfo;

    public ErrorInfo(int submissionId, String errorInfo) {
        this.submissionId = submissionId;
        this.errorInfo = errorInfo;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public String getErrorInfo() {
        return errorInfo;
    }
}
