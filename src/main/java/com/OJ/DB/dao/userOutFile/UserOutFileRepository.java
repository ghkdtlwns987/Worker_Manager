package com.OJ.DB.dao.userOutFile;

import com.OJ.domain.OJConfig;
import com.OJ.domain.UserOutFile;
import com.OJ.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserOutFileRepository {

    private static String USER;
    private static String SEPARATOR;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserOutFileRepository(OJConfig config) {
        USER = config.getUserPath();
        SEPARATOR = config.getSeparator();
    }

    public List<UserOutFile> getUserOutFileList() throws IOException, NumberFormatException {
        File userDirectory = new File(USER);
        List<UserOutFile> userOutFileList = new ArrayList<>();

        for (File file : userDirectory.listFiles()) {
            String content = FileUtil.readFile(file.getPath());
            String[] splitContent = content.split(SEPARATOR);
            userOutFileList.add(
                    new UserOutFile(
                            FileUtil.getFileNameWithoutExtension(file.getName()),
                            splitContent[0],
                            Integer.parseInt(splitContent[1])
                    ));
        }

        logger.info("Succeed making user out files");

        return userOutFileList;
    }
}
