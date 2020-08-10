package org.sample.verticles;

import org.sample.services.StudentRecordService;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.impl.ClientHelper;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.asyncsql.MySQLClient;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.ext.sql.SQLClientHelper;
import io.vertx.reactivex.ext.sql.SQLOperations;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;


public class StudentDataVerticle extends AbstractVerticle{

	Logger logger = LoggerFactory.getLogger(StudentDataVerticle.class);
	
	//private  MySQLPool client;
	
	private SQLClient client;
	@Override
	public void start(Promise<Void> promise) throws Exception{
		
		this.client = JDBCClient.create(vertx, initConfig());
		
		//this.client = MySQLClient.createNonShared(vertx, initConfig());
		MySQLConnectOptions connectOptions = new MySQLConnectOptions()
				  .setPort(3306)
				  .setHost("localhost")
				  .setDatabase("library")
				  .setUser("user1")
				  .setPassword("mypasswd");
		
		PoolOptions poolOptions = new PoolOptions()
				  .setMaxSize(5);
		
		//this.client = MySQLPool.pool(vertx, connectOptions, poolOptions);

		
		//this.client = MySQLClient.createNonShared(vertx.getDelegate(), initConfig());
		
		logger.info("Hello111");	
		
		StudentRecordService.create(vertx, client, handler -> {
			
			if(handler.succeeded()) {
				logger.info("StudentRecordService created");
				ServiceBinder binder = new ServiceBinder(vertx.getDelegate());
				binder
				.setAddress("student.record.service")
				.register(StudentRecordService.class, handler.result());
				
				promise.complete();
			}else {
				logger.info("Hello");
				logger.error("Student data service not started "+handler.cause());
				promise.fail(handler.cause());
			}
		});
	}
	
	
	private JsonObject initConfig() {
		JsonObject config = new JsonObject();
		config.put("url", "jdbc:mysql://127.0.0.1:3306/school_system");
		config.put("host", "localhost");
		config.put("port", 3306);
		config.put("database", "school_system");
		config.put("user", "user1");
		config.put("password", "mypasswd");
		return config;
	}
}
