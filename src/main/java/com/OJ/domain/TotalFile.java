package com.OJ.domain;

public class TotalFile {

    private ResultType resultType;
    private int cpuTime;
    private int memoryUsage;
    private String compileErrorMessage;

    public TotalFile(ResultType resultType, int cpuTime, int memoryUsage) {
        this.resultType = resultType;
        this.cpuTime = cpuTime;
        this.memoryUsage = memoryUsage;
    }

    public static TotalFile CompileErrorTotalFile(ResultType resultType, int cpuTime, int memoryUsage, String compileErrorMessage){
        TotalFile totalFile = new TotalFile(resultType, cpuTime, memoryUsage);
        totalFile.setCompileErrorMessage(compileErrorMessage);

        return totalFile;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public int getCpuTime() {
        return cpuTime;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public String getCompileErrorMessage() {
        return compileErrorMessage;
    }

    public void setCompileErrorMessage(String compileErrorMessage) {
        this.compileErrorMessage = compileErrorMessage;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public void setCpuTime(int cpuTime) {
        this.cpuTime = cpuTime;
    }

    public void setMemoryUsage(int memoryUsage) {
        this.memoryUsage = memoryUsage;
    }
}
