package com.bw.iot.tbc.wechatdemo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bw.iot")
public class WechatDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatDemoApplication.class, args);
    }

}
