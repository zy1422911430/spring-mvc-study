package com.study.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @ClassName: FileTCPClient
 * @Description TODO socket文件传输
 * @Author: zyd
 * @Date: 2018/10/30 16:15
 * @Version: 1.0
 */
public class FileTCPClient extends Socket {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTCPClient.class);
    private static final String SERVER_IP = "127.0.0.1"; // 服务端IP
    private static final int SERVER_PORT = 8888; // 服务端端口

    private Socket client;

    private FileInputStream fis;

    private DataOutputStream dos;

    private DataInputStream dis;

    private Read read = new Read();
    class Read extends Thread {
        @Override
        public void run() {
            try {
                dis = new DataInputStream(client.getInputStream());
                while (client != null) {
                    long len = dis.readLong();
                    System.out.println("len==" + len);
                    byte[] bytes = new byte[(int) len];
                    dis.read(bytes);
                    System.out.println(new String(bytes));
                    if(len > 0){
                        dis.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Author zyd
     * @Description //TODO 构造tcp客户端
     * @Date 14:25 2018/10/31
     * @Param []
     * @return
     **/
    public FileTCPClient() throws Exception {
        super(SERVER_IP, SERVER_PORT);
        this.client = this;
        LOGGER.info("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");
    }

    /**
     * @Author zyd
     * @Description //TODO 文件传输客户端
     * @Date 13:48 2018/10/31
     * @Param [filePath]
     * @return void
     **/
    public void sendFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());

                // 文件名和长度
                //dos.writeUTF(file.getName());
                //dos.flush();
                //dos.writeLong(file.length());
                int len = new Long(file.length()).intValue();
                System.out.println("len=="+len);
                //文件长度转为byte数组
                byte[] bytes1 = ByteConvert.intToBytes(len);
                dos.write(bytes1);
                dos.flush();

                //文件名及文件名长度
                byte[] nameBytes = file.getName().getBytes(Charset.forName("UTF-8"));
                dos.write(ByteConvert.intToBytes(nameBytes.length));
                dos.flush();
                dos.write(nameBytes);
                dos.flush();

                // 开始传输文件
                LOGGER.info("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                    System.out.print("| " + (100 * progress / file.length()) + "% |");
                }
                LOGGER.info("======== 文件传输成功 ========");
            }
        } catch (IOException e) {
            LOGGER.error("IO异常:",e);
        } finally {
            exit();
        }
    }

    public void exit(){
        try {
            if (fis != null)
                fis.close();
            if (dos != null)
                dos.close();
            client.close();
        } catch (IOException e) {
            LOGGER.error("IO关闭异常:",e);
        }
    }

    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) throws Exception{
        FileTCPClient client = new FileTCPClient(); // 启动客户端连接
        client.sendFile("F:\\压缩包测试 - 副本\\ZDYSKU001\\竹林 - 副本 (2).jpg"); // 传输文件
    }


}
