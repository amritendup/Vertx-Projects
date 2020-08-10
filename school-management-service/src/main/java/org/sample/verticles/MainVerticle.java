package org.sample.verticles;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;

public class MainVerticle extends AbstractVerticle{

	Logger logger = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start(Promise<Void> promise) throws Exception {
		
		Promise<String> studentDeployment = Promise.promise();
		
		vertx.deployVerticle(new StudentDataVerticle(), studentDeployment);
		
		studentDeployment.future().compose(id -> {
			logger.info("Deployment id : "+ id);
		      Promise<String> httpVerticleDeployment = Promise.promise();
		      vertx.deployVerticle(
		        "org.sample.verticles.StudentRecordAPIVerticle",
		        new DeploymentOptions().setInstances(1),
		        httpVerticleDeployment);

		      return httpVerticleDeployment.future();

		    }).onComplete(ar -> {
		      if (ar.succeeded()) {
		    	  logger.info("Verticle deployment succeeded");
		        promise.complete();
		      } else {
		    	  logger.error("Verticle deployment failed "+ ar.cause());
		        promise.fail(ar.cause());
		      }
		    });
		  }
}
