package com.OJ.manager;

import com.OJ.domain.OJConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Manager {

    private final static int SLEEP_TIME = 5000; // 5초 대기

    private static String SERVER_IP;
    private static int SERVER_PORT;

    private Socket socket;
    private byte[] buffer;
    private int message;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Manager(OJConfig config) {
        SERVER_IP = config.getServerIp();
        SERVER_PORT = Integer.parseInt(config.getServerPort());
    }

    public void connectOJManager() {
        do {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            } catch (IOException e) {
                logger.error("Retry connecting socket");
                logger.error("IOException Message ", e);
                sleep();
            }
        } while (!socket.isConnected());
    }

    public void receiveMessage() {
        BufferedInputStream br;
        buffer = new byte[4];
        int readSize = 0;

        try {
            br = new BufferedInputStream(socket.getInputStream());
            readSize = br.read(buffer);
        } catch (IOException e) {
            logger.error("[Socket] Failed to received message");
            logger.error("IOException Message ", e);
        }

        logger.debug("Receive Buffer size : " + readSize);
        if (readSize <= 0) {
            message = 0;
        } else {
            message = byteArrayToInt(buffer);
        }

        logger.info("Received Message : {}", message);
    }

    public void reply() {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            bos.write(buffer);
            bos.flush();
        } catch (IOException e) {
            logger.error("[Socket] Failed to reply {}", message);
            logger.error("IOException Message ", e);
        }
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.error("[Socket] Failed to close socket");
            logger.error("IOException Message ", e);
        }
    }

    public int getMessage() {
        return message;
    }

    private int byteArrayToInt(byte[] buffer) {
        return (buffer[3] << 24) + ((buffer[2] & 0xFF) << 16) + ((buffer[1] & 0xFF) << 8) + (buffer[0] & 0xff);
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            logger.error("InterruptedException Message ", e);
        }
    }
}
