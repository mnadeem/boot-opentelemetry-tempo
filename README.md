# Project Demonstrating Distribute Tracing

This is a demo project to demonstrate how we can integrate the following

* [Opentelemetry](https://opentelemetry.io/)
* [Grafan Tempo](https://grafana.com/oss/tempo/) Which internally utilized [Jaeger](https://www.jaegertracing.io/)
* [Spring Boot Project](https://spring.io/projects/spring-boot)

# Running



Execute the following on root folder

````bash
mvn clean package docker:build
````
## Docker Compose

And then 

````bash
docker-compose up
````

## Docker Stack

````bash
docker swarm init
docker stack deploy --compose-file docker-compose.yaml trace
docker stack services trace
docker stack rm trace
````
# Tracing

Access the endpoint

![](docs/access-flights.png)

Copy the trace id

![](docs/trace-id.png)

Get the trace information

![](docs/jaeger-trace.png)


