# Movie Ticket Booking System - Backend

Spring Boot backend for the Movie Ticket Booking System.

## Setup

1. Install PostgreSQL and Redis
2. Create database: `ticket_booking`
3. Update `application.properties` with your database credentials
4. Run: `mvn spring-boot:run`

## Default Configuration

- Server Port: 8080
- Database: PostgreSQL (localhost:5432)
- Redis: localhost:6379

## Features

- JWT Authentication
- Role-based Authorization
- Redis Seat Locking (5-min TTL)
- RESTful APIs
- Exception Handling
- Transaction Management

## API Documentation

See main README.md for API endpoints.

