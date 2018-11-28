package com.study.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName: DownloadFileUtils
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/11/24 16:23
 * @Version: 1.0
 */
public class DownloadFileUtils {

    private static final String winLocalDir = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\download\\";

    private static final String linuxLocalDir = "/usr/local/img/download/";

    /**
     * @Author: zyd
     * @Description: //TODO 根据url下载图片
     * @Date: 16:34 2018/11/26
     * @Param: url 图片url地址
     * @Return: java.io.InputStream
     **/
    public static InputStream getInputStreamByGet(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Author: zyd
     * @Description: //TODO 将下载的图片资源保存到本地服务器
     * @Date: 16:35 2018/11/26
     * @Param: is 图片的输入流
     * @Param: url 图片的下载路径
     * @Param: sku 图片的分类管理文件夹
     * @Return: java.lang.String
     **/
    public static String saveData(InputStream is, String url, String sku) {
        //获取url的文件名
        String[] split = url.split("\\/");
        String fileName = split[split.length - 1];

        //获取当前操作系统类型
        String os = System.getProperty("os.name").toLowerCase();
        File file = null;
        if(os.indexOf("linux")>=0){
            //如果文件夹不存在，则创建
            File dir = new File(linuxLocalDir + sku + "/");
            if (!dir.exists()) {
                dir.mkdir();
            }
            file = new File(linuxLocalDir + sku + "/", fileName);
        }

        if(os.indexOf("windows")>=0) {
            //如果文件夹不存在，则创建
            File dir = new File(winLocalDir + sku + "/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(winLocalDir + sku + "/", fileName);
        }
        //下载图片资源
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try  {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
            return linuxLocalDir + sku + "\\"+fileName;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bos != null)
                    bos.close();
                if(bis != null)
                    bis.close();
                if(is != null)
                    is.close();
            } catch (IOException e){

            }
        }
        return null;
    }


    public static void main(String[] args) {
        String url = "http://t2.hddhhn.com/uploads/tu/201611/228/st87.png";
        InputStream inputStream = getInputStreamByGet(url);
        saveData(inputStream, url, "testSKU");
    }
}
