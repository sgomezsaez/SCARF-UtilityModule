#!/bin/bash
set -e

echo "Installing KERETA..."

sudo docker build -t scarf/kereta .

echo "Running KERETA..."

sudo docker run -d --rm -p $KERETA_HOST:$KERETA_PORT:8080 scarf/kereta

echo "KERETA running on port "$KERETA_PORT"
