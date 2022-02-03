/**
 * 
 */
package com.abvertx.web.common;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;

import com.abvertx.web.utils.JsonUtils;
import com.abvertx.web.vo.Response;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @date Feb 3, 2022 11:23:04 PM
 *
 * @author 大鱼
 *
 */
public abstract class BaseResource {

	public final String VERSION = "/v1.0";
	
	/**
	 * 注册路由方法
	 * @param router router
	 */
	public abstract void registerResource(Router router);
	
	/**
	 * 无内容返回
	 * @param routingContext routingContext
	 */
	public void sendResponseNoContent(RoutingContext routingContext)
	{
		routingContext.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(HttpResponseStatus.NO_CONTENT.code())
				.end();
	}
	
	/**
	 * 返回正常请求
	 * @param routingContext routingContext
	 * @param jsonStr jsonStr
	 */
	public void sendResponseData(RoutingContext routingContext, String jsonStr)
	{
		routingContext.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(HttpResponseStatus.OK.code())
				.end(jsonStr);
	}
	
	/**
	 * 返回异常信息
	 * @param routingContext routingContext
	 */
	public void sendResponseCustomError(RoutingContext routingContext, String errorCode)
	{
		routingContext.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(Integer.parseInt(StringUtils.substring(errorCode, 0, 3)))
				.end(JsonUtils.objectToJson(new Response(errorCode, null)));
	}
	
	/**
	 * 异常处理方法
	 * @param routingContext routingContext
	 */
	public void failedHandler(RoutingContext routingContext)
	{
		HttpServerResponse response = routingContext.response();
		Throwable failure = routingContext.failure();
		if (failure instanceof ValidationException)
		{
			String errorMsg = failure.getMessage();
			response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(errorMsg);
		}
		else if (failure instanceof RuntimeException)
		{
			String errorCode = failure.getMessage();
			response.setStatusCode(Integer.parseInt(StringUtils.substring(errorCode, 0, 3)))
					.end(JsonUtils.objectToJson(new Response(errorCode, null)));
		}
		else
		{
			response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end("internal error!");
		}
	}
	
}
