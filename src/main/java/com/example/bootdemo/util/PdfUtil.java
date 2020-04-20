package com.example.bootdemo.util;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;


public class PdfUtil {
    /**
     * 创建pdf
     * @param examjson
     * @return
     */
    public static void createPdf(String examjson){
        Map<String, Object> exam = JSONObject.parseObject(examjson, Map.class);
        String examPath = "";
        try{
            //创建文件
            Document document = new Document();
            //设置字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            //文件路径
            examPath = "D:/pdf";
            File files = new File(examPath);
            if(! files.exists() && !files.isDirectory()) {					//判断目录是否存在
                files.mkdir();
            }
            //创建PDF
            PdfWriter writer =  PdfWriter.getInstance(document, new FileOutputStream(examPath+"/test.pdf"));
            // 设置页面布局
            writer.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
            //打开文件
            document.open();
            //图片1
            Image image1 = Image.getInstance("D:/test.png");
            //设置图片的宽度和高度
            image1.scaleAbsolute(140, 40);
            //将图片1添加到pdf文件中
            document.add(image1);
            //标题
            Paragraph paragraph = new Paragraph(150);//段落的间距
            //1 2 3  中右左
            paragraph.setAlignment(1);  //对齐方式
            Font font = new Font(bfChinese);//字体
            font.setSize(22);//字体大小
            paragraph.setFont(font);//设置段落字体
            Chunk chunk = new Chunk("Test");
            paragraph.add(chunk);
            document.add(paragraph);

            Paragraph paragraph1 = new Paragraph(40);
            //1 2 3  中右左
            paragraph1.setAlignment(1);  //对齐方式
            Font font1 = new Font(bfChinese);//字体
            font1.setSize(20);
            paragraph1.setFont(font1);
            Chunk chunk1 = new Chunk("Test1");
            paragraph1.add(chunk1);
            //paragraph1.setSpacingBefore(-15);
            //paragraph1.setSpacingAfter(-50);//往下距离200
            document.add(paragraph1);

            //点线
            DottedLineSeparator line = new DottedLineSeparator();
            //下移5个单位
            line.setOffset(-15);
            //设置点之间的距离
            //line.setGap(20);
            document.add(line);

            Paragraph paragraph3 = new Paragraph(150);
            //1 2 3  中右左
            paragraph3.setAlignment(1);  //对齐方式
            Font font3 = new Font(bfChinese);//字体
            font3.setSize(12);
            paragraph3.setFont(font3);
            Chunk chunk3 = new Chunk("编号："+getRandom(17));
            paragraph3.add(chunk3);
            paragraph3.setSpacingAfter(5);
            document.add(paragraph3);

            Paragraph paragraph4 = new Paragraph(15);
            //1 2 3  中右左
            paragraph4.setAlignment(1);  //对齐方式
            Font font4 = new Font(bfChinese);//字体
            font4.setSize(12);
            paragraph4.setFont(font4);
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            Chunk chunk4 = new Chunk("文档生成日期："+df.format(System.currentTimeMillis()));
            paragraph4.add(chunk4);
            paragraph4.setSpacingAfter(5);
            document.add(paragraph4);

            document.newPage(); //换页

            Paragraph answerPar = new Paragraph(20);				//标准答案
            answerPar.setAlignment(3);  //对齐方式
            Font answerfont = new Font(bfChinese);//字体
            answerfont.setSize(12);
            answerPar.setFont(answerfont);
            Chunk answerchunk = new Chunk(examjson);
            answerPar.add(answerchunk);
            answerPar.setSpacingAfter(5);
            document.add(answerPar);
            //关闭文档
            document.close();
            //关闭书写器
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 生成指定位数的随机数
     * @param length
     * @return
     */
    public static String getRandom(int length){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    public static void main(String[] args) {
        String testJson = "{\"orgName\":\"中国建设银行股份有限公司海南省分行\",\"gender\":\"男\",\"monthPension\":\"2525.40\",\"currentRepyDy\":\"202001\",\"reDate\":\"201911\",\"contactphone\":\"0898-65316035\",\"idno\":\"460102199610291218\",\"operator\":\"东软测试\",\"nm\":\"陈煜\"}";
        createPdf(testJson);
    }
}
