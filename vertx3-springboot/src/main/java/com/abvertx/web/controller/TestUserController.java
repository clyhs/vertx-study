package com.abvertx.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.abvertx.web.anno.RequestBlockingHandler;
import com.abvertx.web.anno.RequestMapping;
import com.abvertx.web.base.ControllerHandler;
import com.abvertx.web.base.ResponeWrapper;
import com.abvertx.web.enums.RequestMethod;

@RequestMapping("api/user")
@Component
public class TestUserController {

    @RequestMapping("userInfo")
    public ControllerHandler userInfo() {
        return vertxRequest -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", "大白");
            map.put("age", "18");
            vertxRequest.buildVertxRespone().responeSuccess(map);
        };
    }


    @RequestBlockingHandler
    @RequestMapping("findGirlFriend")
    public ControllerHandler findGirlFriend() {
        return vertxRequest -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            vertxRequest.buildVertxRespone().respone(new ResponeWrapper(10001, null, "girl friend not found"));
        };
    }


    @RequestMapping(value = "userLogin", method = RequestMethod.POST)
    public ControllerHandler userLogin() {
        return vertxRequest -> {
            String username = vertxRequest.getParam("username").orElse("not found");
            String password = vertxRequest.getParam("password").orElse("not found");
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password", password);
            vertxRequest.buildVertxRespone().responeSuccess(map);
        };
    }
}