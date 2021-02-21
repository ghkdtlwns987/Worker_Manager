package com.OJ.DB.dao.errorInfo;

import com.OJ.DB.DBConnection;
import com.OJ.domain.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErrorInfoRepository {

    private DBConnection dbConnection;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ErrorInfoRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean create(ErrorInfo errorInfo) {
        String insertSql = "INSERT INTO error_info VALUES (?, ?)";

        try (Connection con = dbConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            pstmt.setInt(1, errorInfo.getSubmissionId());
            pstmt.setString(2, errorInfo.getErrorInfo());
            int rowCount = pstmt.executeUpdate();

            logger.info("Number of rows created with " + errorInfo.getSubmissionId() + " error info : " + rowCount);
        } catch (SQLException e) {
            logger.error("[Error Info | SQL] Failed to insert about error info of {} submission", errorInfo.getSubmissionId());
            logger.error("SQLException message ", e);
            return false;
        }

        return true;
    }
}
