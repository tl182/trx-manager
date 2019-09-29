## Toy funds transfer manager

Run tests with `mvn clean test`.

Uberjar is available in releases section.
Run it with `java -jar trx-manager-${version}.jar`.
App starts on `localhost:8080`.
 H2 Console is available at `localhost:9090` with connection string `jdbc:h2:mem:trx_manager` and empty user and password.

### Following routes are available:
* `GET /accounts/:id` - get account information
* `POST /accounts { "balance": <balance-value> }` - create new account with given balance
* `PUT /accounts/:id { "balance": <balance-value> }` - update account with given balance
* `GET /transfers/:id` - get transfer information
* `POST /transfers { "fromId": <sender-id>, "toId": <receiver-id>, "amount": <amount> }` - transfer funds from sender to recepient
