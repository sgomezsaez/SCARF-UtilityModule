#!/bin/bash
set -e

IPADD=$(ifconfig eth0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}')
KERETA_PORT=8090

echo "Installing KERETA in $IPADD..."

docker build -t scarf/kereta .

echo "Running KERETA..."

docker run -i -p $IPADD:$KERETA_PORT:8080 scarf/kereta "/usr/local/tomcat/bin/catalina.sh jdpa start"

echo "KERETA running on $IPADD:$KERETA_PORT"
