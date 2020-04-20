package com.example.bootdemo.util;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;

public class PdfMatter {
    private static final Logger LOG = LoggerFactory.getLogger(PdfMatter.class);
    //pdf模板和结果路径相关设置
    private String templateDir;
    // 文件保存的目录
    private final String basePath = "D:/pdf";
    //需填充数据
    private Map<String, Object> dataMap;
    // 结果文件名
    private String resultFileName;

    /**
     * 构造器，生成PDF引擎实例，并引入相应模板文件XXX.FO、路径和报表数据HashMap
     *
     * @param templateDir
     *            模板文件所在目录
     * @param resultFileName
     *            文件结果PDF文件名称
     * @param dataMap
     *            对应模板的数据HashMap，由调用该打印引擎的里程根据模板格式和约定进行准备
     */
    public PdfMatter(String templateDir, String resultFileName, Map dataMap) {
        this.templateDir = templateDir;
        this.dataMap = dataMap;
        this.resultFileName = resultFileName;
    }

    private BaseFont getBaseFont(Object obj) {
        // 需要根据不同的模板返回字体
        BaseFont bf = null;
        try {

            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return bf;
    }
    public String doTransform() {
        try {
            PdfReader reader;
            PdfStamper stamper;
            reader = new PdfReader(templateDir);
            stamper = new PdfStamper(reader, new FileOutputStream(basePath + File.separator + resultFileName));
            AcroFields form = stamper.getAcroFields();
            List<String> imageUrls = new ArrayList<>();
            imageUrls.add("D:/test.png");
            imageUrls.add("D:/yz.jpg");
            // 插入图片
            insertImage(stamper, form, imageUrls);
            // 写表格内容
            form.addSubstitutionFont(getBaseFont(""));
            transformRegular(form);
            stamper.setFormFlattening(true);
            stamper.close();
            // 后置处理
            postProcess();
        } catch (Exception e) {
            LOG.error("生成pdf文件异常{}", e);
        }
        return basePath + "/" + resultFileName;
    }

    private void insertImage(PdfStamper stamper, AcroFields fields, List<String> imageUrls) {
        try {
            PdfContentByte under = stamper.getOverContent(1);
            List<AcroFields.FieldPosition> list = fields.getFieldPositions("image");
            for (int i = 0; i < list.size(); i++) {
                Rectangle signRect = list.get(i).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                Image image = Image.getInstance(imageUrls.get(i));
                if (image != null) {
                    image.setAbsolutePosition(x, y);
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    under.addImage(image);
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成pdf的后置处理
     */
    private void postProcess() {
    }
    /**
     * 填充规整的表单域
     *
     * @param form
     */
    private void transformRegular(AcroFields form) {
        if (dataMap == null || dataMap.size() == 0) {
            return;
        }
        String key = "";
        Iterator<String> iterator = dataMap.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            try {
                //非空放入
                if( dataMap.get(key) != null) {
                    form.setField(key, dataMap.get(key).toString());
                }
            } catch (Exception e) {
                LOG.error("填充数据异常{}", e);
            }
        }
    }

    public static void main(String[] args) {
        String testData = "{\"idvId\":\"3506818013\",\"yilTranMon\":\"0\",\"syInType\":\"参保缴费\",\"gsInType\":\"参保缴费\",\"gsStartTime\":\"\",\"printOrg\":\"469900\",\"syTranMon\":\"0\",\"orgNo\":\"4699001007039\",\"gsPayMonth\":\"0\",\"yilStartTime\":\"\",\"insureOrg\":\"469900\",\"idvaccNoType\":\"1\",\"shiyStartTime\":\"\",\"gsTranMon\":\"0\",\"syEndTime\":\"\",\"yalTranMon\":\"0\",\"syPayMonth\":\"0\",\"orgName\":\"中国建设银行股份有限公司海南省分行\",\"shiyInType\":\"参保缴费\",\"ylInType\":\"参保缴费\",\"gsEndTime\":\"\",\"yilInType\":\"参保缴费\",\"ylStartTime\":\"\",\"yilPayMonth\":\"0\",\"limitType\":\"\",\"idno\":\"460005199612072514\",\"shiyPayMonth\":\"0\",\"ylPayMonth\":\"0\",\"syStartTime\":\"\",\"shiyTranMon\":\"0\",\"ylEndTime\":\"\",\"shiyEndTime\":\"\",\"yilEndTime\":\"\",\"nm\":\"张新泰\"}";
        Map<String, Object> map = JSONObject.parseObject(testData, Map.class);
        map.put("cbjg", "海南省社会保险事业局");
        map.put("dyjg", "海南省社会保险事业局");
        map.put("dysj", "2020年3月31日");
        map.put("dyfs", "STM自助一体机");
        long name = System.currentTimeMillis();
        String resultFileName = "result_" + name + ".pdf";
        PdfMatter pdf = new PdfMatter("D:\\pdfFinal02.pdf", resultFileName , map);
        long s1 = System.currentTimeMillis();
        String PdfFilePath = pdf.doTransform();
        long s2 = System.currentTimeMillis();
        System.out.println("生成pdf文件耗时:" + (s2 - s1));
        try {
            long start = System.currentTimeMillis();
            if (PdfFilePath != null && !"".equals(PdfFilePath)) {
                InputStream in = new BufferedInputStream(new FileInputStream(new File(PdfFilePath)));
                byte[] buff = new byte[1024];
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                int len = 0;
                while ((len = in.read(buff)) != -1) {
                    os.write(buff, 0, len);
                }
                //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
                os.flush();
                os.toByteArray();
                String dUrlData = Base64.getEncoder().encodeToString(os.toByteArray());
                //System.out.print("生成pdf base64串:{}"  + dUrlData);
            }
            long end = System.currentTimeMillis();
            System.out.print("转base64耗时:{}" +  (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("生成pdf文件名称{}" +  PdfFilePath);
    }
}
