package com.OJ.util;

import com.OJ.domain.Language;
import com.OJ.domain.OJConfig;
import com.OJ.domain.Problem;
import com.OJ.domain.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtil {

    private static final int BUFFER_SIZE = 1024;
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        int size = 0;
        char[] buffer = new char[BUFFER_SIZE];
        StringBuilder sb = new StringBuilder();
        while((size = br.read(buffer, 0, BUFFER_SIZE)) != -1){
            sb.append(new String(buffer, 0, size));
        }

        return sb.toString();
    }

    public static void initDirectory(OJConfig config) throws IOException {

        // workspace folder clean
        deleteDirectory(config.getWorkspacePath());

        // mkdir testcase, user
        Files.createDirectory(Paths.get(config.getCopyTestcasePath()));
        Files.createDirectory(Paths.get(config.getUserPath()));

        logger.debug("Finish initDirectory");
    }

    private static void deleteDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteDirectory(file.getPath()); // 하위 directory 비우기
                file.delete();
            }
        }
    }

    public static void copyFiles(OJConfig config, Problem problem, Submission submission) throws IOException, NullPointerException {

        String sourceDirectoryPath = getSourceDirectoryPath(config, problem, submission);
        String destinationDirectoryPath = config.getCopyTestcasePath();

        logger.debug("Copy from {} to {}", sourceDirectoryPath, destinationDirectoryPath);

        InputStream inputStream;
        OutputStream outputStream;

        File sourceDirectory = new File(sourceDirectoryPath);
        for (File file : Objects.requireNonNull(sourceDirectory.listFiles())) {
            String sourceFilePath = file.getAbsolutePath();
            String destinationFilePath = destinationDirectoryPath + file.getName();
            File copyFile = new File(destinationFilePath);

            inputStream = new FileInputStream(sourceFilePath);
            outputStream = new FileOutputStream(copyFile);

            int length;
            while ((length = inputStream.read()) != -1) {
                outputStream.write(length);
            }

            inputStream.close();
            outputStream.close();
        }

        logger.info("Copy {} problem directory succeeded", problem.getId());
    }

    private static String getSourceDirectoryPath(OJConfig config, Problem problem, Submission submission) {

        StringBuilder directoryPath = new StringBuilder();
        int parentDirectory = problem.getId() / 1000;

        directoryPath.append(config.getTestcasePath()).append(parentDirectory).append(File.separator).append(problem.getId());
        if (submission.getSample().isSample()) {
            directoryPath.append("_s");
        }

        return directoryPath.toString();
    }

    /**
     * Main.X 파일을 만드는 역할
     *
     * @param sourceCode
     * @param language
     * @return Main 파일의 경로, null -> 실패 했을 때
     */
    public static String createMainFile(OJConfig config, String sourceCode, Language language) {

        // language 유효성 검사
        if (language == Language.ERROR) {
            return null;
        }

        StringBuilder filePath = new StringBuilder();
        filePath.append(config.getWorkspacePath()).append("Main.").append(language.getExtension());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath.toString());
            fileOutputStream.write(sourceCode.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error("[SQL] Failed to create Main file");
            logger.error("IOException Message ", e);
            return null;
        }

        return filePath.toString();
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public static String getFileNameWithoutExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(0, index);
    }

    private static String removeLastNewLine(String content) {
        String result;

        result = content.substring(0, content.length() - 1);

        // window환경인 경우 추가로 제거를 해줘야 한다.
        if (result.charAt(result.length() - 1) == '\r') {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
