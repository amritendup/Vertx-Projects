FROM java:8-jdk

ENV VERTICLE_FILE target/school-management-service-0.0.1-SNAPSHOT.jar

# Set the location of the verticles
ENV VERTICLE_HOME /opt/verticles

EXPOSE 8081

COPY $VERTICLE_FILE $VERTICLE_HOME/
#COPY src/config/docker.json $VERTICLE_HOME/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar school-management-service-0.0.1-SNAPSHOT.jar"]
