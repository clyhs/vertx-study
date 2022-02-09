/**
 * 
 */
package com.abvert.web.utils;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

/**
 * @date Feb 3, 2022 11:54:31 PM
 *
 * @author 大鱼
 *
 */
public class JdbcUtils {

	MySQLPool client;
	
	public JdbcUtils(Vertx vertx) {
		MySQLConnectOptions connectOptions = new MySQLConnectOptions()
			    .setPort(3306)
			    .setHost("127.0.0.1")
			    .setDatabase("vertxdemo")
			    .setUser("root")
			    .setPassword("123456")
			    .setPort(3306)
			    .setCharset("utf8");
		PoolOptions poolOptions = new PoolOptions()
			    .setMaxSize(5);
		
		client = MySQLPool.pool(vertx, connectOptions, poolOptions);
	}

	/**
	 * @return the client
	 */
	public MySQLPool getClient() {
		return client;
	}
}
