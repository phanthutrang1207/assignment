# Getting started
### **Prerequisites**
For building and running the application you need:
- JDK 1.8
- Maven (http://maven.apache.org/download.cgi)
- Docker (https://www.docker.com/get-started)

### Start services locally
```
$ git clone https://github.com/phanthutrang1207/assignment.git
```

This will automatically start RabbitMQ, MongoDB and Redis for you.

```
$ cd assignment && docker-compose up
```
Ports used:
- RabbitMQ: 5672 and 15672
- MongoDB: 27017
- Redis: 6379


At root project folder run services by execute these command line:

**Service discover** - Port 8761
```
$ ./service-discovery/mvnw spring-boot:run
```

**Product service** - Port 8082
```
$ ./product-service/mvnw spring-boot:run
```

**Audit service** - Port 8083
```
$ ./audit-service/mvnw spring-boot:run
```

**Api gateway** - Port 8080
```
$ ./api-gateway/mvnw spring-boot:run
```

You can access to http://localhost:8761/ to see list of registried services

You can access to http://localhost:8080/actuator/routes to see all routes that api-gate get from discovery service


**Request access token**

**NOTE:** **This request token include client id and client secret, it is only used for testing purpose, it will only avaible one week for assignment evaluation, please do not use it for other purpose.**

```
curl --location --request POST 'https://dev-8366513.okta.com/oauth2/default/v1/token' \
    --header 'Authorization: Basic MG9hM25zc2hjWnNOOEpaY0s1ZDY6MnhHelhRbTcwWDdsMjZheG90NXdJMnhXSFNxWUdiQkhzOG1OdXNpQg==' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'grant_type=client_credentials' \
    --data-urlencode 'scope=my_custom_scope'
```
Use token to acces orders api via gateway port 8080 and service name order-service which is discovered in discovery service

`http://localhost:8080/product-service/products`

```
curl --location --request GET 'http://localhost:8080/product-service/products' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer {accessToken}'
```

Here is Postman collection used for testing: 
# Requirements analysis
Build a `Smart Choice` web application which supporting user can view and compare the price of Product from different providers (Tiki, Lazada, Shopee,...)
This application has those below features:

**1. Support customer compare a product price and some information like: product name, current price, the discount rate, promotion...**

**Propose solution**: Introduce a rest service which receive user request - search keyword. Use that keyword to call query products from different providers.

**2. The customer can click on the product to see more details.**

**Propose solution**: Introduce a GET api to get a product by provider product id and provider name. We need to avoid the case provider product id is duplicated between different provider then we have to add provider name.

**3. For audit support, the company wants to keep track of the searching history of the customer. Failure to store customer activity should have no impact to the application.**

**Propose solution**: Setup a message-broker - we choose RabbitMQ. When ever user search something, we produce a message contains search keyword to message queue. We create another service act as audit service, this service will listen on message queue and consume message, store search key word into database.

**4. Handle performance issue when this website be used by a lot of people**

**Propose solution**: Run many instances for our service and setup a load balancer to balance traffic across multiple instances. We will setup an Eureka server for handling load balancing and act as discovery service.

**5. Make sure all the data must be completely returned from 3rd parties before the return to the website.**

**Propose solution**: Apply multithread to get data from providers. We get data from providers in parallel and wait for all task done, then we combine data and return to user

**6. Handle decrease the cost and improve the effectiveness of the application because each request sent to 3rd will be charged for a fee.**

**Propose solution**: Cache result by search keyword in period of time (period time configurable). Ex: When user search for keyword `apple` the result will be cache in 3 mins, in that 3 mins if user search with `apple` again we just get result from cache, no need to call third party again.
# Overview design

<img src="./images/system-design.png" width="800">


# Technique, framework, tools

## Spring boot framework
Spring Boot offers a fast way to build applications. It looks at classpath and at the beans have been configured, makes reasonable assumptions about what are missing, and adds those items. With Spring Boot, we can focus more on business features and less on infrastructure.

## Zuul and Eureka
Because we already used Spring Boot and we take it advantage, Zuul and Eureka are supprted by Spring, they are easy to setup and run in Spring ecosystem.

## Okta
Okta is an identity management service help us to manage our service and manage authentication. It is free for our developer, easy to setup and use. So that we don't have to spend time on setup our authentication server.

We use Okta to generate access token and verify access token for our services.
## Redis
Redis will act as center cache of our system. Becuase we intend to make our services scale which run in many instances, we have to make all instances share the same cache. Then when a client requests are rediected to different instances we still can get from cache for that user requests

## RabbitMQ
RabbitMQ is a message-queueing software also known as a message broker or queue manager. Simply said; it is software where queues are defined, to which applications connect in order to transfer a message or messages.

We use it because it is fast to setup and run, it just works for our requirement. Save time to focus on our manin service.

# Services
## API gateway
Zuul acts as an API gateway and authentication filter. It receives all the requests coming from the outside system, verify authentication and then delegates the requests to internal microservices. We will not expose our services to public, only expose api gateway to public, all request to our system have to pass through our API gateway.

## Service-discovery
A Service Discovery help us to centralize and manage all our services. It give our services a name and we or api gateway can use to navigate and route to our services.

## Product-service
This service responsibility for all services which related to products like: compare product price by search key or view detailed product.
By the way, it supported for sending message to queue then audit-service can comsuming for audit search products.

## Audit-service
This service reponsibility for consuming message queue and store search activities of client to database for later data analyze.

# Security
- We intend introduce these services run inside private/internal system, so we only expose API Gateway to outside.
- API Gateway will responsibilize for authentication and route request to internal services.
- Client must request JWT access token from our indicate Authorization Server by using `client ID` and `client secret` which are provided by us.
- API Gateway also handle `rate limiting`. Currently we config it limit to 10 request per 30 seconds.
# Preference

# . . .
