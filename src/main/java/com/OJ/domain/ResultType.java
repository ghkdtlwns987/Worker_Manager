package com.OJ.domain;

public enum ResultType {

    ACCEPT(2, "Accept"),
    WRONG_ANSWER(3, "Wrong Answer"),
    TIME_LIMIT(4, "Runtime_Error"),
    MEMORY_LIMIT(5, "Wrong Answer"),
    COMPILE_ERROR(6, "Compile Error"),
    OUTPUT_LIMIT(8, "Wrong Answer"),
    RUNTIME_ERROR(9, "Runtime_Error"),
    PRESENTATION_ERROR(10, "Wrong Answer"),
    EMPTY_TEST_DATA(11, "Empty test data"),
    JUDGING_ERROR(12, "Judging Error"),
    SYSTEM_CALL_ERROR(13, "Runtime_Error"),
    ERROR(-1, "Error");

    private int type;
    private String information;

    ResultType(int type, String information) {
        this.type = type;
        this.information = information;
    }

    public static ResultType findByType(int type) {
        for (ResultType resultType : ResultType.values()) {
            if (resultType.getType() == type) {
                return resultType;
            }
        }

        return ERROR;
    }

    public boolean isWrongAnswerType() {
        return this == ResultType.WRONG_ANSWER ||
                this == ResultType.MEMORY_LIMIT ||
                this == ResultType.OUTPUT_LIMIT ||
                this == ResultType.PRESENTATION_ERROR;
    }

    public boolean isRuntimeErrorTyp() {
        return this == ResultType.RUNTIME_ERROR ||
                this == ResultType.TIME_LIMIT ||
                this == ResultType.SYSTEM_CALL_ERROR;
    }

    public boolean isCompileErrorType(){
        return this == ResultType.COMPILE_ERROR;
    }

    public boolean hasErrorMassage() {
        return isRuntimeErrorTyp() || isWrongAnswerType() || isCompileErrorType();
    }

    public int getType() {
        return type;
    }

    public String getInformation() {
        return information;
    }
}
