### Tech stack:
1. Java 11
2. Undertow 
3. Gson
4. Lombok
5. Junit
6. Mockito 
7. RestAssured

### API:
* /api/accounts GET - returns an array of bank accounts
* /api/accounts/:accountId GET - returns the account with id of :id
* /api/accounts POST - adds a new bank account and returns it with an id attributed added
* /api/transfers POST - creates a new transaction to transfer money from an account to another account
* /api/transfers/:accountId/log GET - returns all transactions related to a specific account

### How to run:
1. Run ./gradlew clean build
2. Go to build/distributions and unpack interview-backend-<VERSION>-SNAPSHOT.tar or interview-backend-<VERSION>-SNAPSHOT.zip
3. Go to interview-backend-<VERSION>-SNAPSHOT/bin and run there ./interview-backend or interview-backend.bat. You can run it with optional parameters: ./interview-backend <REDIS_HOST> <REDIS_PORT> <REST_HOST>
4. Open http://localhost:8080/ in your browse. The application is ready for usage.

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
