package org.sample.services;



import java.util.List;

import org.sample.data.StudentRecord;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.MaybeHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.ext.sql.SQLClientHelper;
import io.vertx.reactivex.ext.sql.SQLConnection;
import io.vertx.reactivex.ext.sql.SQLOperations;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;


public class StudentRecordServiceImpl implements StudentRecordService{

	private static final Logger logger = LoggerFactory.getLogger(StudentRecordServiceImpl.class);
	
	private final SQLClient client;
	
	public StudentRecordServiceImpl(Vertx vertx,SQLClient client,
			Handler<AsyncResult<StudentRecordService>> initHandler) {
		
		
		this.client = client;		
		logger.info("Hello Serviceimpl");
		
		
		
		SQLClientHelper.usingConnectionSingle(client, conn ->
		conn.rxExecute(INIT_STATEMENT)
		.andThen(Single.just(this)))
		.subscribe(SingleHelper.toObserver(initHandler));
		
		
		/*
		 * client.getConnection(ar -> { if(ar.succeeded()) {
		 * logger.info("DB Connection successful"); SQLConnection conn= ar.result();
		 * conn.execute(INIT_STATEMENT, handler ->{ if(handler.succeeded()) {
		 * logger.info("DB Operation successful");
		 * initHandler.handle(Future.succeededFuture(this));
		 * 
		 * }else { logger.error("DB Operation failed "+ handler.cause()); } } );
		 * conn.close(); }else { logger.error("Connection not successful"+ ar.cause());
		 * } });
		 */
		
		 //client.rxGetConnection().flatMapCompletable(conn ->
		 //conn.rxExecute(INIT_STATEMENT).doAfterTerminate(conn::close))
		 //.toObservable();
		
		 logger.info("Hello Serviceimpl end");
		
	}
		

	@Override
	public StudentRecordService findByStudentId(String studentId, Handler<AsyncResult<JsonObject>> resultHandler) {
		// TODO Auto-generated method stub
		
		JsonArray params = new JsonArray().add(studentId);
		
		/*
		 * client.querySingleWithParams(SELECT_STATEMENT, params, handler -> {
		 * if(handler.succeeded()) {
		 * 
		 * //JsonObject record = handler.result().getJsonObject(0);
		 * resultHandler.handle(handler); } });
		 */
		Single<ResultSet> results = client.rxQueryWithParams(SELECT_STATEMENT, params);
		
		results.map(resultSet -> {
		
			if(resultSet.getNumRows()==0) {
				return new JsonObject();
			}else {
				return resultSet.getRows().get(0);
			}
		}).subscribe(SingleHelper.toObserver(resultHandler));
		
		
		
		//.map(jsonArray ->
		//	return new StudentRecord(jsonArray.getJsonObject(0));) 
		//.toObservable();
		 
		
		return this;
	}
	
	private static final String INIT_STATEMENT = "CREATE TABLE IF NOT EXISTS student_record (\n" +
		    "  student_id VARCHAR(20) NOT NULL,\n" +
		    "  first_name VARCHAR(20) NOT NULL,\n" +
		    "  last_name VARCHAR(20) NOT NULL,\n" +
		    "  email VARCHAR(45) NOT NULL,\n" +
		    "  grade VARCHAR(11) NOT NULL,\n" +
		    "  PRIMARY KEY (student_id))";

	
	private static final String SELECT_STATEMENT = "SELECT * FROM student_record where student_id = ?" ;

}
