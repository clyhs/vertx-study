/**
 * 
 */
package com.abvert.web.dao;

import com.abvert.web.base.BaseDao;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;

/**
 * @date Feb 10, 2022 10:03:45 PM
 *
 * @author 大鱼
 *
 */
public class UserDao extends BaseDao{
	
	public UserDao(MySQLPool client) {
		this.client = client;
	}
	
	public void findById(Message<JsonObject> msg) {
		String id = msg.body().getString("id").toString();
		String sql = "select * from t_user where id=?";
		this.client.getConnection().compose( conn -> this.getOne(conn, sql, Integer.valueOf(id)))
		.onComplete(res -> {
			msg.reply(res.result());
		});
	}
	
	protected Future<JsonObject> getOne(SqlConnection conn, String sql, Integer id) {
		Promise<JsonObject> promise = Promise.promise();
		conn.preparedQuery(sql).execute(Tuple.of(id), ar2 -> {
			conn.close();
			if (ar2.succeeded()) {
				RowSet<Row> rows = ar2.result();
				if(null!=rows && rows.size() == 1) {
					rows.forEach(item -> {
						JsonObject obj = new JsonObject();
						obj.put("id", item.getValue("id").toString());
						obj.put("name", item.getValue("name").toString());
						promise.complete(obj);
					});
				}
			} else {
				promise.fail(ar2.cause());
			}
		});
		return promise.future();
	}
}
