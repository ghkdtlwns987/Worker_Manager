package com.OJ.container;

import com.OJ.domain.OJConfig;
import com.OJ.domain.Problem;
import com.OJ.domain.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Container {

    private static Logger logger = LoggerFactory.getLogger(Container.class);

    public static void run(OJConfig config, Problem problem, Submission submission) throws InterruptedException, IOException {

        String[] command = new String[]{
                "docker", "exec", config.getContainerName(), config.getCommand(),
                config.getEnv(),
                String.valueOf(submission.getLanguage().getValue()),
                String.valueOf(problem.getTimeLimit()),
                String.valueOf(problem.getMemoryLimit())
        };

        logger.debug("Docker command {}", String.join(" ", command));

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        String output = printStream(process);

        logger.debug("Docker stand out put\n{}", output);
        logger.info("Success running container");
    }

    public static boolean isAlive(OJConfig config) throws IOException, InterruptedException {
        String[] command = {"docker", "ps", "--filter", "Status=running", "--format", "{{.Names}}"};

        logger.debug("Docker command {}", String.join(" ", command));

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        String containerList = printStream(process);

        logger.debug("Docker stand out put\n{}", containerList);

        for (String container : containerList.split(System.getProperty("line.separator"))) {
            if (container.equals(config.getContainerName())) {
                return true;
            }
        }

        return false;
    }

    public static void restart(OJConfig config) throws IOException, InterruptedException {
        String[] command = {"docker", "restart", config.getContainerName()};

        logger.debug("Docker command {}", String.join(" ", command));

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        String containerList = printStream(process);

        logger.debug("Docker stand out put\n{}", containerList);
        logger.info("{} restart", config.getContainerName());
    }

    private static String printStream(Process process) throws IOException, InterruptedException {
        process.waitFor();

        InputStream input = process.getInputStream();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        int n = 0;

        while ((n = input.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, n));
        }

        return sb.toString();
    }
}
