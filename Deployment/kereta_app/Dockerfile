#Kereta Provisioning - Docker
FROM tomcat:8-jre8

MAINTAINER Santiago Gomez

ENV CATALINA_HOME /usr/local/tomcat

# Configuring Tomcat Users
COPY /config/tomcat-users.xml ${CATALINA_HOME}/conf/tomcat-users.xml

# Copying Kereta Application
COPY /bin/Kereta.war ${CATALINA_HOME}/webapps/Kereta.war

# Changing to CATALINA_HOME
WORKDIR $CATALINA_HOME

RUN apt-get -y update
#RUN apt-get -y install mysql-client

EXPOSE 8080
CMD ["catalina.sh", "run"]
