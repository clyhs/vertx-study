package com.abvertx.web.config;

import com.abvertx.web.handler.HandlerManager;
import com.abvertx.web.handler.demo.DemoHandler;

public class Configure {
    private static final Configure ourInstance = new Configure();

    public static Configure getInstance() {
        return ourInstance;
    }

    public void init(){
        initHandler();
    }

    private void initHandler(){
        HandlerManager.getInstance().addHandler(new DemoHandler());
    }
}