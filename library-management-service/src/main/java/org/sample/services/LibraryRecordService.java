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
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.mysqlclient.MySQLPool;

@ProxyGen
@VertxGen
public interface LibraryRecordService {

	@GenIgnore
	static LibraryRecordService create(Vertx vertx, SQLClient client, WebClient webClient, Handler<AsyncResult<LibraryRecordService>> initHandler) {
		System.out.println("Hello Service");
		return new LibraryRecordServiceImpl(vertx, client, webClient,initHandler);
	}
	
	@GenIgnore
	static LibraryRecordService createProxy(Vertx vertx, String address) {
		return new LibraryRecordServiceVertxEBProxy(vertx.getDelegate(),address);
	}
	
	
	
	@Fluent
	LibraryRecordService findByLibraryId(String libraryId,Handler<AsyncResult<JsonObject>> resultHandler);
	
	@Fluent
	LibraryRecordService addLibraryUser(String libraryId,Handler<AsyncResult<JsonObject>> resultHandler);
	
	@Fluent
	public LibraryRecordService isSchoolUser(String libraryId, Handler<AsyncResult<Boolean>> resultHandler);
	
}
