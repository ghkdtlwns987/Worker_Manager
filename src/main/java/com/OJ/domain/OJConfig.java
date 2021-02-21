package com.OJ.domain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OJConfig {

    private String env;
    private String driver;
    private String url;
    private String dbUserName;
    private String dbPassword;
    private String serverIp;
    private String serverPort;
    private String workspacePath;
    private String totalFile;
    private String copyTestcasePath;
    private String testcasePath;
    private String userPath;
    private String separator;
    private String containerName;
    private String command;

    public OJConfig(int workerNumber) throws IOException {
        initialization(workerNumber);
    }

    private void initialization(int workerNumber) throws IOException {
        String propertyFilePath = "config/application.properties";
        Properties properties = new Properties();

        InputStream fis = getClass().getClassLoader().getResourceAsStream(propertyFilePath);
        properties.load(fis);

        this.env = properties.getProperty("env");
        this.driver = properties.getProperty("driver");
        this.url = properties.getProperty("url");
        this.dbUserName = properties.getProperty("db_username");
        this.dbPassword = properties.getProperty("db_password");
        this.serverIp = properties.getProperty("server_ip");
        this.serverPort = properties.getProperty("server_port");
        this.workspacePath = properties.getProperty("workspace_path") + workerNumber + File.separator;
        this.totalFile = workspacePath + properties.getProperty("total_file");
        this.copyTestcasePath = workspacePath + properties.getProperty("copy_testcase_path");
        this.testcasePath = properties.getProperty("testcase_path");
        this.userPath = this.workspacePath + properties.getProperty("user_path");
        this.separator = properties.getProperty("separator");
        this.containerName = properties.getProperty("container_name") + workerNumber;
        this.command = properties.getProperty("command");
    }

    public String getEnv() {
        return env;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getTotalFile() {
        return totalFile;
    }

    public String getCopyTestcasePath() {
        return copyTestcasePath;
    }

    public String getUserPath() {
        return userPath;
    }

    public String getSeparator() {
        return separator;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getCommand() {
        return command;
    }

    public String getTestcasePath() {
        return testcasePath;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }
}
