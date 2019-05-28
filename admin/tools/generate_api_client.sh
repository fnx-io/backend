#!/usr/bin/env bash

wget http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.5/swagger-codegen-cli-2.4.5.jar -O swagger-codegen-cli.jar

java -jar swagger-codegen-cli.jar generate \
   -i ../../server/src/main/resources/swagger-api.yaml \
   -l dart \
   -o ../../api_client \
   -c gen_config.json
