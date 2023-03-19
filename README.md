# Simple Shop Application (WIP)

![для сашки](https://user-images.githubusercontent.com/112489897/226066859-a818c9da-01ae-4772-afb9-466b273e2eea.png)


Application designed through microservice architecture <br>
The main goal of the project is to build and dockerize a microservice architecture application <br>
This design approaches in which a single application is built as a set of small services.<br>
Each module runs in its own process and interacts with other modules.<br>
Modules work independently and are built around business needs and perform a specific function.<br>
Each business transaction which spans multiple microservices are split into micro-service specific local transactions and they are executed in a sequence to complete the business workflow.(SAGA Pattern)

By this Application you can place an order and receive an email about its status

# How to run
To run this application locally you need Docker to be installed <br> <br>

1.  Download project from Github
2.  Using the terminal, switch to ```shop-microservices\setup-files``` folder
3.  Run ```docker compose up -d```
4.  Check that all containers are up and running
5.  Go to ```localhost:8080``` to get client secret
  - Use Credentials ```admin:admin```, go to ```clients -> spring-cloud-client -> Credentials -> Regenerate Secret```
6.  Make requests to API-Gateway(localhost:8181) with Auth 2.0(for example using Postman/SoapUI)
  - Grant Type: Client Credentials
  - Access Token URL: http://keycloak:8080/realms/spring-boot-shop-realm/protocol/openid-connect/token
  - Client ID: spring-cloud-client
  - Client Secret: see step 5

# Endpoints
- Create order POST:```http://localhost:8181/api/order``` <br>
Example:<br>
```
{
    "userId":1,
    "skuCode":"ps4",
    "quantity":10,
    "price":200

}
```
- Get Orders GET:```http://localhost:8181/api/order```<br> <br>
- Create product POST:```http://localhost:8181/api/product``` <br>
Example:<br>
```
{
    "name":"product1",
    "description":"food",
    "price":320 
}
```
- Get Products GET:```http://localhost:8181/api/order```<br> <br>
- Create item in inventory POST:```http://localhost:8181/api/inventory``` <br>
Example:<br>
```
{
    "skuCode":"xbox",
    "quantity":10
}
```
- Get items from Inventory GET:```http://localhost:8181/api/inventory```<br> <br>
- Get balance for user with id x :```http://localhost:8181/api/payment/balance/x```<br> <br>

# Monitoring
- Eureka Discovery Server(eureka:password):```http://localhost:8761```<br>
- Zipkin distibuted tracing:```http://localhost:8761```<br>
- Mailhog:```http://localhost:8025```<br>
- Prometheus:```http://localhost:9090```<br>
- Grafana(admin:password -> SpringBootStatistics dashboard):```http://localhost:3000```<br>

## P.S. Authentication failed issue
Happens when you call docker container from your local.<br>
This is because your client application(like Postman) can't call keycloak container <br>
You should inform your system to redirect traffic to keycloak by change your hosts file,then you will be able to call keycloak from your localhost <br>
Just add line  ```127.0.0.1 keycloak``` in hosts file in administrator mode(using Notepad++ for example)<br>
Windows:<br>
C:\Windows\System32\drivers\etc\hosts<br>
Linux:
etc/hosts <br>
Mac:
/private/etc/hosts<br>






