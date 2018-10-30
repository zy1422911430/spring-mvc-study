package com.study.utils;

/**
 * @ClassName: ZipUtils
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/10/29 16:30
 * @Version: 1.0
 */

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public final class ZipUtils {

    private static Logger log = LoggerFactory.getLogger(ZipUtils.class);

    private ZipUtils() {
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     * @param fileName   指定生成zip文件的文件名
     * @param isDrop     是否删除原文件:true删除、false不删除
     */
    public static boolean createZip(String sourcePath, String zipPath, String fileName, Boolean isDrop) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath + "/" + fileName + ".zip");
            zos = new ZipOutputStream(fos);
            zos.setEncoding("utf-8");//此处修改字节码方式。
            //createXmlFile(sourcePath,"293.xml");
            File file = new File(sourcePath);
            File[] fileList = file.listFiles();
            for (File file1 : fileList) {
                writeZip(file1, "", zos);
            }
            if (isDrop){
                try {
                    deleteDir(file);
                } catch (Exception e) {
                    log.error("删除文件失败",e);
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            log.error("创建ZIP文件失败", e);
            return false;
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败", e);
                return false;
            }
        }
        return true;
    }

    /**
     * @Author zyd
     * @Description //TODO 删除指定目录下的文件及目录
     * @Date 17:57 2018/10/29
     * @Param [dir]
     * @return boolean
     **/
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {       //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    log.error("创建ZIP文件失败", e);
                } catch (IOException e) {
                    log.error("创建ZIP文件失败", e);
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        log.error("创建ZIP文件失败", e);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "F:\\压缩包测试";
        String sourceFilePath = "F:\\zipfile";
        String fileName = "2018-10-29";
        System.out.println(createZip(filePath, sourceFilePath, fileName, true));
    }
}