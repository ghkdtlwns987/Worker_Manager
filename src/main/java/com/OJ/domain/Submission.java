package com.OJ.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Submission {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int id;
    private int problemId;
    private int classId;
    private int contentProblemId;
    private Sample sample;
    private ResultType result;
    private float progressRate;
    private float passRate;
    private Language language;
    private int runningTime;
    private int memory;
    private String userId;
    private String sourceCodePath;
    private String ip;
    private String container;

    public Submission(
            int id, int problemId, int classId, int contentProblemId, Sample sample, ResultType result, float progressRate,
            float passRate, Language language, int runningTime, int memory, String userId, String ip, String container, String sourceCodePath) {
        this.id = id;
        this.problemId = problemId;
        this.classId = classId;
        this.contentProblemId = contentProblemId;
        this.sample = sample;
        this.result = result;
        this.progressRate = progressRate;
        this.passRate = passRate;
        this.language = language;
        this.runningTime = runningTime;
        this.memory = memory;
        this.userId = userId;
        this.ip = ip;
        this.container = container;
        this.sourceCodePath = sourceCodePath;
    }

    public void updateGradingResult(ResultType result, float passRate, int runningTime, int memory){
        this.result = result;
        this.passRate = passRate;
        this.runningTime = runningTime;
        this.memory = memory;
    }

    public boolean checkValidation(){

        // submission 유효성 검사
        // problem 유효성 검사
        if(problemId <= 0){
            logger.error("[Judging Error] Invalided " + problemId + " problem id of " + id + " submission id");
            return false;
        }

        // language 유효성 검사
        if(language == Language.ERROR){
            logger.error("[Judging Error] Invalided " + language.getValue() + " language of " + id + " submission id");
            return false;
        }

        // user ID 유효성 검사
        if(userId == null){
            logger.error("[Judging Error] Invalided  user id of " + id + " submission id");
            return false;
        }

        // sample 유효성 검사
        if(sample == Sample.ERROR){
            logger.error("[Judging Error] Invalided sample of " + id + " submission id");
            return false;
        }

        // source code 유효성 검사
        if(sourceCodePath == null){
            logger.error("[Judging Error] Invalided source code of " + id + " submission id");
            return false;
        }

        return true;
    }

    public int getId() {
        return id;
    }

    public int getProblemId(){
        return this.problemId;
    }

    public String getSourceCodePath(){
        return this.sourceCodePath;
    }

    public Language getLanguage() { return this.language; }

    public int getContentProblemId() {
        return contentProblemId;
    }

    public Sample getSample() {
        return sample;
    }

    public String getUserId() {
        return userId;
    }

    public int getClassId() {
        return classId;
    }

    public ResultType getResult() {
        return result;
    }

    public float getProgressRate() {
        return progressRate;
    }

    public float getPassRate() {
        return passRate;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public int getMemory() {
        return memory;
    }

    public String getIp() {
        return ip;
    }

    public String getContainer() {
        return container;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", problemId=" + problemId +
                ", classId=" + classId +
                ", contentProblemId=" + contentProblemId +
                ", sample=" + sample +
                ", result=" + result +
                ", progressRate=" + progressRate +
                ", passRate=" + passRate +
                ", language=" + language +
                ", runningTime=" + runningTime +
                ", memory=" + memory +
                ", userId='" + userId + '\'' +
                ", ip='" + ip + + '\'' +
                ", container=" + container +
                ", sourceCodePath='" + sourceCodePath + '\'' +
                '}';
    }
}
