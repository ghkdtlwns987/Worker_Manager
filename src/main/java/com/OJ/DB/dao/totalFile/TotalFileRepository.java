package com.OJ.DB.dao.totalFile;

import com.OJ.domain.OJConfig;
import com.OJ.domain.ResultType;
import com.OJ.domain.TotalFile;
import com.OJ.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TotalFileRepository {

    private static String TOTAL_FILE;
    private static String SEPARATOR;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TotalFileRepository(OJConfig config) {
        TOTAL_FILE = config.getTotalFile();
        SEPARATOR = config.getSeparator();
    }

    public TotalFile getTotalFile() throws IOException, NumberFormatException {

        TotalFile result;

        File file = new File(TOTAL_FILE);
        String content = FileUtil.readFile(file.getPath());
        String[] splitContent = content.split(SEPARATOR);

        if (splitContent.length == 4) {
            logger.debug("Total user type is Compile Error");
            result = TotalFile.CompileErrorTotalFile(
                    ResultType.findByType(Integer.parseInt(splitContent[0])),
                    Integer.parseInt(splitContent[1]),
                    Integer.parseInt(splitContent[2]),
                    splitContent[3]
            );

            logger.info("Succeed making total file");
            return result;
        }

        logger.debug("Total user type isn't Compile Error");
        result = new TotalFile(
                ResultType.findByType(Integer.parseInt(splitContent[0])),
                Integer.parseInt(splitContent[1]),
                Integer.parseInt(splitContent[2])
        );

        logger.info("Succeed making total file");
        return result;
    }
}
