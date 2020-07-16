package com.yusys.kafka.app.producer.userbehavior.controller;

import com.yusys.kafka.app.producer.userbehavior.service.SupplyDataService;
import com.yusys.kafka.common.producer.KafkaSendTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/dataCollection")
@PropertySource("classpath:userbehavior.properties")
public class DataCollectionController {
    @Value("${user-behavior-topic}")
    private String topic;
    @Value("${serviceAppId}")
    private String key;
    @Value("${topic-partition}")
    private Integer partition;
    @Autowired
    private KafkaSendTemplate kafkaSendTemplate;
    @Autowired
    private SupplyDataService supplyDataService;
    @RequestMapping(value = "log.gif")
    public void sendData(String args, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        if (args != null && !"".equals(args)) {
            String data = supplyDataService.supplyData(request, args);
            kafkaSendTemplate.sendData(topic, partition, key, data);
        }
        // 返回一个图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/gif");
        OutputStream out = response.getOutputStream();
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(image, "gif", out);
        out.flush();
    }
}

