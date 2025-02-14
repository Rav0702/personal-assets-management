# Team 37

## Motivation

The goal of the project is to simplify personal financial management by providing real-time tracking, budgeting tools,
and expense analysis, helping users stay organized and make informed financial decisions. By implementing this POC we aim
to measure the feasibility of the project and integration with OpenBanking. Since we are not a certified TPP (Third Party Provider), we are using the
Deutsche Bank API Program to simulate the OpenBanking API. The main architectural goal of the project is to create a scalable and extensible
solution that can be easily integrated with other banks and financial institutions. The feasibility of the project will be measured by the
usability of the API throughout the series of use scenarios mentioned in the R1 document.

## Running the project

### Prerequisites
Docker and docker-compose are required to run the project.

### Running the project

To run the project the following steps need to be taken:
1. Clone the repository
2. Navigate to the `poc` directory of the project
3. Create the `.env` file in the `poc` directory of the project with the following content:
```
MYSQL_DATABASE=database_name
MYSQL_PASSWORD=database_password
NORIS_BANK_SIMULATION_CLIENT_ID=Simulation client ID of a NORRIS BANK app from https://developer.db.com/dashboard/developerapps
NORIS_BANK_SIMULATION_CLIENT_SECRET=Simulation client secret of a NORRIS BANK app from https://developer.db.com/dashboard/developerapps
DEUTSCHE_BANK_SIMULATION_CLIENT_ID=Simulation client ID of a DEUTSCHE BANK app from https://developer.db.com/dashboard/developerapps
DEUTSCHE_BANK_SIMULATION_CLIENT_SECRET=Simulation client secret of a DEUTSCHE BANK app from https://developer.db.com/dashboard/developerapps
```
4. Run the following command in the `poc` directory of the project:
```bash
docker-compose up --build
```
5. The project should now be running and accessible at `http://localhost:8090`
6. If any problems occur, you try running the build command:
```bash
docker build .
docker-compose up
```

## OAuth2 Authorization Flow.

In this project we are using the Authorization Code Flow with PKCE. Configured for the [Deutsche Bank API](https://developer.db.com)
Prerequisites are as follows:
1. You have an Simulation Client created in the [My Apps Dashboard](https://developer.db.com/dashboard/developerapps) with
Datasource being `Client Account Data`, App Type `Confident`,
Grant type `Authorization Code` with `Enforce PKCE`, Redirect URI being `https://localhost:8090/v1/open-banking-authorization/retrieve-access-token`,
2. You have a Test User created in the [Test Users Dashboard](https://developer.db.com/dashboard/testusers) with the Simulation Client created in the previous step.

Our application supports Norris and Deutsche Bank apps and users. Please add adequate simulation client id and secret in the .env file.

The flow is as follows:

1. The POC application generates code verifier and code challenge.
2. The POC application sends a request to the Deutsche Bank API Program authorisation service
3. The user is redirected to the Deutsche Bank API Program authorisation service
4. The user logs in and authorises the POC application
5. The Deutsche Bank API Program authorisation service redirects the user back to the POC application with an authorization code
6. The POC application sends a request to the Deutsche Bank API Program to retrieve the access token using the authorization code and the code verifier
7. The Deutsche Bank API Program responds with the access token.

Since the POC application contains only the backend project the flow is implemented to be utilized with the browser handling the redirections.

1. The user calls `v1/open-banking-authorization/{userId}/initialize-session?bank={bank}` endpoint with queryParam bank being the bank they want to use
   (can be DEUTSCHE_BANK or NORIS_BANK)
2. The response contains the url that can be copied and pasted into the browser
3. The user is redirected to the Deutsche Bank API Program authorisation service and can log in using the credentials (FKN and PIN) of Test Users 
that were provided in [Deutche Bank API program Dashboard](https://developer.db.com/dashboard/testusers)
4. The browser is then redirected back to the POC application with the authorization code 
5. Since the POC application is running on localhost, the browser will not be able to redirect back to the POC application using https,
so the url needs to be copied and pasted into postman or curl to send the request to the POC application using http instead of https
6. The POC application sends a request to the Deutsche Bank API Program to retrieve the access token using the authorization code and the code verifier

## Postman Collection

The postman collection can be found in the `POC APP.postman_collection` file in the root of the project.
It contains the requests that can be used to test the flow of the application. The collection contains the following requests:
- `Register User` - creates a user in the application, returns Id that should be used as userId in the following requests (add it as a variable in the collection)
- `Authenticate` - logs in the user and returns the JWT token that should be used in the following requests (add it as a variable in the collection)
- `Session Initialize` - initializes the session with the bank and returns the url that the user needs to be followed to login and authorize the application
- `OAUTH Callback` - Paste the url that the user is redirected to after authorizing the application in the browser (change https to http)
- `Sync Accounts in Request` - Syncs the accounts of the user synchronously in request
- `Sync Transactions in Request` - Syncs transactions for the user in request
- `Sync Accounts Kafka` - Syncs accounts using Kafka
- `Sync Transaction Kafka` - Syncs transactions using Kafka
- `Get Transactions` - Get Transactions from the database
- `Get Accounts` - Get Accounts from the database

The overall structure of our api is available in swagger ui accessible on http://localhost:8090/swagger-ui/index.html
(please note that it is autogenerated so some functionalities might not be fully correct).

## Project Structure

The POC is divided into main packages:
- `authentication` - contains the classes responsible for the Spring Security authentication to our app using JWT.
- `cashaccount` - responsible for handling cash accounts from open banking integration
- `core` - contains the configuration of the application and base classes used throughout the project.
- `kafka` - manages kafka message broker connection
- `openbankingoauth` - contains the classes responsible for the OAuth2 Authorization Code Flow with PKCE.
- `scheduler` - schedules messages on kafka queue.
- `shared` - some shared classes not linked to any domain.
- `user` - contains the classes responsible for the user management.
- `transaction` - contains the classes responsible for the transaction management.

## Testing

The project utilizes Testcontainers to run the integration tests. The tests spin off separate containers for the database,
redis, api, and mock server (used for mocking openBanking responses). The tests can be run using the maven verify command.
