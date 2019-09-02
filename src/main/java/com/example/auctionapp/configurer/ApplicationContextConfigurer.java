package com.example.auctionapp.configurer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zll on 2018/7/26.
 */
@Configuration
public class ApplicationContextConfigurer implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextConfigurer.applicationContext;
    }

    public static <T> T getBean(Class<T> t) {
        return ApplicationContextConfigurer.applicationContext.getBean(t);
    }

}
