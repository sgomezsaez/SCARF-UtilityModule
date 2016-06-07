#!/bin/bash
set -e

echo "Installing Kereta Database..."

echo "Creating local mysql folder"
if [ ! -d "/etc/mysql/db" ]; then
	echo "creating db folder"
        sudo mkdir -p /etc/mysql/db
fi

#Running Database
sudo docker run -d --name mysql_$KERETA_DATABASE_NAME -v /etc/mysql/db:/var/lib/mysql -p $KERETA_DATABASE_HOST:$KERETA_DATABASE_PORT:$KERETA_PORT -e MYSQL_ROOT_PASSWORD=$KERETA_DATABASE_PASSWORD -e MYSQL_DATABASE=$KERETA_DATABASE_NAME -d mysql:5.5 --character-set-server=utf8 --collation-server=utf8_bin

#Connect to database from an application in another container
#docker run --name app-container-name --link my-container-name:mysql -d app-that-uses-mysql

#Connect through mysql client from another container
#sudo docker run -it --link mysql_Kereta:mysql --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"root"'

echo "Done installing Kereta database. Accessible in $KERETA_HOST:$KERETA_PORT"
