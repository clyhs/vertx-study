/**
 * 
 */
package com.abvertx.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abvertx.web.verticle.MainVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @date Feb 3, 2022 11:08:20 PM
 *
 * @author 大鱼
 *
 */
public class VerthttpServer {
	
	private static final Logger logger = LoggerFactory.getLogger(VerthttpServer.class);

    public static void main(String[] args){
    	
        VertxOptions vertxOptions = new VertxOptions();
        Vertx vertx = Vertx.vertx(vertxOptions);

        //部署http服务器
        vertx.deployVerticle(MainVerticle.class.getName(),
                new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE), res -> {
            if(res.succeeded()){
                logger.warn("服务端部署成功----");
            }else {
                logger.error("服务端部署失败---" + res.cause());
            }
        });
    }

}
