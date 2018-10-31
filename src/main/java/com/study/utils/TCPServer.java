package com.study.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

/**
 * @ClassName: TCPServer
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/10/30 11:41
 * @Version: 1.0
 */
public class TCPServer extends ServerSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPClient.class);

    private static final int SERVER_PORT = 8889; // 服务端端口

    private static DecimalFormat df = null;

    static {
        // 设置数字格式，保留一位有效小数
        df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
    }

    public TCPServer() throws Exception {
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
            new Thread(new TCPServer.Task(socket)).start();
        }
    }

    /**
     * 处理客户端传输过来的文件线程类
     */
    class Task implements Runnable {

        private Socket socket;

        private DataInputStream dis;

        private DataOutputStream dos;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());
                //获取本次传输内容长度
                byte[] lenBytes = new byte[4];
                dis.read(lenBytes,0,lenBytes.length);
                int len = ByteConvert.bytesToInt(lenBytes);

                //获取本次传输的文本
                byte[] contents = new byte[len];
                dis.read(contents,0,contents.length);
                String content = new String(contents);
                LOGGER.info("本次获取的数据："+content);
            } catch (Exception e) {
                LOGGER.error("获取数据失败！",e);
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    socket.close();
                } catch (Exception e) {
                    LOGGER.error("IO关闭失败！",e);
                }
            }
        }
    }

    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            TCPServer server = new TCPServer(); // 启动服务端
            server.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
