package com.OJ.DB.dao.submission;

import com.OJ.DB.DBConnection;
import com.OJ.domain.*;
import com.OJ.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubmissionRepository {

    DBConnection dbConnection;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SubmissionRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void update(Submission submission) {
        String sql = "UPDATE " +
                "submission " +
                "SET " +
                "result = ?, pass_rate = ?, progress_rate = ?, running_time = ?, memory = ?, ip = ?, container = ? WHERE ID = ?";

        try (Connection con = dbConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, submission.getResult().getType());
            pstmt.setFloat(2, submission.getPassRate());
            pstmt.setFloat(3, 1);
            pstmt.setInt(4, submission.getRunningTime());
            pstmt.setInt(5, submission.getMemory() >> 10);
            pstmt.setString(6, submission.getIp());
            pstmt.setString(7, submission.getContainer());
            pstmt.setInt(8, submission.getId());

            logger.info("Number of rows updated with " + submission.getId() + " submission : " + pstmt.executeUpdate());
        } catch (SQLException e) {
            logger.error("[Runner | SQL] Failed to update {} submission", submission.getId());
            logger.error("Submission info\n{}", submission.toString());
            logger.error("SQLException message ", e);
        }
    }

    public Submission getSubmissionInfoById(int submissionId, OJConfig config) {
        Submission entity = null;
        String sql = "SELECT " +
                "sb.ID, sb.classID, sb.C_P_ID, sb.problemID, sb.sample, sb.result, " +
                "sb.progress_rate, sb.pass_rate, sb.language, sb.running_time, sb.memory, sb.userID, " +
                "sc.sourceCode " +
                "FROM " +
                "submission as sb, sourceCode as sc " +
                "WHERE " +
                "sb.ID = ? AND sb.ID = sc.submissionID";

        try (Connection con = dbConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, submissionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                Language language = Language.findByValue(rs.getInt("language"));
                Sample sample = Sample.findByValue(rs.getInt("sample"));

                entity = new Submission(
                        rs.getInt("ID"),
                        rs.getInt("problemID"),
                        rs.getInt("classID"),
                        rs.getInt("C_P_ID"),
                        sample,
                        ResultType.findByType(rs.getInt("result")),
                        rs.getFloat("progress_rate"),
                        rs.getFloat("pass_rate"),
                        language,
                        rs.getInt("running_time"),
                        rs.getInt("memory"),
                        rs.getString("userID"),
                        findIp(),
                        config.getContainerName(),
                        FileUtil.createMainFile(config, rs.getString("sourceCode"), language)
                );
            }
        } catch (SQLException e) {
            logger.error("[Submission repository | SQL] Failed to get submission information of {}", submissionId);
            logger.error("SQLException Message ", e);
        }

        return entity;
    }

    private String findIp() {
        String ip = "0.0.0.0";

        try {
            URL url = new URL("http://bot.whatismyipaddress.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            ip = br.readLine().trim();
        } catch (IOException e) {
            logger.error("IOException ", e);
        }

        return ip;
    }
}
