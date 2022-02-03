/**
 * 
 */
package com.abvertx.web.verticle;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abvertx.web.constants.HttpStatus;
import com.abvertx.web.constants.NetDownError;
import com.abvertx.web.handler.HandlerManager;
import com.abvertx.web.handler.InterHandler;
import com.abvertx.web.message.AbstractDownMessage;
import com.abvertx.web.message.AbstractMessage;
import com.abvertx.web.message.AbstractUpMessage;
import com.abvertx.web.message.MessageFactory;
import com.abvertx.web.message.reco.IMessageRecognizer;
import com.abvertx.web.message.reco.MessageRecognizer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

/**
 * @date Feb 3, 2022 9:50:10 PM
 *
 * @author 大鱼
 *
 */
public class MainVerticle extends AbstractVerticle {
	
	private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
	
	private HttpServer httpServer;
	
	private IMessageRecognizer recognizer;

	/* (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		
		recognizer = new MessageRecognizer();
		// TODO Auto-generated method stub
		HttpServerOptions options = new HttpServerOptions()
                .setIdleTimeout(10000)
                .setIdleTimeoutUnit(TimeUnit.MILLISECONDS)
                .setTcpKeepAlive(true);

        httpServer = vertx.createHttpServer(options);
        
        httpServer.exceptionHandler( it -> {
            if(it instanceof IOException){
                logger.error("\n ---network io error:"+it.getMessage());
                return;
            }
            logger.error("\n ---net error:"+it.getMessage());
        });
        
//        httpServer.requestHandler(req -> {
//            HttpServerResponse resp = req.response().setStatusCode(200);
//            resp.putHeader("content-type", "application/json");
//            resp.putHeader("connection", "Keep-Alive");
//
//            JsonObject js = new JsonObject().put("key","hello world");
//            resp.end(js.encode());
//        });
        
        httpServer.requestHandler(this::handleRequest);

        httpServer.listen(8080,"127.0.0.1",res -> {
            if (res.succeeded()) {
                startPromise.complete();
                logger.info("HTTP server started on port 8080");
            } else {
                startPromise.fail(res.cause());
                logger.error(res.cause().getMessage());
            }
        });
	}
	
	private void handleRequest(HttpServerRequest request){
        request.bodyHandler(body -> {
            HttpServerResponse resp = request.response().setStatusCode(200);
            resp.putHeader("content-type", "application/json");
            resp.putHeader("connection", "Keep-Alive");

            //解析上传的json数据
            JsonObject upData = AbstractMessage.decodeUpMessage(body);
            if(upData == null){
                //上传json参数有误
                AbstractDownMessage output = new NetDownError((short)-1, HttpStatus.JSON_ERROR);
                output.encode();
                resp.end(output.SendMessage());

                logger.error("json error: \n" + body.toString());
                return;
            }

            AbstractDownMessage output;
            short messageId = upData.getInteger("mId",-1).shortValue();
            MessageFactory input = recognizer.recognize(messageId);
            if(input == null){
                output = new NetDownError((short)-1,HttpStatus.ERROR);
                output.encode();
                resp.end(output.SendMessage());
                return;
            }

            InterHandler handlerManager = HandlerManager.getInstance().getHandler(messageId);
            if(handlerManager == null){
                logger.error("not handler :"+messageId);
                output = new NetDownError(messageId,HttpStatus.ERROR);
                output.encode();
                resp.end(output.SendMessage());
                return;
            }

            input.decode(messageId,upData,request);
            handlerManager.handler((AbstractUpMessage)input,resp);
        });
    }
	
	@Override
    public void stop(){
        httpServer.close();
        logger.error(" AppLogin Server stop ------");
    }
	
	

}
