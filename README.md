
## Spring Boot with TestContainers

It's possible to run the Application in development mode on local host from any IDE 
by running the class in test/gae.piaz.springtc.TestApplication or through the gradle command "bootTestRun".

Example Curl to hit the Endpoints:

- curl http://localhost:8181/customers
- curl http://localhost:8181/customers/tanuzzo
- curl -d '{"id":123,"name":"Tanuzzo"}' -H "Content-Type: application/json" -X POST http://localhost:8181/customers
