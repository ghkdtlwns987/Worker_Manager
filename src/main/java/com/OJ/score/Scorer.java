package com.OJ.score;

import com.OJ.domain.*;
import com.OJ.util.errorMessage.RuntimeErrorMessage;
import com.OJ.util.errorMessage.WrongAnswerErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Scorer {

    private Map<String, InFile> inFileMap;
    private Map<String, OutFile> outFileMap;
    private List<UserOutFile> userOutFiles;

    private List<InFile> wrongInFileList;
    private List<OutFile> wrongOutFileList;
    private List<UserOutFile> wrongUserOutFileList;

    private List<InFile> runtimeInFileList;
    private List<UserOutFile> runtimeUserOutFileList;

    private int correctCount;
    private ResultType resultType;
    private String compileErrorMessage;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Scorer(Map<String, InFile> inFileMap, Map<String, OutFile> outFileMap, List<UserOutFile> userOutFiles) {
        this.inFileMap = inFileMap;
        this.outFileMap = outFileMap;
        this.userOutFiles = userOutFiles;
        correctCount = outFileMap.size();

        wrongInFileList = new ArrayList<>();
        wrongOutFileList = new ArrayList<>();
        wrongUserOutFileList = new ArrayList<>();

        runtimeInFileList = new ArrayList<>();
        runtimeUserOutFileList = new ArrayList<>();
    }

    public void run(Problem problem, TotalFile totalFile) {

        logger.info("Grading problem id {}", problem.getId());

        // compile error
        if (totalFile.getResultType().isCompileErrorType()) {
            correctCount = 0;
            resultType = totalFile.getResultType();
            compileErrorMessage = totalFile.getCompileErrorMessage();
            return;
        }

        ResultType result = ResultType.ACCEPT;

        // empty testcase
        if (userOutFiles.size() == 0) {
            correctCount = 0;
            resultType = ResultType.EMPTY_TEST_DATA;
            return;
        }

        for (UserOutFile userOutFile : userOutFiles) {
            logger.debug("Grading No {} space check {}", userOutFile.getNo(), problem.isSpaceCheck());

            ResultType testcaseResultType = ResultType.ACCEPT;
            InFile inFile = inFileMap.get(userOutFile.getNo());
            OutFile outFile = outFileMap.get(userOutFile.getNo());

            if (userOutFile.getResultType() == ResultType.ACCEPT) {
                testcaseResultType = grading(inFile, outFile, userOutFile, problem.isSpaceCheck());
            } else if (userOutFile.getResultType().isWrongAnswerType()) { // MEMORY_LIMIT, OUTPUT_LIMIT
                logger.debug("Wrong Answer logic");

                correctCount--;
                testcaseResultType = userOutFile.getResultType();
                addWrongAnswerObjects(inFile, outFile, userOutFile);
            } else if (userOutFile.getResultType().isRuntimeErrorTyp()) {
                logger.debug("Run Time Error logic");

                correctCount--;
                testcaseResultType = userOutFile.getResultType();
                addRuntimeErrorObjects(inFile, userOutFile);
            }

            result = highPriority(result, testcaseResultType);
        }


        resultType = result;
    }

    private ResultType grading(InFile inFile, OutFile outFile, UserOutFile userOutFile, boolean spaceCheck) {

        ResultType testcaseResultType = ResultType.ACCEPT;

        // Grading without space check
        if (isCorrectWithoutSpace(outFile.getAnswer(), userOutFile.getResult())) {
            // to validate Presentation Error
            if (spaceCheck && !isCorrect(outFile.getAnswer(), userOutFile.getResult())) {
                logger.debug("Presentation Error\n[Answer] - [{}]\n[User output] - [{}]", outFile.getAnswer(), userOutFile.getResult());

                correctCount--;
                testcaseResultType = ResultType.PRESENTATION_ERROR;

                // error message
                addWrongAnswerObjects(inFile, outFile, userOutFile);
            }
        } else { // wrong answer
            logger.debug("Wrong Answer\n[Answer] - [{}]\n[User output] - [{}]", outFile.getAnswer(), userOutFile.getResult());

            correctCount--;
            testcaseResultType = ResultType.WRONG_ANSWER;

            // error message
            addWrongAnswerObjects(inFile, outFile, userOutFile);
        }

        return testcaseResultType;
    }

    private void addWrongAnswerObjects(InFile inFile, OutFile outFile, UserOutFile userOutFile) {
        wrongInFileList.add(inFile);
        wrongOutFileList.add(outFile);
        wrongUserOutFileList.add(userOutFile);
    }

    private void addRuntimeErrorObjects(InFile inFile, UserOutFile userOutFile) {
        runtimeInFileList.add(inFile);
        runtimeUserOutFileList.add(userOutFile);
    }

    private ResultType highPriority(ResultType oldType, ResultType newType) {
        return (oldType.getType() > newType.getType()) ? oldType : newType;
    }

    public float getPassRate() {

        // empty testcase
        if (outFileMap.size() == 0) {
            return 0;
        }

        return (float) correctCount / outFileMap.size();
    }

    public ResultType getResultType() {
        return resultType;
    }

    private boolean isCorrectWithoutSpace(String answer, String userResult) {
        return answer.replaceAll("\\s", "").equals(userResult.replaceAll("\\s", ""));
    }

    private boolean isCorrect(String answer, String userResult) {
        String userAnswer = userResult;

        if (userAnswer.length() != 0 && (userAnswer.charAt(userAnswer.length() - 1) == '\n')) {
            userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
        }

        // window의 경우 추가 작업이 필요
        if (userAnswer.length() != 0 && (userAnswer.charAt(userAnswer.length() - 1) == '\r')) {
            userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
        }

        return answer.equals(userAnswer);
    }

    public String getErrorMessage(ResultType resultType) {
        String result;

        if (resultType.isWrongAnswerType()) {
            result = WrongAnswerErrorMessage.createErrorMessage(wrongInFileList, wrongOutFileList, wrongUserOutFileList);
        } else if (resultType.isRuntimeErrorTyp()) {
            result = RuntimeErrorMessage.createErrorMessage(runtimeInFileList, runtimeUserOutFileList);
        } else { // compile error
            result = compileErrorMessage;
        }

        return result;
    }
}
