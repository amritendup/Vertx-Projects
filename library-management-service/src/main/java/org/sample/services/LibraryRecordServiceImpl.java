package org.sample.services;



import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.maybe.MaybeObserveOn;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.ext.sql.SQLClientHelper;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;


public class LibraryRecordServiceImpl implements LibraryRecordService{

	private static final Logger logger = LoggerFactory.getLogger(LibraryRecordServiceImpl.class);
	
	private final SQLClient client;
	
	private WebClient webClient;
	
	public LibraryRecordServiceImpl(Vertx vertx,SQLClient client,WebClient webClient,
			Handler<AsyncResult<LibraryRecordService>> initHandler) {
		
		
		this.client = client;	
		this.webClient = webClient;
		
		logger.info("Hello Serviceimpl");
		
		
		
		SQLClientHelper.usingConnectionSingle(client, conn ->
		conn.rxExecute(INIT_STATEMENT)
		.andThen(Single.just(this)))
		.subscribe(SingleHelper.toObserver(initHandler));
		
		 logger.info("Hello Serviceimpl end");
		
	}
		

	@Override
	public LibraryRecordService findByLibraryId(String libraryId, Handler<AsyncResult<JsonObject>> resultHandler) {
		// TODO Auto-generated method stub
		
		JsonArray params = new JsonArray().add(libraryId);
		
		
		Single<ResultSet> results = client.rxQueryWithParams(SELECT_STATEMENT, params);
		
		results.map(resultSet -> {
		
			if(resultSet.getNumRows()==0) {
				return new JsonObject();
			}else {
				return resultSet.getRows().get(0);
			}
		}).subscribe(SingleHelper.toObserver(resultHandler));
		 	
		return this;
	}
	
	 
	
	@Override
	public LibraryRecordService addLibraryUser(String libraryId, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonArray params = new JsonArray()
				.add(libraryId)
				.add("student")
				.add(0)
				.add(0);
		
		
		
		Single<UpdateResult> updatedResults = client.rxUpdateWithParams(INSERT_STATEMENT, params);
		
		
		updatedResults.map(results -> {
			logger.info(results.toJson());
			if(results.getUpdated()==0) {
				return new JsonObject();
			}else {
				return results.toJson();
			}
		}).subscribe(SingleHelper.toObserver(resultHandler));;
		 	
		return this;
	}
	
	@Override
	public LibraryRecordService isSchoolUser(String libraryId, Handler<AsyncResult<Boolean>> resultHandler) {
		
		Promise<Boolean> findUser = Promise.promise();
		
		this.webClient.get("/api/student/" + libraryId).rxSend().filter(mapper -> {
			if(mapper.bodyAsJsonObject().getString("student_id").equals(libraryId)) {
				findUser.complete(true);
				return true;
			}else {
				findUser.complete(false);
				return false;
			}
		});
		
		return this;
	}
	private static final String INIT_STATEMENT = "CREATE TABLE IF NOT EXISTS library_record (\n" +
		    "  lib_user_id VARCHAR(20) NOT NULL,\n" +
		    "  user_type VARCHAR(20) NOT NULL,\n" +
		    "  item_count INT NOT NULL,\n" +
		    "  hold_days INT NOT NULL,\n" +
		    "  create_date DATETIME DEFAULT  CURRENT_TIMESTAMP,\n" +
		    "  update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
		    "  PRIMARY KEY (lib_user_id))";

	
	private static final String SELECT_STATEMENT = "SELECT * FROM library_record where lib_user_id = ?" ;

	
	private static final String INSERT_STATEMENT = "INSERT INTO library_record \n" +
		    "  (lib_user_id, user_type, item_count, hold_days)\n" +
		    "  VALUES (?,?,?,?)";

	
		   
}
