# Fetch Rewards API

This is a REST service built using Java and Spring Boot to simulate a points-based reward service. The API allows transactions to be added to users to indicate points they can spend from a particular payer.  Points given can also be spent by the user.

This is mostly a learning project to use a framework I haven't used before.  There are plenty of room for improvement.  Basic data transfer object pattern is used to relay requests and responses, which can probably be optimized more with better understanding of the framework.  Data models can be improved to support more functionality.

## To run the project

1. Install [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows)
2. Clone the repository `git clone https://github.com/kuo22/fetch-rewards-api.git`
3. Install JDK 16 and configure it to be used in IntelliJ
4. Run the `src/main/java/com/fetchreward/pointsservice/PointsServiceApplication` class

## API usage examples

### Add points to user

`POST http://localhost:8080/transaction/{id}`

Example:

`curl -L -X POST 'http://localhost:8080/transaction/1' -H 'Content-Type: application/json' --data-raw '{ "payer": "DANNON", "points": 200, "timestamp": "2020-10-31T15:00:00Z" }'`

Request body:
```
{
    "payer": "DANNON",
    "points": 200,
    "timestamp": "2020-10-31T15:00:00Z"
}
```

Payer is the name of the party that will be supplying the user with points.

### Spend user points

`POST http://localhost:8080/spend/{id}`

Example:

`curl -L -X POST 'http://localhost:8080/spend/1' -H 'Content-Type: application/json' --data-raw '{ "points": 100 }'`

Request body:
```
{
    "points": 5000
}
```

Response body:
```
[
    {
        "payer": "UNILEVER",
        "points": -200
    },
    {
        "payer": "MILLER COORS",
        "points": -4700
    },
    {
        "payer": "DANNON",
        "points": -100
    }
]
```

### Check user balance

`GET http://localhost:8080/balance/1`

Example:

`curl -L -X GET 'http://localhost:8080/balance/1'`

Response body:

```
{
    "UNILEVER": 0,
    "MILLER COORS": 5300,
    "DANNON": 1000
}
```