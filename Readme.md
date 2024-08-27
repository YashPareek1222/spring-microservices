# Spring Microservices Project

## Overview

This project is a demonstration of a microservices architecture implemented using Spring Boot, Spring Cloud, and various other technologies. The system is composed of multiple independently deployable services that communicate with each other over a network, using synchronous and asynchronous communication.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Services](#services)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)

## Architecture

The architecture follows a microservices pattern where each service is responsible for a specific domain or functionality. The services communicate via RESTful APIs and are independently deployable.

### Key Components:

- **API Gateway**: Routes requests to the appropriate service and handles cross-cutting concerns such as authentication.
- **Service Registry (Eureka)**: Registers all microservices and provides service discovery.
- **Circuit Breaker (Hystrix/Resilience4j)**: Provides fault tolerance and resilience to the system.
- **Kafka**: It serves as a key component for enabling asynchronous communication between microservices, ensuring scalability, fault tolerance, and high-throughput data processing.

## Technologies Used

- **Spring Boot**
- **Spring Cloud (Eureka, Gateway)**
- **Spring Data JPA**
- **Spring Security / OAuth2**
- **Spring Kafka (for messaging)**
- **MySQL / PostgreSQL (Database)**
- **Docker**

## Services

### 1. **Product Service**
- Manages product catalog including CRUD operations.
- **Port**: 8081

### 2. **Order Service**
- Handles customer orders and integrates with Inventory service.
- **Port**: 8082

### 3. **Inventory Service**
- Update inventory for products.
- **Port**: 8083

### 4. **Notification Service**
- Sends notifications to users about order status, etc.
- **Port**: 8084

### 5. **API Gateway**
- Acts as the entry point for all requests, routing them to the appropriate microservice.
- **Port**: 8080

### 6. **Discovery Server (Eureka)**
- Service discovery mechanism.
- **Port**: 8761


## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven** or **Gradle**
- **Docker** and **Docker Compose**
- **MySQL** or **PostgreSQL** database
- **Kafka** (for messaging)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yashpareek1222/spring-microservices.git
   cd spring-microservices

2. **Build the project:**
    ```bash
   mvn clean install
   
3. **Set up Docker containers (if using Docker Compose):**
    ```bash
   docker-compose up -d

4. **Run services locally:**
    ```bash
   mvn spring-boot:run

## Running the Application:
- **Start Eureka Server:** Start the Eureka Server to enable service discovery.
- **Start Other Services:** Start the remaining microservices.

## API Endpoints
### Product Service
- **GET** **`/api/product`**: Fetch all the products.
- **POST** **`/api/product`**: Create a new product.
### Order Service
- **POST** **`/api/product`**: Place a new order.
### Inventory Service
- **GET** **`/api/inventory`**: Checks the inventory if the product is in stock or not.
### Notification Service
 Sends a notification with order ID.

## Configuration
### Application Properties
Each microservice has its own application.yml or application.properties file for configuration. Centralized configurations are stored in the Config Server.

### Security
OAuth2/JWT is used for securing the APIs. The API Gateway handles authentication and forwards the security context to the backend services.

## Future Enhancements
- Implement rate limiting in API Gateway.
- Integrate with an external payment gateway.
- Add more detailed logging and monitoring.

## Contributing
Contributions are welcome! Please fork the repository, create a feature branch, and submit a pull request.