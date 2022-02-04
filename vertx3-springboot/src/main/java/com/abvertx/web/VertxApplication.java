/**
 * 
 */
package com.abvertx.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import com.abvertx.web.base.SpringBootContext;
import com.abvertx.web.vertx.verticle.VerticleMain;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @date Feb 4, 2022 9:08:30 PM
 *
 * @author 大鱼
 *
 */
@Slf4j
@SpringBootApplication
public class VertxApplication {

	public static void main(String[] args) {
        SpringApplication.run(VertxApplication.class, args);
    }

    /**
     * 监听SpringBoot 启动完毕 开始部署Vertx
     *
     * @param event
     */
    @EventListener
    public void deployVertx(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        SpringBootContext.setApplicationContext(applicationContext);
        VerticleMain verticleMain = applicationContext.getBean(VerticleMain.class);
        Vertx vertx = Vertx.vertx();
        //部署vertx
        vertx.deployVerticle(verticleMain, handler -> {
            log.info("vertx deploy state [{}]", handler.succeeded());
        });
    }
}
