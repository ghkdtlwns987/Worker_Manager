package com.OJ.domain;

public enum Sample {

    SAMPLE(1,true),
    UN_SAMPLE(0, false),
    ERROR(-1, false);

    Sample(int value, boolean flag) {
        this.value = value;
        this.flag = flag;
    }

    private int value;
    private boolean flag;

    public static Sample findByValue(int value){
        for(Sample sample : Sample.values()){
            if(sample.getValue() == value){
                return sample;
            }
        }

        return ERROR;
    }

    public int getValue() {
        return value;
    }

    public boolean isSample() {
        return flag;
    }
}
