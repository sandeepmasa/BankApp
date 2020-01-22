# Bank Account Transfer Application

Java RESTful API for money transfers between accounts

### Technologies
- JAX-RS API
- Maven (Wrapper)
- H2 in memory database
- Jetty
- Apache HTTP Client

## BUILD application

### LINUX
```
   ./mvnw clean install
```

### WINDOWS
```
   mvnw.cmd clean install
```

### RUN application
```
mvnw.cmd exec:java -Dexec.mainClass=com.bank.service.BankApplication

OR

mvn exec:java -Dexec.mainClass=com.bank.service.BankApplication
```

## Example 

1. Register New Account with  URL : http://localhost:8080/account/register

```
curl --location --request POST 'http://localhost:8080/account/register' \
--header 'Content-Type: application/json' \
--data-raw '{
	"customerId" : 91,
	"balance" : 100,
	"currencyCode" : "USD"
}'
```

2. Transfer Balance between accounts using URL : http://localhost:8080/bank
```
curl --location --request POST 'http://localhost:8080/bank' \
--header 'Content-Type: application/json' \
--data-raw '{
	"currencyCode": "USD",
	"amount" : 100.0,
	"fromAccountId" : 2,
	"toAccountId":  1
}'
```

3. List All account balances using URL : http://localhost:8080/account/list

```
curl --location --request GET 'http://localhost:8080/account/list' \
--header 'Content-Type: application/json' \
--data-raw ''
```