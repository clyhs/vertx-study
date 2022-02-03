package com.abvertx.web.handler.demo;

import com.abvertx.web.constants.HandlerCode;
import com.abvertx.web.handler.InterHandler;
import com.abvertx.web.message.AbstractUpMessage;

import io.vertx.core.http.HttpServerResponse;

public class DemoHandler implements InterHandler {
    @Override
    public void handler(AbstractUpMessage up, HttpServerResponse resp) {
        //上传参数
        DemoRequest request = (DemoRequest)up;
        System.out.println("上传参数:"+ request.name + "-" + request.age);

        //返回数据
        String n = "cscscs---";
        String in = "info ---";
        //编码返回json
        DemoResponse response = new DemoResponse(getMessageId(),n,in);
        response.encode();
        resp.end(response.SendMessage());
    }

    @Override
    public short getMessageId() {
        return HandlerCode.DEMO_V1;
    }
}