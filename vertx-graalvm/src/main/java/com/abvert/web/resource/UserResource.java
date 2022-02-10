/**
 * 
 */
package com.abvert.web.resource;

import java.util.ArrayList;
import java.util.List;

import com.abvert.web.base.BaseResource;
import com.abvert.web.entity.UserEntity;
import com.abvert.web.utils.JdbcUtils;
import com.abvert.web.utils.JsonUtils;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.*;

/**
 * @date Feb 9, 2022 7:24:56 PM
 *
 * @author 大鱼
 *
 */
public class UserResource extends BaseResource {
	
	public UserResource(MySQLPool client){
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.abvert.web.base.BaseResource#registerResource(io.vertx.ext.web.Router)
	 */
	@Override
	public void registerResource(Router router) {
		// TODO Auto-generated method stub
		router.get(VERSION + "/users").handler(this::queryAllResource).failureHandler(this::failedHandler);
		router.get(VERSION + "/user/:id").handler(this::queryById).failureHandler(this::failedHandler);
	}

	private void queryAllResource(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			// query data from DB
            /*
			MySQLPool client = new JdbcUtils(routingContext.vertx()).getClient();
			client.getConnection(res -> {
				if (res.succeeded()) {
					SqlConnection conn = res.result();
					conn.query("select * from t_user").execute(result -> {
						conn.close();
						if (result.succeeded()) {
							List<UserEntity> lists = new ArrayList<>();
							result.result().forEach(item -> {
								logger.debug("item:{}",item.getValue("id").toString());
								UserEntity u = new UserEntity();
								u.setId(Integer.parseInt(item.getValue("id").toString()));
								u.setName(item.getValue("name").toString());
								lists.add(u);
							});
							future.complete(lists);
						} else {
							future.complete();
						}

					});

				} else {
					future.complete();
				}
			});*/
			List<JsonObject> lists = new ArrayList<>();
            this.getConn(routingContext).compose(conn -> this.getRows(conn,"select * from t_user limit ?, ?",0,5)).onComplete(rowSetAsyncResult -> {
				RowSet<Row> rows =rowSetAsyncResult.result();
				rows.forEach(item -> {
					logger.debug("item:{}",item.getValue("id").toString());
					JsonObject u = new JsonObject();
					//u.setId(Integer.parseInt(item.getValue("id").toString()));
					//u.setName(item.getValue("name").toString());
					u.put("id",Integer.parseInt(item.getValue("id").toString()));
					u.put("name",item.getValue("name").toString());
					lists.add(u);
				});
				future.complete(lists);

			});

		}, false, asyncResult -> {
			if (asyncResult.failed()) {
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseData(routingContext, JsonUtils.objectToJson(asyncResult.result()));
		});
	}
	
	private void queryById(RoutingContext routingContext) {
		Integer id = Integer.valueOf(routingContext.request().getParam("id").toString());
		JsonObject obj = new JsonObject();
		obj.put("id", id);
		routingContext.vertx().executeBlocking(future -> {
			routingContext.vertx().eventBus().request("user:findById", obj).onComplete(res -> {
				JsonObject o = (JsonObject) res.result().body();
				future.complete(o);
				
			});
		}, false, asyncResult -> {
			if (asyncResult.failed()) {
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseData(routingContext, JsonUtils.objectToJson(asyncResult.result()));
		});
	}
	

}
