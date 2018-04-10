package com.tjw.zxing.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author wb-tjw362524
 * @Date 17:37 2018/4/9
 */
@Service
public class ZService {

    public String encode(String content,int width,int height,String path,String fileName){
        Map<EncodeHintType,Object> hintTypeObjectMap = new HashMap<>();
        hintTypeObjectMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"utf-8");
        OutputStream out=null;
        String realPath = path+"/"+fileName;
        try {
                out = new FileOutputStream(realPath);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BitMatrix bitMatrix;
        try {
            // 生成二维码
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hintTypeObjectMap);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            BASE64Encoder encoder = new BASE64Encoder();
            FileInputStream fileInputStream = new FileInputStream(new File(realPath));
            byte[] buffer = new byte[1024*4];
            int n=0;
            while((n=fileInputStream.read(buffer))!=-1){
                out1.write(buffer,0,n);
            }
            byte[] byts = out1.toByteArray();
            String ss = encoder.encode(byts);
            System.out.println(ss);
            //在将base64转图片
            byte[] buffer1 = new BASE64Decoder().decodeBuffer(ss);
            FileOutputStream out2 = new FileOutputStream(path+"/"+"sss.png");
            out2.write(buffer1);
            out2.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return realPath;
    }
    public String mixLog(File qrcode,File log,Integer width,Integer height,String path,String fileName){
        /**
         * 读取二维码图片，并构建绘图对象
         */
        OutputStream os = null ;
        try {
            Image image2 = ImageIO.read(qrcode) ;
            if (width==null){
                width = image2.getWidth(null) ;
            }
            if (height==null){
                height = image2.getHeight(null) ;
            }
            BufferedImage bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB) ;
            //BufferedImage bufferImage =ImageIO.read(image) ;
            Graphics2D g2 = bufferImage.createGraphics();
            g2.drawImage(image2, 0, 0, width, height, null) ;
            int matrixWidth = bufferImage.getWidth();
            int matrixHeigh = bufferImage.getHeight();

            //读取Logo图片
            BufferedImage logo= ImageIO.read(log);
            //开始绘制图片
            g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
            BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke);// 设置笔画对象
            //指定弧度的圆角矩形
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
            g2.setColor(Color.white);
            g2.draw(round);// 绘制圆弧矩形

            //设置logo 有一道灰色边框
            BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke2);// 设置笔画对象
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+1, matrixHeigh/5*2+1, matrixWidth/5-3, matrixHeigh/5-3,20,20);
            g2.setColor(new Color(128,128,128));
            g2.draw(round2);// 绘制圆弧矩形

            g2.dispose();

            bufferImage.flush() ;
            os = new FileOutputStream(path+"/"+fileName) ;
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os) ;
            en.encode(bufferImage) ;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return path+"/"+fileName ;
    }
}
