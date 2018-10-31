package com.study.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

/**
 * @ClassName: FileTCPServer
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/10/30 16:18
 * @Version: 1.0
 */
public class FileTCPServer extends ServerSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTCPClient.class);

    private static final int SERVER_PORT = 8888; // 服务端端口

    private static DecimalFormat df = null;

    static {
        // 设置数字格式，保留一位有效小数
        df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
    }

    public FileTCPServer() throws Exception {
        super(SERVER_PORT);
    }

    /**
     * 使用线程处理每个客户端传输的文件
     *
     * @throws Exception
     */
    public void load() throws Exception {
        while (true) {
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
            Socket socket = this.accept();
            /**
             * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
             * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
             * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式
             */
            // 每接收到一个Socket就建立一个新的线程来处理它
            new Thread(new Task(socket)).start();
        }
    }

    /**
     * 处理客户端传输过来的文件线程类
     */
    class Task implements Runnable {

        private Socket socket;

        private DataInputStream dis;

        private FileOutputStream fos;

        private DataOutputStream dos;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());

                //获取文件长度
                byte[] lenBytes = new byte[4];
                dis.read(lenBytes,0,lenBytes.length);
                int len = ByteConvert.bytesToInt(lenBytes);
                long fileLength = (long)len;
                LOGGER.info("len=="+len);

                byte[] nameLenBytes = new byte[4];
                dis.read(nameLenBytes,0,nameLenBytes.length);
                int nameLen = ByteConvert.bytesToInt(nameLenBytes);
                LOGGER.info("nameLen=="+nameLen);

                // 文件名
                byte[] nameBytes = new byte[nameLen];
                dis.read(nameBytes,0,nameLen);
                String fileName = new String(nameBytes);
                LOGGER.info("fileName=="+fileName);

                File directory = new File("D:\\FTCache");
                if (!directory.exists()) {
                    directory.mkdir();
                }
                System.out.println(directory.getAbsolutePath() + File.separatorChar + fileName);
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                fos = new FileOutputStream(file);

                // 开始接收文件
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }
                LOGGER.info("======== 文件接收成功 [File Name：" + fileName + "] [Size：" + getFormatFileSize(fileLength) + "] ========");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    socket.close();
                } catch (Exception e) {
                    LOGGER.error("IO关闭异常！",e);
                }
            }
        }
    }

    /**
     * 格式化文件大小
     *
     * @param length
     * @return
     */
    private String getFormatFileSize(long length) {
        double size = ((double) length) / (1 << 30);
        if (size >= 1) {
            return df.format(size) + "GB";
        }
        size = ((double) length) / (1 << 20);
        if (size >= 1) {
            return df.format(size) + "MB";
        }
        size = ((double) length) / (1 << 10);
        if (size >= 1) {
            return df.format(size) + "KB";
        }
        return length + "B";
    }

    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            FileTCPServer server = new FileTCPServer(); // 启动服务端
            server.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
