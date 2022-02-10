package com.abvert.web.base;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abvert.web.utils.JdbcUtils;
import com.abvert.web.utils.JsonUtils;
import com.abvert.web.verticle.MainVerticle;
import com.abvert.web.vo.Response;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

public abstract class BaseResource {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

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
		if (failure instanceof RuntimeException)
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
	
	protected Future<SqlConnection> getConn(RoutingContext routingContext) {
		MySQLPool client = new JdbcUtils(routingContext.vertx()).getClient();
		Promise<SqlConnection> promise = Promise.promise();
		client.getConnection(ar1 -> {
			if (ar1.succeeded()) {
				logger.info("Connected");
				// Obtain our connection
				SqlConnection conn = ar1.result();
				promise.complete(conn);
			} else {
				promise.fail(ar1.cause());
			}
		});
		return promise.future();
	}

	// 第二步 用获取到的链接查询数据库
	protected Future<RowSet<Row>> getRows(SqlConnection conn, String sql,Integer page, Integer size) {
		Promise<RowSet<Row>> promise = Promise.promise();
		conn.preparedQuery(sql).execute(Tuple.of(page, size), ar2 -> {
			// Release the connection to the pool
			conn.close();
			if (ar2.succeeded()) {
				promise.complete(ar2.result());
			} else {
				promise.fail(ar2.cause());
			}
		});
		return promise.future();
	}
	
}