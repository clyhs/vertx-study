/**
 * 
 */
package com.abvertx.web.verticle;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abvertx.web.resource.UserResource;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @date Feb 3, 2022 11:10:10 PM
 *
 * @author 大鱼
 *
 */
public class MainVerticle extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

	private HttpServer httpServer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.vertx.core.AbstractVerticle#start(io.vertx.core.Future)
	 */
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		// TODO Auto-generated method stub
		HttpServerOptions options = new HttpServerOptions().setIdleTimeout(10000)
				.setIdleTimeoutUnit(TimeUnit.MILLISECONDS).setTcpKeepAlive(true);

		httpServer = vertx.createHttpServer(options);

		Router router = Router.router(vertx);
		// We need request bodies
		router.route().handler(BodyHandler.create());
		// We consume application/json, also produce it
		router.route().consumes("application/json").produces("application/json");

		// Register API
		registerResource(router);

		httpServer.requestHandler(router).listen(8080, asyncResult -> {
			if (asyncResult.failed()) {
				System.out.println("start server failed! " + asyncResult.cause());
			} else {
				System.out.println("start server success!");
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.vertx.core.AbstractVerticle#stop(io.vertx.core.Future)
	 */
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		// TODO Auto-generated method stub

		httpServer.close(stopFuture);
	}

	private void registerResource(Router router) {
		UserResource demoResource = new UserResource();
		demoResource.registerResource(router);
	}

}
