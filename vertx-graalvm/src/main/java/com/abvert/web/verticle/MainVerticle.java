/**
 * 
 */
package com.abvert.web.verticle;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abvert.web.VertxHttpServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * @date Feb 5, 2022 8:25:15 PM
 *
 * @author 大鱼
 *
 */
public class MainVerticle extends AbstractVerticle{
	
	private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
	
	private HttpServer httpServer;

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start(io.vertx.core.Promise)
	 */
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		// TODO Auto-generated method stub
		//super.start(startPromise);
		
		HttpServerOptions options = new HttpServerOptions()
                .setIdleTimeout(10000)
                .setIdleTimeoutUnit(TimeUnit.MILLISECONDS)
                .setTcpKeepAlive(true);

        httpServer = vertx.createHttpServer(options);

        //
		Router router = Router.router(vertx);
		
		router.route("/hello").handler(rc -> {
			logger.info("Got hello request");
			rc.response().end("World");
		});        

		httpServer.requestHandler(router).listen(8080,"127.0.0.1",res -> {
            if (res.succeeded()) {
            	logger.info("start server success!");
                startPromise.complete();
            } else {
            	logger.info("start server failed! " + res.cause());
                startPromise.fail(res.cause());
            }
        });
	
	}
	
	
//	3.8.2
//	@Override
//	public void start(Future<void> startFuture) throws Exception {
//		HttpServerOptions options = new HttpServerOptions().setIdleTimeout(10000)
//				.setIdleTimeoutUnit(TimeUnit.MILLISECONDS).setTcpKeepAlive(true);
//
//		httpServer = vertx.createHttpServer(options);
//
//		Router router = Router.router(vertx);
//		
//		router.route("/hello").handler(rc -> {
//			//log.info("Got hello request");
//			rc.response().end("World");
//		});
//		
//		httpServer.requestHandler(router).listen(8080, asyncResult -> {
//			if (asyncResult.failed()) {
//				System.out.println("start server failed! " + asyncResult.cause());
//			} else {
//				System.out.println("start server success!");
//			}
//		});
//	}

}
