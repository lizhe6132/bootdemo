package com.example.bootdemo;

import com.example.bootdemo.util.PathSafeUtil;
import com.yusys.kafka.app.producer.userbehavior.util.AESUtil;
import com.yusys.kafka.common.producer.KafkaProducerFactory;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class BootdemoApplicationTests extends ApplicationTest{
    private final int TIMEOUT=30000;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private final String SERVER="127.0.0.1:2181";
    @Test
    void contextLoads() {
    }
    @Test
    void zookeeperTest() throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(SERVER, TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                    System.out.printf("连接完成");

                }
            }
        });
        countDownLatch.await();
        System.out.printf(zooKeeper.getState()+"");
    }
    @Test
    public void encryptTest() {

        //待加密串
        String message = "http://128.1.128.141:13400/";
        System.out.println(PathSafeUtil.getSafePath(message));

    }
    @Test
    void test() {
        int x = -121;
        String s = Integer.valueOf(x).toString();
        int i = 0;
        int j =1;
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        String first = s.substring(0,1);
        if ("-".equals(first)) {
            i = 1;
            j = 0;
        }
        for (;i < s.length(); i++) {
            sb.append(s.charAt(i));
            sb2.append(s.charAt((s.length() - j) - i));
        }
        if (Integer.valueOf(sb.toString()).equals(Integer.valueOf(sb2.toString()))) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }
    @Test
    public void test4() {
        String json = "{\"sendContent\":{\"gmsfhm\":\"230523199404184818\"},\"interfaceCode\":\"/jzz/jh/getQgkHjxx\",\"businessCode\":\"jhzwcmcx\"}";
        String idcard = "(:|=)[^a-zA-Z]\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$/";
        Pattern ptCard = Pattern.compile(idcard);
        String idnoRegex = "^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))(([0|1|2][1-9])|3[0-1])((\\d{4})|\\d{3}X)$";
        Pattern p = Pattern.compile(idnoRegex);
        String testjs = "610525199011272239";

        System.out.println(testjs.replaceAll(idnoRegex, "******"));
        Matcher m = p.matcher(testjs);
        System.out.println("ReplaceAll:" + m.replaceAll("*"));
        while (m.find()) {
            System.out.println(m.group());
        }
    }
    @Test
    public void testFloat() {
        float s1 = 0.01f;
        float s3 = 0.01f;
        double s2 = 0.01d;
        BigDecimal decimal1 = new BigDecimal(String.valueOf(s1));
        BigDecimal decimal2 = new BigDecimal(String.valueOf(s2));
        System.out.println(decimal1.equals(decimal2));
        System.out.println(decimal1.compareTo(decimal2));
        System.out.println(s1 - 0.01);
    }
    @Test
    public void createEncryKey() {
        String password = "1234567";
        System.out.println(AESUtil.getSecretKey(password));
    }
}
