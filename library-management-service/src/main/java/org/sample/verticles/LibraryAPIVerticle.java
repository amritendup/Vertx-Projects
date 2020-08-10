package org.sample.verticles;

import org.sample.services.LibraryRecordService;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;


public class LibraryAPIVerticle extends AbstractVerticle{

	private static final Logger logger = LoggerFactory.getLogger(LibraryAPIVerticle.class);
	private LibraryRecordService dataService;
	
	private ServiceDiscovery discovery ;
	
	private WebClient client;
	
	@Override
	public void start(Promise<Void> promise) throws Exception{
		dataService = LibraryRecordService.createProxy(vertx, "library.record.service");
		
		discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
				.setAnnounceAddress("school-library-address")
				.setName("school.library"));
		JsonObject json = new JsonObject()
				.put("name", "student-record");
		discovery.getRecord(new JsonObject().put("name", "student-record"), ar -> {
			if(ar.succeeded()) {
				if(ar.result()!=null) {
					ServiceReference ref = discovery.getReference(ar.result());
					
					this.client = ref.getAs(WebClient.class);
					
				}else {
					logger.error("No Matching service record found" + ar.cause());
					promise.fail("No Record");
				}
			}else {
				logger.error("Error in finding Service from Discovery",ar.cause());
				promise.fail(ar.cause());
			}
		});
		
		HttpServer httpServer = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		router.get("/api/library/add:id").handler(this::addUserHandler);
		
		
		httpServer
	      .requestHandler(router)
	      .rxListen(8082)
	      .subscribe(s -> {
	        logger.info("HTTP server running on port " + 8082);
	        promise.complete();
	      }, t -> {
	        logger.error("Could not start a HTTP server", t);
	        promise.fail(t);
	      });
		
	}
	
	
	private void addUserHandler(RoutingContext context) {
		String libraryId = context.request().getParam("id");
		logger.info("Parameter received :"+ libraryId);
		
		

		
		Promise<Boolean> findUser = Promise.promise();
		
		//HttpRequest<Buffer> httpRequest = this.client.get("/api/student/" + libraryId);
		
		dataService.isSchoolUser(libraryId, findUser);
		
		
		findUser.future().compose(found -> {
			if(!found) {
				logger.info("User does not exist");
				context.response().end("User does not exist");
				//findUser.complete(false);
			}else {
				dataService.addLibraryUser(libraryId, ar -> {
					if(ar.succeeded()) {
						logger.info("user created");
						context.response().end("user created");
						//findUser.complete(true);
					}else {
						logger.error("user creation falied" + ar.cause());
						context.response().end("user creation falied");
						findUser.fail(ar.cause());
					}
				});
			}
			return findUser.future();
		});
		
		/*
		 * findUser.future().compose(search -> { if(!search) {
		 * context.response().end("User already registered");
		 * 
		 * }else {
		 * 
		 * Promise<String> addUser = Promise.promise();
		 * this.createUserHandler(libraryId, context, addUser); return addUser.future();
		 * } }).onComplete(ar -> { if (ar.succeeded()) {
		 * logger.info("Operation Completed"); findUser.complete(); } else {
		 * logger.error("Operation Failed"+ ar.cause()); findUser.fail(ar.cause()); }
		 * });
		 */	
		
		
	}
	
	private void createUserHandler(String libraryId,RoutingContext context, Handler<AsyncResult<String>> handler) {
		dataService.addLibraryUser(libraryId, resultHandler -> {
			if(resultHandler.succeeded()) {
				
				if(resultHandler.result().isEmpty()) {
					context.response().end("User not updated");
				}else {
					context.response().setStatusCode(200);
					context.response().end(resultHandler.result().toString());
				}
				
			}else {
				context.fail(resultHandler.cause());
			}
		});
	}
	
	
}
