package com.OJ;

import com.OJ.DB.DBConnection;
import com.OJ.domain.OJConfig;
import com.OJ.manager.Manager;
import com.OJ.run.Runner;
import com.OJ.util.FileUtil;
import com.OJ.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        OJConfig config;

        try {
            config = new OJConfig(Integer.parseInt(args[0]));
        } catch (IOException e) {
            logger.error("Failed to read config file");
            logger.error("IOException message ", e);
            return;
        }

        DBConnection db = new DBConnection(config);
        Manager manager = new Manager(config);

        while (true) {
            // 환경 설정
            try {
                FileUtil.initDirectory(config);
                if (!Container.isAlive(config)) {
                    Container.restart(config);
                }

            } catch (IOException | InterruptedException e) {
                logger.error("[Error | setting] Failed to set environment");
                logger.error("Exception message ", e);
                return;
            }

            // OJ Manager와의 작업
            manager.connectOJManager();
            manager.receiveMessage();

            if (manager.getMessage() <= 0) {
                logger.error("Invalid Message");
                manager.closeSocket();
                continue;
            }

            manager.reply();
            manager.closeSocket();

            // Run 작업 - 실행, 채점, 갱신 작업
            Runner runner = new Runner(db, config);
            try {
                runner.run(manager.getMessage());
            } catch (IOException e) {
                logger.error("[Runner | File] Fail to read file");
                logger.error("IOException Message", e);
                return;
            }
        }
    }
}
