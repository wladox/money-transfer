### Tech stack:
1. Java 11
2. Undertow 
3. Gson 
4. Lombok
5. Junit
6. Mockito 
7. RestAssured

### API
 Endpoint      | HTTP Method        | Description   
 |------------- |:-------------:| ----- 
 /api/accounts              | GET   | returns a list of bank accounts   
 /api/accounts/{accountId}  | GET   | returns the account with id of {accountId} 
 /api/accounts              | POST  | adds a new bank account and returns it with an id attributed added 
 /api/transfers             | POST  | creates a new transaction to transfer money from an account to another account 
 /api/transfers/log?accountId={accountId} | GET   | returns all transactions related to a specific account 

### Examples

##### Create new account:
`curl -X POST 'localhost:8080/api/accounts/' --header 'Content-Type: application/json' \
--data-raw '{
	"number": "test_account_123",
	"balance": 400
}'`

##### Get all accounts:
`curl -X GET 'localhost:8080/api/accounts'`

##### Get single accounts:
`curl -X GET 'localhost:8080/api/accounts/test_account_123'`

##### Get single accounts:
`curl -X GET 'localhost:8080/api/accounts/test_account_123'`

##### Create new transfer:
`curl -X POST 'localhost:8080/api/transfers/' \
--header 'Content-Type: application/json' \
--data-raw '{
	"from": "test_account_234",
	"to": "test_account_123",
	"amount": 20
}'`

##### Show transfer history:
`curl -X GET 'localhost:8080/api/transfers/log?accountId=test_account_123'`

### :
1. Run ./gradlew run
2. Open http://localhost:8080/ping in your browser. If you see "PONG" then the application is ready.

