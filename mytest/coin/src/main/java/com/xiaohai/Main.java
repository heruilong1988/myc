package com.xiaohai;


import com.xiaohai.jaxrs.api.TImpl;
import com.xiaohai.jaxrs.service.base.PlatformFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        System.setProperty("proxyType", "4");
        System.setProperty("proxyPort", "1080");
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxySet", "true");

        PlatformFactory platformFactory = (PlatformFactory) applicationContext.getBean(PlatformFactory.class);
        TImpl timpl = new TImpl(platformFactory.getPlatformService("binance"));
        timpl.service();
    }
}
