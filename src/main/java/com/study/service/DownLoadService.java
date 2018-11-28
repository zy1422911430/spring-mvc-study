package com.study.service;

import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @ClassName: DownLoadService
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/11/22 9:39
 * @Version: 1.0
 */
@Service
public class DownLoadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownLoadService.class);

    public void getExcel(){
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("测试图片导出到Excel");
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeight((short)(sheet.getDefaultRowHeightInPoints()*100));


        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);


        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        BufferedImage bufferImg = null;
        FileOutputStream fileOut = null;
        try {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            bufferImg = ImageIO.read(new File("C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-10-24-彩绘需求\\图片\\0001-星空.jpg"));
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
            HSSFClientAnchor anchor = new HSSFClientAnchor(3, 2, 1, 1,(short) 1, 2, (short) 2, 3);
            anchor.setAnchorType(3);
            //插入图片
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

            fileOut = new FileOutputStream("D:/测试Excel.xls");
            // 写入excel文件
            wb.write(fileOut);
        } catch (Exception e){
            LOGGER.error("导出Excel出错：",e);
        }


    }
}
