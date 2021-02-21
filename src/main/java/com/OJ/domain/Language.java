package com.OJ.domain;

public enum Language {
    C(1, "c"),
    CPP(2, "cpp"),
    JAVA(3, "java"),
    PYTHON2(4, "py"),
    PYTHON3(5, "py"),
    ERROR(0, null);

    private int value;
    private String extension;

    Language(int value, String extension) {
        this.value = value;
        this.extension = extension;
    }

    public static Language findByValue(int value){
        for(Language language : Language.values()){
            if(language.getValue() == value){
                return language;
            }
        }

        return ERROR;
    }

    public int getValue() {
        return value;
    }

    public String getExtension() {
        return extension;
    }
}
