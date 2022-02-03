/**
 * 
 */
package com.abvertx.web.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.abvertx.web.common.BaseResource;
import com.abvertx.web.entity.UserEntity;
import com.abvertx.web.utils.JdbcUtils;
import com.abvertx.web.utils.JsonUtils;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.SqlConnection;

/**
 * @date Feb 3, 2022 11:32:55 PM
 *
 * @author 大鱼
 *
 */
public class UserResource extends BaseResource {
	
	private static List<UserEntity> resources = new ArrayList<>();

	/* (non-Javadoc)
	 * @see com.abvertx.web.common.BaseResource#registerResource(io.vertx.ext.web.Router)
	 */
	@Override
	public void registerResource(Router router) {
		// TODO Auto-generated method stub
		router.get(VERSION +"/user/:id").handler(this::queryResource).failureHandler(this::failedHandler);
		router.get(VERSION + "/user").handler(this::queryAllResource).failureHandler(this::failedHandler);
		router.post(VERSION + "/user").handler(this::createResource).failureHandler(this::failedHandler);

	}
	
	private void createResource(RoutingContext routingContext)
	{
		String bodyString = routingContext.getBodyAsString();
		if (StringUtils.isEmpty(bodyString))
		{
			throw new RuntimeException();
		}

		UserEntity resource = JsonUtils.jsonToPojo(bodyString, UserEntity.class);
		if (Objects.isNull(resource))
		{
			throw new RuntimeException();
		}

		routingContext.vertx().executeBlocking(futrue ->
		{
			resources.add(resource);
			futrue.complete();
		},false, asyncResult ->
		{
			if (asyncResult.failed())
			{
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseNoContent(routingContext);
		});
	}

	/**
	 * 查询所有资源实例
	 * @param routingContext routingContext
	 */
	private void queryAllResource(RoutingContext routingContext)
	{
		
		routingContext.vertx().executeBlocking(future ->
		{
			//query data from DB
			List<Integer> rr = new ArrayList<>();
			MySQLPool client = new JdbcUtils(routingContext.vertx()).getClient();
			client.getConnection( res -> {
				if(res.succeeded()) {
					SqlConnection conn = res.result();
					conn.query("select * from t_user").execute(result -> {
						conn.close();
						if(result.succeeded()) {
							result.result().forEach(item -> {
								rr.add(Integer.parseInt(item.getValue("id").toString()));
							});
						}
						future.complete(rr);
					});
					
				}else {
					System.out.println("xxx");
				}
			});
			//sendResponseData(routingContext, JsonUtils.objectToJson(rr));
			
		}, false, asyncResult ->
		{
			if (asyncResult.failed())
			{
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseData(routingContext, JsonUtils.objectToJson(asyncResult.result()));
		});
	}

	/**
	 * 查询指定资源实例
	 * @param routingContext routingContext
	 */
	private void queryResource(RoutingContext routingContext)
	{
		String instanceId = routingContext.request().getParam("id");
		List<Integer> resourceIds =
				resources.stream().map(resourceEntity ->resourceEntity.getId()).collect(Collectors.toList());
		if (!resourceIds.contains(instanceId))
		{
			sendResponseCustomError(routingContext, "404");
			return;
		}

		routingContext.vertx().executeBlocking(future ->
		{
			//query data from DB
			UserEntity resource =
					resources.stream().filter(resourceEntity -> resourceEntity.getId().equals(instanceId))
					.collect(Collectors.toList()).get(0);
			future.complete(resource);
		}, false, asyncResult ->
		{
			if (asyncResult.failed())
			{
				routingContext.fail(asyncResult.cause());
				return;
			}
			sendResponseData(routingContext, JsonUtils.objectToJson(asyncResult.result()));
		});
	}

}
