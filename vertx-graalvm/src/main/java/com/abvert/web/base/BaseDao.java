/**
 * 
 */
package com.abvert.web.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abvert.web.utils.JdbcUtils;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

/**
 * @date Feb 10, 2022 10:06:02 PM
 *
 * @author 大鱼
 *
 */
public abstract class BaseDao {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected MySQLPool client;

	protected Future<SqlConnection> getConn(RoutingContext routingContext) {
		Promise<SqlConnection> promise = Promise.promise();
		this.client.getConnection(ar1 -> {
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
