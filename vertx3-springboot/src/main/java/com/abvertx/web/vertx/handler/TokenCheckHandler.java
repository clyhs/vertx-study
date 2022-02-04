package com.abvertx.web.vertx.handler;

import org.springframework.stereotype.Component;

import com.abvertx.web.base.ResponeWrapper;
import com.abvertx.web.vertx.vo.VertxRespone;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

@Component
public class TokenCheckHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext event) {
        HttpServerRequest request = event.request();
        String accesstoken = request.getHeader("accesstoken");
        if (org.springframework.util.StringUtils.isEmpty(accesstoken)) {
            VertxRespone.build(event).respone(new ResponeWrapper(10002, null, "登录失效，请重新登录！"));
        } else {
            //继续下一个路由
            event.next();
        }
    }
}