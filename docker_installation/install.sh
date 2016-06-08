#!/bin/bash

set -e

echo "Provision SCARF-U Module. Utility-based decision making for the distribution of cloud applications. Part of the SCARF-T Environment"

HOST=$(ifconfig eth0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}')

#Kereta Database Config

CURRENT_DIR=$(pwd)

echo "Creating local mysql folder"
if [ ! -d "/etc/mysql/db" ]; then
        echo "creating db folder"
        sudo mkdir -p /etc/mysql/db
fi

sudo docker-compose up -d

echo "Done Provisioning SCARF-U Module. Accessible in $HOST:8080"
