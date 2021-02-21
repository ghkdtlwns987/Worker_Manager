package com.OJ.DB.dao.outFile;

import com.OJ.domain.OJConfig;
import com.OJ.domain.OutFile;
import com.OJ.domain.Problem;
import com.OJ.domain.Submission;
import com.OJ.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutFileRepository {

    private static String COPY_TESTCASE;

    private static Logger logger = LoggerFactory.getLogger(OutFileRepository.class);

    public OutFileRepository(OJConfig config) {
        COPY_TESTCASE = config.getCopyTestcasePath();
    }

    public Map<String, OutFile> getOutFileMap(Problem problem, Submission submission) throws IOException {

        Map<String, OutFile> outFileMap = new HashMap<>();
        File copyTestcaseDirectory = new File(COPY_TESTCASE);

        for(File file : copyTestcaseDirectory.listFiles()){
            String extension = FileUtil.getExtension(file.getName());

            if(extension.equals("out")){
                String originFileName = FileUtil.getFileNameWithoutExtension(file.getName());
                outFileMap.put(originFileName, new OutFile(originFileName, FileUtil.readFile(file.getAbsolutePath())));
            }
        }

        logger.info("Succeed in making .out files");

        return outFileMap;
    }
}
