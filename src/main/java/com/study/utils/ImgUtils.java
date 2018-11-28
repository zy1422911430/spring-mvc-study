package com.study.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * @ClassName: ImgUtils
 * @Description TODO 对图片处理的工具类，包括图片截取、缩放等
 * @Author: zyd
 * @Date: 2018/11/24 9:19
 * @Version: 1.0
 */
public class ImgUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtils.class);

    private static final String winScaleImgDir = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\scale\\";

    private static final String winCropImgDir = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\crop\\";

    private static final String winModifyImgDir = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\modify\\";

    private static final String linuxScaleImgDir = "/usr/local/img/scale/";

    private static final String linuxCropImgDir = "/usr/local/img/crop/";

    private static final String linuxModifyImgDir = "/usr/local/img/crop/";

    /**
     * @Author zyd
     * @Description //TODO 按像素缩放图片方法
     * @Date 10:00 2018/11/24
     * @Param srcImageFile 要缩放的图片路径
     * @Param sku 图片对应的自定义sku
     * @Param height 目标高度像素
     * @Param width  目标宽度像素
     * @Param bb     是否补白
     * @Return String 返回缩放后的图片存放路径
     **/
    public final static String scale(String srcImageFile, String sku, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            String fileName = f.getName();
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);//bi.SCALE_SMOOTH  选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                double ratioHeight = (new Integer(height)).doubleValue() / bi.getHeight();
                double ratioWhidth = (new Integer(width)).doubleValue() / bi.getWidth();
                if (ratioHeight > ratioWhidth) {
                    ratio = ratioHeight;
                } else {
                    ratio = ratioWhidth;
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform//仿射转换
                        .getScaleInstance(ratio, ratio), null);//返回表示剪切变换的变换
                itemp = op.filter(bi, null);//转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
            }
            if (bb) {//补白
                itemp = fillWithColor(width, height, itemp, Color.white);
            }
            return saveImg(fileName, (BufferedImage) itemp, sku, "scale");
        } catch (IOException e) {
            LOGGER.error("IO异常：", e);
        }
        return null;
    }

    /**
     * @Author zyd
     * @Description //TODO 根据比例缩放图片
     * @Date 10:16 2018/11/24
     * @Param srcImageFile 原图片路径
     * @Param sku 图片对应的自定义sku
     * @Param ratio 图片缩放比例
     * @Param bb 是否补白
     * @Return String 返回缩放后的图片存放路径
     **/
    public final static String scaleWithRatio(String srcImageFile, String sku, double ratio, boolean bb) {
        try {
            File f = new File(srcImageFile);
            String fileName = f.getName();
            BufferedImage bi = ImageIO.read(f);
            int width = new Double(bi.getWidth() * ratio).intValue();
            int height = new Double(bi.getHeight() * ratio).intValue();
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);//bi.SCALE_SMOOTH  选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            AffineTransformOp op = new AffineTransformOp(AffineTransform//仿射转换
                    .getScaleInstance(ratio, ratio), null);//返回表示剪切变换的变换
            itemp = op.filter(bi, null);//转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
            if (bb) {//补白
                itemp = fillWithColor(width, height, itemp, Color.white);
            }
            return saveImg(fileName, (BufferedImage) itemp, sku, "scale");
        } catch (IOException e) {
            LOGGER.error("IO异常：", e);
        }
        return null;
    }

    /**
     * @Author zyd
     * @Description //TODO 使用 Graphics2D 上下文的设置，将 Shape 的内部区域填充指定颜色
     * @Date 10:35 2018/11/24
     * @Param width 图片的宽度(单位像素)
     * @Param height 图片的高度(单位像素)
     * @Param itemp 目标图片
     * @Param color 指定的填充颜色
     * @Return java.awt.Image
     **/
    public static Image fillWithColor(int width, int height, Image itemp, Color color) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);//构造一个类型为预定义图像类型之一的 BufferedImage。
        Graphics2D g = image.createGraphics();//创建一个 Graphics2D，可以将它绘制到此 BufferedImage 中。
        g.setColor(color);//控制颜色
        g.fillRect(0, 0, width, height);// 使用 Graphics2D 上下文的设置，填充 Shape 的内部区域。
        if (width == itemp.getWidth(null))
            g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                    itemp.getWidth(null), itemp.getHeight(null),
                    color, null);
        else
            g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                    itemp.getWidth(null), itemp.getHeight(null),
                    color, null);
        g.dispose();
        return image;
    }

    /**
     * @Author zyd
     * @Description //TODO 裁剪图片方法，startX、startY的值为-1时，startX与startY的值会默认为0，endX与endY的值为-1时，endX与endY的值会默认为最大像素
     * @Date 9:56 2018/11/24
     * @Param srcImageFile 图像源
     * @Param sku 目标存放路径
     * @Param startX 裁剪开始x坐标
     * @Param startY 裁剪开始y坐标
     * @Param endX 裁剪结束x坐标
     * @Param endY 裁剪结束y坐标
     * @Return String 返回裁剪完成的图片路径
     **/
    public static String cropImage(String srcImageFile, String sku, int startX, int startY, int endX, int endY) {
        File f = new File(srcImageFile);
        String fileName = f.getName();
        BufferedImage bufferedImage = loadImageLocal(srcImageFile);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (startX == -1) {
            startX = 0;
        }
        if (startY == -1) {
            startY = 0;
        }
        if (endX == -1) {
            endX = width;
        }
        if (endY == -1) {
            endY = height;
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                int rgb = bufferedImage.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }

        return saveImg(fileName, result, sku, "crop");
    }

    /**
     * @Author zyd
     * @Description //TODO 导入本地图片到缓冲区
     * @Date 16:21 2018/11/24
     * @Param imgName 图片路径
     * @Return java.awt.image.BufferedImage
     **/
    public static BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @Author zyd
     * @Description //TODO 将多张图片合成(平行放置)
     * @Date 14:03 2018/11/24
     * @Param imgList 需要合成的多张图片
     * @Param back 背景图片
     * @Param fileName 生成的图片命名
     * @Param sku 图片对应的自定义sku
     * @Return String 成品图片的路径
     **/
    public static String modifyImageTogether(List<String> imgList, String back, String fileName, String sku) {
        List<BufferedImage> bufferedImageList = new ArrayList<>();
        for (String s : imgList) {
            bufferedImageList.add(loadImageLocal(s));
        }
        BufferedImage d = loadImageLocal(back);
        try {
            Graphics2D g = d.createGraphics();
            for (int i = 0; i < bufferedImageList.size(); i++) {
                int w = bufferedImageList.get(i).getWidth();
                int h = bufferedImageList.get(i).getHeight();
                int backWidth = d.getWidth();
                int backHeight = d.getHeight();

                int siderWidth = new Double((backWidth - (w * bufferedImageList.size())) / (bufferedImageList.size() + 1)).intValue();
                LOGGER.info("siderWidth=" + siderWidth);
                int x = i * w + (i + 1) * siderWidth;
                LOGGER.info("x=" + x);
                int y = new Double((backHeight - h) / 2).intValue();
                g.drawImage(bufferedImageList.get(i), x, y, w, h, null);
            }
            g.dispose();
        } catch (Exception e) {
            LOGGER.error("图片合成异常：", e);
        }

        return saveImg(fileName, d, sku, "modify");
    }

    /**
     * @Author: zyd
     * @Description: //TODO 两张图片合成
     * @Date: 11:45 2018/11/26
     * @Param: imgSrc 需要合成的图片路径
     * @Param: backSrc 背景图片路径
     * @Param: x 图片放置的x位置
     * @Param: fileName 要生成的图片名
     * @Param: sku 图片对应的自定义sku
     * @Return: java.lang.String
     **/
    public static String modifyImageTogether(String imgSrc, String backSrc, int x, String fileName, String sku) {
        BufferedImage img = loadImageLocal(imgSrc);
        BufferedImage back = loadImageLocal(backSrc);
        Graphics2D g = back.createGraphics();
        //获取背景图和合成图的高度
        int h = img.getHeight();
        int backHeight = back.getHeight();

        //计算合成是的y轴位置
        int y = new Double((backHeight - h) / 2).intValue();
        g.drawImage(img, x, y, img.getWidth(), h, null);
        return saveImg(fileName, back, sku, "modify");
    }

    /**
     * @Author zyd
     * @Description //TODO 将缓冲区的图片放到指定路径下
     * @Date 16:20 2018/11/24
     * @Param newImage 指定图片的放置位置
     * @Param img 缓冲区图片
     * @Return void
     **/
    public static void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * @Author: zyd
     * @Description: //TODO 将缓存区的图片保存到本地
     * @Date: 16:42 2018/11/26
     * @Param: fileName 需要输出的文件名
     * @Param: itemp 目标图片
     * @Param: sku 自定义sku
     * @Param: type 保存类型，scale为缩放图片存放目录、crop为截图图片存放目录、modify为成品图片存放目录
     * @Return: java.lang.String
     **/
    private static String saveImg(String fileName, BufferedImage itemp, String sku, String type) {

        try {
            //获取当前操作系统类型
            String os = System.getProperty("os.name").toLowerCase();

            String savePath = null;
            File dir = null;
            if (os.indexOf("linux") >= 0) {
                switch (type) {
                    case "scale":
                        savePath = linuxScaleImgDir + sku;
                        break;
                    case "crop":
                        savePath = linuxCropImgDir + sku;
                        break;
                    case "modify":
                        savePath = linuxModifyImgDir + sku;
                        break;
                }
            }

            if (os.indexOf("windows") >= 0) {
                switch (type) {
                    case "scale":
                        savePath = winScaleImgDir + sku;
                        break;
                    case "crop":
                        savePath = winCropImgDir + sku;
                        break;
                    case "modify":
                        savePath = winModifyImgDir + sku;
                        break;
                }
            }

            dir = new File(savePath);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            ImageIO.write((BufferedImage) itemp, "jpg", new File(savePath + "/" + fileName));      //输出压缩图片
            return savePath + "/" + fileName;
        } catch (IOException e) {
            LOGGER.error("图片保存异常：", e);
        }
        return null;
    }

    /**
     * @Author zyd
     * @Description //TODO 删除指定文件夹下的所有文件夹和文件
     * @Date 10:59 2018/11/26
     * @Param [targetDir, sku]
     * @Return boolean true:删除成功,false:删除失败
     **/
    public static boolean removeDir(String targetDir, String sku) {
        String sPath = targetDir + sku;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return false;
        } else {
            if (file.isFile()) {
                LOGGER.error("类型错误，删除类型不能为文件");
                return false;
            }
            return deleteDirectory(sPath);
        }
    }

    /**
     * @Author zyd
     * @Description //TODO 递归删除指定文件夹下的文件和文件夹
     * @Date 11:06 2018/11/26
     * @Param [sPath]
     * @Return boolean
     **/
    private static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                // 路径为文件且不为空则进行删除
                if (files[i].isFile() && files[i].exists()) {
                    files[i].delete();
                    flag = true;
                }
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Author zyd
     * @Description //TODO 将RGB对应的int值转为RGB值
     * @Date 16:18 2018/11/24
     * @Param color RGB的int值
     * @Return java.awt.Color
     **/
    private static Color toRGB(int color) {
        int r = 0xFF & color;
        int g = 0xFF00 & color;
        g >>= 8;
        int b = 0xFF0000 & color;
        b >>= 16;
        return new Color(r, g, b);
    }

    public static void main(String[] args) {
//        //原始背景图片(白底)
//        BufferedImage b = loadImageLocal("C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\图片\\背景图.jpg");
//
//        //要处理的图片1
//        String filePath1 = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\图片\\0001-星空.jpg";
//        //要处理的图片2
//        String filePath2 = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\图片\\0002-蓝色星空.jpg";
//        //要处理的图片3
//        String filePath3 = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\图片\\0003-五彩星空.jpg";
//
//
//        String savePath = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\截图";
//        //裁剪图片
//        String cropImgPath1 = cropImage(filePath1,savePath,300,-1,900,-1);
//        String cropImgPath2 = cropImage(filePath2,savePath,300,-1,900,-1);
//        String cropImgPath3 = cropImage(filePath3,savePath,300,-1,900,-1);
//
//        String saveScalePath = "C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\缩放";
//
//        String saveScalePath1 = scaleWithRatio(cropImgPath1,saveScalePath,0.5,false);
//        String saveScalePath2 = scaleWithRatio(cropImgPath2,saveScalePath,0.5,false);
//        String saveScalePath3 = scaleWithRatio(cropImgPath3,saveScalePath,0.5,false);
//
//        BufferedImage d1 = loadImageLocal(saveScalePath1);
//        BufferedImage d2 = loadImageLocal(saveScalePath2);
//        BufferedImage d3 = loadImageLocal(saveScalePath3);
//        List<BufferedImage> bufferedImageList = new ArrayList<>();
//        bufferedImageList.add(d1);
//        bufferedImageList.add(d2);
//        bufferedImageList.add(d3);
//
//        writeImageLocal("C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\成品\\3.jpg", modifyImagetogeter(bufferedImageList, b));
//        //将多张图片合在一起
//        System.out.println("success");

//        String filePath1 = "C:\\\\Users\\\\My\\\\Desktop\\\\工作资料\\\\OMS需求\\\\2018-11-23-彩绘需求\\\\图片\\\\0003-五彩星空.jpg";
//        BufferedImage bufferedImage = loadImageLocal(filePath1);
//        int height = bufferedImage.getHeight() / 2;
//        int width = bufferedImage.getWidth();
//        int x = 0;
//        for (int i = 0; i < width; i++) {
//            int rgb = bufferedImage.getRGB(i, height);
//            Color color = toRGB(rgb);
//            LOGGER.info(color.toString());
//            if (color.getRed() < 255 || color.getGreen() < 255 || color.getBlue() < 255) {
//                x += 1;
//            }
//            //bufferedImage.
//        }
//        LOGGER.info("x = " + x);

        boolean result = removeDir("C:\\Users\\My\\Desktop\\工作资料\\OMS需求\\2018-11-23-彩绘需求\\", "下载图片");
        System.out.println(result);
    }

}