#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
  export $(cat .env | xargs)
fi

# Run the Java application
java -jar ./target/maisPraTi-0.0.1-SNAPSHOT.jar