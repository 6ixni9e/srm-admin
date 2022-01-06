FROM harbor.db-inc.com/dbops/tomcat:8.5-jdk8-openjdk
ARG WAR_FILE

ENV WAR_FILE=${WAR_FILE}

RUN echo 'Asia/Shanghai' > /etc/timezone && rm -rf /usr/local/tomcat/webapps/*
COPY srm-admin/target/${WAR_FILE} /usr/local/tomcat/webapps/

EXPOSE 8080

CMD /usr/local/tomcat/bin/catalina.sh run
