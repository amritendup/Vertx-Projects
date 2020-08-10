package org.sample.verticles;

import org.sample.services.StudentRecordService;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;


public class StudentRecordAPIVerticle extends AbstractVerticle{

	private static final Logger logger = LoggerFactory.getLogger(StudentRecordAPIVerticle.class);
	private StudentRecordService dataService;
	@Override
	public void start(Promise<Void> promise) throws Exception{
		dataService = StudentRecordService.createProxy(vertx, "student.record.service");
		
		HttpServer httpServer = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		router.get("/api/student/:id").handler(this::recordHandler);
		
		
		httpServer
	      .requestHandler(router)
	      .rxListen(8081)
	      .subscribe(s -> {
	        logger.info("HTTP server running on port " + 8081);
	        promise.complete();
	      }, t -> {
	        logger.error("Could not start a HTTP server", t);
	        promise.fail(t);
	      });
		
	}
	
	
	private void recordHandler(RoutingContext context) {
		String studentId = context.request().getParam("id");
		logger.info("Parameter received :"+ studentId);
		
		dataService.findByStudentId(studentId, resultHandler -> {
			if(resultHandler.succeeded()) {
				logger.info("Requesnt is successful");
				JsonObject result = resultHandler.result();
				context.response().end(result.toString());
			}else {
				logger.error("Request failed : "+resultHandler.cause());
				context.fail(resultHandler.cause());
			}
		});
	}
	
	
}
