## Spring Boot with TestContainers

It's possible to run the Application in development mode on local host from any IDE
by running the class in `test/gae.piaz.springtc.TestApplication` or through the gradle command `bootTestRun`. This
application uses 4 Containers: Redis, Kafka, Postgres, and a custom Python Flask based app.
Prerequisite is to have a docker runtime available.

Furtner details and explanations are available here: https://gaetanopiazzolla.github.io/java/docker/springboot/2023/05/27/springboot-tc.html

---
CURL Requests :

Retrieve all customers from local postgres db:
```shell
curl http://localhost:8181/customers
```

Retrieve all customers from external service:
```shell
curl http://localhost:8181/customers-ext
```

Retrieve a customer by name (cached in Redis):
 ```shell
curl http://localhost:8181/customers/pino
```
Send a new customer Event through Kafka (then persist in DB):
```shell
curl -d '{"id": 9191,"name": "Curlo"}' -H "Content-Type: application/json" -X POST http://localhost:8181/customers
```
