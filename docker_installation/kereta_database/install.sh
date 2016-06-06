#!/bin/bash
set -e

echo "Installing Kereta Database..."

KERETA_DATABASE_NAME="Kereta"
KERETA_DATABASE_USER="root"
KERETA_DATABASE_PASSWORD="root"

#Running Database
docker run --name mysql_$KERETA_DATABASE_NAME -e MYSQL_ROOT_PASSWORD=$KERETA_DATABASE_PASSWORD -e $MYSQL_DATABASE=$KERETA_DATABASE_NAME -d mysql/mysql-server:5.5

#Connect to database from an application in another container
#docker run --name app-container-name --link my-container-name:mysql -d app-that-uses-mysql

#Connect through mysql client from another container
#docker run -it --link my-container-name:mysql_$KERETA_DATABASE_NAME --rm mysql/mysql-server:tag sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$KERETA_DATABASE_PASSWORD"'

echo "Done installing Kereta database..."
