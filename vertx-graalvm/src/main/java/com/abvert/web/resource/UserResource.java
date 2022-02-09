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
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.SqlConnection;

/**
 * @date Feb 9, 2022 7:24:56 PM
 *
 * @author 大鱼
 *
 */
public class UserResource extends BaseResource {

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
	}

	private void queryAllResource(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			// query data from DB

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
