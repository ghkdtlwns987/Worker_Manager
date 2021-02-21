package com.OJ.DB.dao.inFile;

import com.OJ.domain.InFile;
import com.OJ.domain.OJConfig;
import com.OJ.domain.Problem;
import com.OJ.domain.Submission;
import com.OJ.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InFileRepository {

    private static String COPY_TESTCASE;

    private static Logger logger = LoggerFactory.getLogger(InFileRepository.class);

    public InFileRepository(OJConfig config) {
        COPY_TESTCASE = config.getCopyTestcasePath();
    }

    public Map<String, InFile> getInFileMap(Problem problem, Submission submission) throws IOException {

        Map<String, InFile> inFileMap = new HashMap<>();
        File copyTestcaseDirectory = new File(COPY_TESTCASE);

        for(File file : copyTestcaseDirectory.listFiles()){
            String extension = FileUtil.getExtension(file.getName());

            if(extension.equals("in")){
                String originFileName = FileUtil.getFileNameWithoutExtension(file.getName());
                inFileMap.put(originFileName, new InFile(originFileName, FileUtil.readFile(file.getAbsolutePath())));
            }
        }

        logger.info("Succeed in making .in Files");

        return inFileMap;
    }
}
