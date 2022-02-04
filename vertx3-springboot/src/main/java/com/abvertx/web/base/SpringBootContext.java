package com.abvertx.web.base;

import org.springframework.context.ConfigurableApplicationContext;

public class SpringBootContext {
    private static ConfigurableApplicationContext applicationContext;

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        SpringBootContext.applicationContext = applicationContext;
    }
}