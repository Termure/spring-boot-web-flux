## spring-boot-web-flux

### MongoDb docker container configuration
#### docker pull mongo
#### docker images
#### deploy the mongo image in a docker container: docker run -p 27017:27017 --name mongodb_c -d mongo
#### start docker container: docker start mongodb_c
#### start the mongo server from the docker container: docker exec -it mongodb_c bash -> mongosh