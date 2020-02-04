### Tech stack:
1. Java 11
2. Undertow 
3. Gson
4. Lombok
5. Junit
6. Mockito 
7. RestAssured

### API:

 Endpoint      | HTTP Method        | Description  
 |------------- |:-------------:| -----|
 /api/accounts              | GET   | returns a list of bank accounts 
 /api/accounts/{accountId}  | GET   | returns the account with id of {accountId} 
 /api/accounts              | POST  | adds a new bank account and returns it with an id attributed added 
 /api/transfers             | POST  | creates a new transaction to transfer money from an account to another account 
 /api/transfers/:accountId/log | GET   | returns all transactions related to a specific account 


### How to run:
1. Run ./gradlew run
2. Open http://localhost:8080/ping in your browser. If you see "PONG" then the application is ready.

Components:
* request dispatcher
* request handler
* transfer service
* audit log (persistence)

Test cases `transfer`:
* sufficient balance
* non negative amount
* accounts not same
* concurrent transactions
