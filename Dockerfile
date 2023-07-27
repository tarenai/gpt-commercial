FROM eclipse-temurin:8-jre
MAINTAINER apeto
ARG user=spring
ARG group=spring
ENV SPRING_HOME=/home/spring
RUN groupadd -g 1000 ${group} \
	&& useradd -d "$SPRING_HOME" -u 1000 -g 1000 -m -s /bin/bash ${user} \
	&& mkdir -p $SPRING_HOME/config \
	&& mkdir -p $SPRING_HOME/logs \
	&& chown -R ${user}:${group} $SPRING_HOME/config $SPRING_HOME/logs

VOLUME ["$SPRING_HOME/config", "$SPRING_HOME/logs"]
USER ${user}
WORKDIR $SPRING_HOME
ARG JAR_FILE=ai-mechanician/target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENV JAVA_OPTS -Xmx1024m -Djava.awt.headless=true -XX:+HeapDumpOnOutOfMemoryError \
 -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -Xloggc:/home/spring/logs/gc.log \
 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9876 -Dcom.sun.management.jmxremote.ssl=false \
 -Dcom.sun.management.jmxremote.authenticate=false -Dlogging.file.path=/home/spring/logs \
 -Duser.timezone=Asia/Shanghai SPRING_PROFILES_ACTIVE dev
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar $SPRING_HOME/app.jar"]
