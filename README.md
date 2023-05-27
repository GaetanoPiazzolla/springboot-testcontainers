## Spring Boot with TestContainers

It's possible to run the Application in development mode on local host from any IDE
by running the class in `test/gae.piaz.springtc.TestApplication` or through the gradle command `bootTestRun`.  This application uses 4 Containers: Redis, Kafka, Postgres, and a custom Python Flask based app.
Prerequisite is to have a docker runtime available.

Web requests to hit the Endpoints:

- Retrieve all customers from local postgres db:
`curl http://localhost:8181/customers`

- Retrieve all customers from external service:
`curl http://localhost:8181/customers-ext`

- Retrieve a customer by name (cached in Redis):
`curl http://localhost:8181/customers/<name>`

- Send a new customer Event through Kafka (then perist in DB):
`curl -d '{"id": <id>,"name": <name>}' -H "Content-Type: application/json" -X POST http://localhost:8181/customers`