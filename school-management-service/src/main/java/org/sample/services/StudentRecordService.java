package org.sample.services;



import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.mysqlclient.MySQLPool;

@ProxyGen
@VertxGen
public interface StudentRecordService {

	@GenIgnore
	static StudentRecordService create(Vertx vertx, SQLClient client, Handler<AsyncResult<StudentRecordService>> initHandler) {
		System.out.println("Hello Service");
		return new StudentRecordServiceImpl(vertx, client,initHandler);
	}
	
	@GenIgnore
	static StudentRecordService createProxy(Vertx vertx, String address) {
		return new StudentRecordServiceVertxEBProxy(vertx.getDelegate(),address);
	}
	
	
	
	@Fluent
	StudentRecordService findByStudentId(String studentId,Handler<AsyncResult<JsonObject>> resultHandler);
	
	
}
