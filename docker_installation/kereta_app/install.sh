#!/bin/bash
set -e

KERETA_PORT=8888

echo "Installing KERETA..."

sudo docker build -t scarf/kereta .

echo "Running KERETA..."

sudo docker run -it --rm -p $KERETA_PORT:8080 scarf/kereta

echo "KERETA running on port "$KERETA_PORT"
