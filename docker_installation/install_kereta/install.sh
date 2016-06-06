#!/bin/bash
set -e

echo "Installing KERETA..."

echo "Installing Tomcat and MySQL..."

JDK_NAME="openjdk-7-jdk"
sudo apt-get -y install openjdk-7-jdk
#CHANGE THE PATH IF NOT INSTALLED THERE
export JAVA_HOME=/usr/lib/jvm/java-8-oracle/
export PATH=$PATH:/usr/lib/jvm/java-8-oracle/bin

# Affirm completion, optionally delete archive, and exit
echo "Java Development Kit version $JDK_VER successfully installed!"

#TOMCAT
TOMCAT_VERSION="8.0.1"
TOMCAT_ARCHIVE=$TOMCAT_VERSION.tar.gz
TOMCAT_URL="http://archive.apache.org/dist/tomcat/tomcat-8/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz"
CATALINA_HOME="/opt/apache-tomcat-${TOMCAT_VERSION}"

TOMCAT_USER="kereta"
TOMCAT_PASSWORD="kereta"

WAR_FILE="Kereta.war"

DB_USER="root"
DB_PSSW="root"
KERETA_DB_NAME="Kereta"

wget ${TOMCAT_URL} -O /tmp/catalina.tar.gz && \
        tar -zxf /tmp/catalina.tar.gz -C /opt && \
        rm /tmp/catalina.tar.gz


if [ ! -r $WAR_FILE ]; then
    echo "War files are missing. Download it and run this again to deploy it." 1>&2
else
    cp $WAR_FILE /opt/apache-tomcat-${TOMCAT_VERSION}/webapps
fi

cp tomcat-users.xml $CATALINA_HOME/conf/tomcat-users.xml
echo "Configured access for user $TOMCAT_USER and password $TOMCAT_PASSWORD ..."

sudo apt-get -y install mysql-server

if [ ! -d /var/lib/mysql/$KNOWLEDGE_BASE_DB_NAME ] ; then

	echo "Database $KERETA_DB_NAME does not exist. Creating..."
	mysql -u$DB_USER -p$DB_PSSW -e "create database $KERETA_DB_NAME";

fi


$CATALINA_HOME/bin/catalina.sh run

echo "Process finished go to http://localost:8080/Kereta"
