#Kereta Provisioning - Docker
FROM tomcat:8-jre8

MAINTAINER Santiago Gomez

ENV CATALINA_HOME /usr/local/tomcat

# Configuring Tomcat Users
ADD /config/tomcat-users.xml ${CATALINA_HOME}/conf/tomcat-users.xml

# Copying Kereta Application
ADD /bin/Kereta.war ${CATALINA_HOME}/webapps/Kereta.war

# Changing to CATALINA_HOME
WORKDIR $CATALINA_HOME

#EXPOSE 8080
#CMD ["catalina.sh", "run"]
