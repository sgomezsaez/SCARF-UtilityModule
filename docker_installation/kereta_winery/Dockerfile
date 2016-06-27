#Kereta Provisioning - Docker
FROM tomcat:7-jre7

MAINTAINER Santiago Gomez

ENV CATALINA_HOME /usr/local/tomcat
ENV WINERY_LOCAL_REPO /home/ubuntu/Development/SCARF-UtilityModule/org.eclipse.winery
ENV WINERY_DOCKER_REPO /opt/winery

ENV NVM_HOME /opt/nvm
ENV NVM_VERSION 0.31.0
ENV NODE_VERSION 4
ENV BOWER_VERSION 1.7.9

ENV MAVEN_VERSION 3.3.9
ENV MAVEN_URL http://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
ENV DEBIAN_FRONTEND noninteractive
ENV PATH ${PATH}:/opt/apache-maven-${MAVEN_VERSION}/bin/

#Install winery building dependencies: https://github.com/jojow/opentosca-dockerfiles/blob/master/winery/Dockerfile
RUN apt-get update -y && \
    apt-get install -y git wget openjdk-7-jdk build-essential && \
    apt-get clean -y
RUN wget ${MAVEN_URL} && \
    tar -zxf apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    cp -R apache-maven-${MAVEN_VERSION} /opt
RUN git clone https://github.com/creationix/nvm.git ${NVM_HOME} && \
    cd ${NVM_HOME} && \
    git checkout tags/v${NVM_VERSION}
RUN . ${NVM_HOME}/nvm.sh && \
    nvm install ${NODE_VERSION} && \
    nvm use ${NODE_VERSION} && \
    nvm alias default ${NODE_VERSION} && \
    npm install -g bower@${BOWER_VERSION}

# Configuring Tomcat Users
ADD /config/tomcat-users.xml ${CATALINA_HOME}/conf/tomcat-users.xml

# Getting Winery Application
RUN mkdir -p ${WINERY_DOCKER_REPO}
WORKDIR ${WINERY_DOCKER_REPO}
RUN git clone https://sgomezsaez:pozuelo88@github.com/sgomezsaez/SCARF-UtilityModule.git
WORKDIR ${WINERY_DOCKER_REPO}/SCARF-UtilityModule/org.eclipse.winery

WORKDIR ${WINERY_DOCKER_REPO}/SCARF-UtilityModule/org.eclipse.winery/org.eclipse.winery.model.csar.toscametafile
RUN mvn install
WORKDIR ${WINERY_DOCKER_REPO}/SCARF-UtilityModule/org.eclipse.winery/org.eclipse.winery.model.selfservice
RUN mvn install
WORKDIR ${WINERY_DOCKER_REPO}/SCARF-UtilityModule/org.eclipse.winery/org.eclipse.winery.model.tosca
RUN mvn install

WORKDIR ${WINERY_DOCKER_REPO}/SCARF-UtilityModule/org.eclipse.winery
RUN . ${NVM_HOME}/nvm.sh && \
    mvn clean package

# Changing to CATALINA_HOME
WORKDIR $CATALINA_HOME

EXPOSE 8080
CMD cp $WINERY_DOCKER_REPO/org.eclipse.winery.repository/target/winery.war $CATALINA_HOME/webapps/ && \
    cp $WINERY_DOCKER_REPO/org.eclipse.winery.topologymodeler/target/winery-topologymodeler.war $CATALINA_HOME/webapps/

