# RevPay – Digital Payment & Financial Management System

This is a backend-based financial management system designed to simulate a digital payment platform. RevPay allows users to manage wallets, perform transactions, generate invoices, and apply for loans. The project demonstrates core backend development skills using Java, JDBC, and Oracle PL/SQL.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Database Design](#database-design)
- [Links](#links)
- [Built With](#built-with)
- [What I Learned](#what-i-learned)
- [Continued Development](#continued-development)
- [Author](#author)

---

## Overview

RevPay is a console-based financial transaction management system built using Java and Oracle Database. The system supports two types of users (Business and Customer) and allows them to perform digital financial operations securely.

The system includes:

- User registration and authentication
- Wallet creation and balance management
- Sending and receiving transactions
- Invoice creation and tracking
- Loan application management
- Secure password handling
- PL/SQL stored procedure integration

This project demonstrates backend architecture design using layered architecture (Model–Repository–Service–Utility structure).

---

## Features

- Secure user registration and login
- One-to-one User–Wallet mapping
- Wallet-to-wallet transactions
- Invoice creation (Business → Customer)
- Loan application system
- Transaction status tracking
- Database-driven operations using JDBC
- Stored procedure usage for optimized database operations

---

## Database Design

### Entities

- **User**
- **Wallet**
- **Transaction**
- **Invoice**
- **Loan**

### Relationships

- A User owns one Wallet
- A Wallet can send and receive multiple Transactions
- A Business User can create Invoices
- A Customer receives Invoices
- A Business User can apply for Loans

The database operations are handled using Oracle SQL and PL/SQL stored procedures.

---


The project follows a layered architecture:

- **Model Layer** → Entity classes
- **Repository Layer** → Database interaction (JDBC)
- **Service Layer** → Business logic
- **Utility Layer** → DB connection & helper functions

---

## Links

**Solution URL:**  
👉 https://github.com/Riddhith/P1-RevPay

**Live Demo:**  
Console-based application — run locally using IntelliJ IDEA or any Java IDE.

---

## Built With

- Java (Core Java)
- JDBC
- Oracle Database
- PL/SQL Stored Procedures
- IntelliJ IDEA
- JUnit (Unit Testing)
- Log4j (Logging)
- Layered Architecture (Model–Repository–Service Pattern)

---

## What I Learned

By building RevPay, I strengthened my understanding of:

- Layered backend architecture design
- Database normalization and ER modeling
- Writing complex SQL queries
- Using PL/SQL stored procedures for business logic
- Implementing JDBC for database connectivity
- Managing transactions and ensuring data consistency
- Writing unit tests using JUnit
- Secure password handling techniques

This project improved my confidence in designing real-world financial backend systems.

---

## Continued Development

Possible enhancements for the project:

- Add REST API layer using Spring Boot
- Implement JWT-based authentication
- Add role-based access control
- Introduce centralized exception handling
- Implement transaction rollback mechanisms
- Add frontend UI using React or Angular
- Containerize using Docker
- Deploy to cloud (Azure / AWS)
- Improve test coverage using Mockito
- Add full logging configuration

---

## Author

**GitHub** – https://github.com/Riddhith  
**LinkedIn** – https://www.linkedin.com/in/banerjeeriddhith  
**Portfolio** – https://riddhith04.wixsite.com/riddhith-banerjee

---

