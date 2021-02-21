package com.OJ.DB.dao.problem;

import com.OJ.DB.DBConnection;
import com.OJ.domain.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProblemRepository {

    private DBConnection dbConnection;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProblemRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Problem getProblemById(int problemId) {
        Problem entity = null;
        String sql = "SELECT time_limit, memory_limit, spacecheck " +
                "FROM Problem " +
                "WHERE ID = ?";

        try (Connection con = dbConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, problemId);
            try (ResultSet rs = pstmt.executeQuery()) {

                rs.next();
                entity = new Problem(
                        problemId,
                        rs.getInt("time_limit"),
                        rs.getInt("memory_limit"),
                        rs.getBoolean("spacecheck")
                );
            }
        } catch (SQLException e) {
            logger.error("[Problem repository | SQL] Failed to get {} problem", problemId);
            logger.error("SQLException ", e);
        }

        return entity;
    }
}
