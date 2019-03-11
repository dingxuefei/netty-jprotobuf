package com.iscas.common.tools.pdf;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
/**
 * 给PDF添加水印
 * @author solexit06
 *
 */
public class WaterPdf {
//    public static void main(String[] args) throws DocumentException,IOException {
//        //要输出的文件
//        String outFile="F:/itext222.pdf";
//        //要加水印的原文件
//        String inputFile="F:/itext.pdf";
//        setWatermark(outFile,inputFile,"F://111.png");
//    }

    /**
     * outFile 输出文件地址+全名
     * inputhFile   要加水印的原文件
     * imagePath   水印图片的地址+全名
     * */

    public static void setWatermark(String outFile, String inputFile,String imagePath) throws DocumentException,IOException {

        PdfReader reader = new PdfReader(inputFile);//获取需要加水印的输入文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(outFile)));
        PdfStamper stamper = new PdfStamper(reader, bos);

        int total = reader.getNumberOfPages();//获取输入文件页数
        for (int i = 1; i <= total; i++) {
            PdfContentByte contentOver = stamper.getOverContent(i);//在内容上方加水印
            //PdfContentByte contentOver  = stamper.getUnderContent(i);//在内容下方加水印
            /*添加图片水印*/
            String imgUrl=imagePath;
            Image img = Image.getInstance(imgUrl);//获取要作为水印的图片
            img.setAbsolutePosition(10, 10);//图片距离文档的左下边距
            img.scaleToFit(575, 802);//图片水印的大小
            img.setRotationDegrees(0);//旋转
            contentOver.addImage(img);//添加图片水印
            contentOver.addImage(img);

            /*添加文字水印*/
            contentOver.beginText();
            contentOver.setColorFill(Color.RED);//水印颜色
            contentOver.endText();

        }
        stamper.close();
        System.out.println("添加水印成功");
    }
}
