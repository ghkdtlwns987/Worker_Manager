package com.OJ.DB;

import com.OJ.domain.OJConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {


    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DBConnection(OJConfig config) {
        DRIVER = config.getDriver();
        URL = config.getUrl();
        USERNAME = config.getDbUserName();
        PASSWORD = config.getDbPassword();
    }

    public Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            logger.error("[DB] connection failed");
            logger.error("Exception Message ", e);
        }

        return conn;
    }
}
