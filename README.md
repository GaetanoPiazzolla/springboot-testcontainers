

Example Curl:

curl http://localhost:8181/customers
curl http://localhost:8181/customers/tanuzzo
curl -d '{"id":123,"name":"Tanuzzo"}' -H "Content-Type: application/json" -X POST http://localhost:8181/customers
