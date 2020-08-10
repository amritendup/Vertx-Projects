package org.sample.verticles;

import org.sample.services.LibraryRecordService;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceBinder;


public class LibraryDataVerticle extends AbstractVerticle{

	Logger logger = LoggerFactory.getLogger(LibraryDataVerticle.class);
	
	//private  MySQLPool client;
	
	private SQLClient client;
	
	private WebClient webClient;
	
	@Override
	public void start(Promise<Void> promise) throws Exception{
		
		this.client = JDBCClient.create(vertx, initConfig());
		
		//this.client = MySQLClient.createNonShared(vertx, initConfig());
		/*
		 * MySQLConnectOptions connectOptions = new MySQLConnectOptions() .setPort(3306)
		 * .setHost("localhost") .setDatabase("library") .setUser("user1")
		 * .setPassword("mypasswd");
		 * 
		 * PoolOptions poolOptions = new PoolOptions() .setMaxSize(5);
		 */
		
		//this.client = MySQLPool.pool(vertx, connectOptions, poolOptions);

		
		//this.client = MySQLClient.createNonShared(vertx.getDelegate(), initConfig());
		
		
		LibraryRecordService.create(vertx, client, webClient,handler -> {
			
			if(handler.succeeded()) {
				logger.info("LibraryRecordService created");
				ServiceBinder binder = new ServiceBinder(vertx.getDelegate());
				binder
				.setAddress("library.record.service")
				.register(LibraryRecordService.class, handler.result());
				
				promise.complete();
			}else {
			
				logger.error("Library data service not started "+handler.cause());
				promise.fail(handler.cause());
			}
		});
	}
	
	
	private JsonObject initConfig() {
		JsonObject config = new JsonObject();
		config.put("url", "jdbc:mysql://127.0.0.1:3306/school_system");
		//config.put("url", "jdbc:mysql://172.17.0.2:3306/school_system");
		config.put("host", "localhost");
		config.put("port", 3306);
		config.put("database", "school_system");
		config.put("user", "user1");
		config.put("password", "mypasswd");
		return config;
	}
}
