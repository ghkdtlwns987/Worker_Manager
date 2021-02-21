package com.OJ.run;

import com.OJ.DB.DBConnection;
import com.OJ.DB.dao.errorInfo.ErrorInfoRepository;
import com.OJ.DB.dao.outFile.OutFileRepository;
import com.OJ.DB.dao.problem.ProblemRepository;
import com.OJ.DB.dao.submission.SubmissionRepository;
import com.OJ.DB.dao.inFile.InFileRepository;
import com.OJ.DB.dao.totalFile.TotalFileRepository;
import com.OJ.DB.dao.userOutFile.UserOutFileRepository;
import com.OJ.domain.*;
import com.OJ.score.Scorer;
import com.OJ.util.FileUtil;
import com.OJ.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Runner {

    private OJConfig config;
    private SubmissionRepository submissionRepository;
    private ProblemRepository problemRepository;
    private ErrorInfoRepository errorInfoRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Runner(DBConnection dbConnection, OJConfig config) {
        submissionRepository = new SubmissionRepository(dbConnection);
        problemRepository = new ProblemRepository(dbConnection);
        errorInfoRepository = new ErrorInfoRepository(dbConnection);
        this.config = config;
    }

    public void run(int receivedSubmissionId) throws IOException {

        Submission submission = submissionRepository.getSubmissionInfoById(receivedSubmissionId, config);
        if (isJudgingError(submission)) {
            submission.updateGradingResult(ResultType.JUDGING_ERROR, 0, 0, 0);
            submissionRepository.update(submission);
            return;
        }

        Problem problem = problemRepository.getProblemById(submission.getProblemId());
        if (isEmptyTestcase(submission, problem)) {
            submission.updateGradingResult(ResultType.EMPTY_TEST_DATA, 0, 0, 0);
            submissionRepository.update(submission);
            return;
        }

        // in, out file read
        InFileRepository inFileRepository = new InFileRepository(config);
        OutFileRepository outFileRepository = new OutFileRepository(config);

        Map<String, InFile> inFileMap = inFileRepository.getInFileMap(problem, submission);
        Map<String, OutFile> outFileMap = outFileRepository.getOutFileMap(problem, submission);

        // container 실행
        try {
            Container.run(config, problem, submission);
        } catch (IOException | InterruptedException e) {
            logger.error("[Runner | Worker] Failed to run container of {} submission", submission.getId());
            logger.error("Exception message ", e);
            return;
        }

        // read total.user, X.user
        TotalFileRepository totalFileRepository = new TotalFileRepository(config);
        TotalFile totalFile;
        try {
            totalFile = totalFileRepository.getTotalFile();
        } catch (IOException | NumberFormatException e) {
            logger.error("[Runner | File] Failed to read total file");
            logger.error("Exception message ", e);
            return;
        }

        UserOutFileRepository userOutFileRepository = new UserOutFileRepository(config);
        List<UserOutFile> userOutFiles;
        try {
            userOutFiles = userOutFileRepository.getUserOutFileList();
        } catch (IOException | NumberFormatException e) {
            logger.error("[Runner | File] Failed to read user out files");
            logger.error("Exception message ", e);
            return;
        }

        // 채점
        logger.info("pre-scoring result : {}", totalFile.getResultType().getType());

        Scorer scorer = new Scorer(inFileMap, outFileMap, userOutFiles);
        scorer.run(problem, totalFile);

        // worker의 결과와 채점 후 결과 비교
        ResultType finalResultType = (scorer.getResultType().getType() > totalFile.getResultType().getType()) ? scorer.getResultType() : totalFile.getResultType();

        logger.info("post-scoring result : {}", scorer.getResultType().getType());

        // error info update
        if(!updateErrorInfo(submission, scorer, finalResultType)){
            return;
        }

        // 결과 update -> DB
        submission.updateGradingResult(finalResultType, scorer.getPassRate(), totalFile.getCpuTime(), totalFile.getMemoryUsage());
        submissionRepository.update(submission);
    }

    private boolean isEmptyTestcase(Submission submission, Problem problem) throws IOException {

        try {
            FileUtil.copyFiles(config, problem, submission);
        } catch (NullPointerException e) {
            logger.error("[Runner | Copy] Not existed Directory {}, sample {}", problem.getId(), problem.isSpaceCheck());
            logger.error("NullPointerException Message", e);
            return true;
        }

        return false;
    }

    private boolean isJudgingError(Submission submission) {
        return (submission == null) || !submission.checkValidation();
    }

    private boolean updateErrorInfo(Submission submission, Scorer scorer, ResultType resultType) {
        if (!resultType.hasErrorMassage()) {
            return true;
        }

        ErrorInfo errorInfo = new ErrorInfo(submission.getId(), scorer.getErrorMessage(resultType));
        return errorInfoRepository.create(errorInfo);
    }
}
