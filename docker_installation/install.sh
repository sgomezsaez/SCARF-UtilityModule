#!/bin/bash

set -e

echo "Provision SCARF-U Module. Utility-based decision making for the distribution of cloud applications. Part of the SCARF-T Environment"

HOST=$(ifconfig eth0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}')

#Kereta Config
export KERETA_PORT=8090
export KERETA_HOST=$HOST

#Kereta Database Config
export KERETA_DATABASE_HOST=$HOST
export KERETA_DATABASE_PORT=3306
export KERETA_DATABASE_NAME="Kereta"
export KERETA_DATABASE_USER="root"
export KERETA_DATABASE_PASSWORD="root"

CURRENT_DIR=$(pwd)

echo "Provisioning Kereta Application"
cd ./kereta_app
chmod 755 ./*.sh
source ./install.sh
cd $CURRENT_DIR

echo "Provisioning Kereta Database"
cd ./kereta_database
chmod 755 ./*.sh
source ./install.sh
cd $CURRENT_DIR

echo "Done Provisioning SCARF-U Module. Accessible in $HOST:8080"
