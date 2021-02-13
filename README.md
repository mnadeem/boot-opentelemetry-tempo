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

And then 

````bash
docker-compose up
````