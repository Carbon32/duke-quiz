#!/bin/bash

docker build -t quiz-api .
docker run -d --name quiz-api \
  --add-host=host.docker.internal:host-gateway \
  --env-file docker.env \
  -p 8081:8081 \
  quiz-api