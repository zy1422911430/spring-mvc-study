package com.study.utils;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Timer;

/**
 * @ClassName: TCPClient
 * @Description TODO 与C++进行通信的TCP工具类
 * @Author: zyd
 * @Date: 2018/10/30 10:04
 * @Version: 1.0
 */
public class TCPClient extends Socket {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTCPClient.class);
    private static final String SERVER_IP = "127.0.0.1"; // 服务端IP
    private static final int SERVER_PORT = 8889; // 服务端端口

    private Socket client;

    private DataOutputStream dos;

    private DataInputStream dis;

    /**
     * @Author zyd
     * @Description //TODO 构造tcp客户端
     * @Date 14:25 2018/10/31
     * @Param []
     * @return
     **/
    public TCPClient() throws Exception {
        super(SERVER_IP, SERVER_PORT);
        this.client = this;
        LOGGER.info("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");
    }

    /**
     * @Author zyd
     * @Description //TODO 向服务端推送内容
     * @Date 14:38 2018/10/31
     * @Param [content]
     * @return void
     **/
    public void sendContent(String content){
        try {
            dos = new DataOutputStream(client.getOutputStream());
            //发送本次内容长度
            int len = content.getBytes(Charset.forName("UTF-8")).length;
            dos.write(ByteConvert.intToBytes(len));
            dos.flush();
            //发送内容
            dos.write(content.getBytes(Charset.forName("UTF-8")));
            dos.flush();
        } catch (IOException e) {
            LOGGER.error("IO异常：",e);
        } finally {
            exit();
        }
    }

    public void exit(){
        try {
            if (dos != null)
                dos.close();
            client.close();
        } catch (IOException e) {
            LOGGER.error("IO关闭异常:",e);
        }
    }

    public static void main(String[] args) throws Exception {
        TCPClient tcpClient = new TCPClient();
        tcpClient.sendContent("hello!!");
    }
}
