# NR Car Center

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005C3A?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)
![License](https://img.shields.io/badge/License-Educational-green?style=for-the-badge)

A Modern Digital Solution for Automotive Showrooms, Vehicle Inventory, & Customer Management

---

## Table of Contents
* [Project Overview](#project-overview)
* [Problem Statement](#problem-statement)
* [Objectives](#objectives)
* [Core Features](#core-features)
* [System Architecture](#system-architecture)
* [Technology Stack](#technology-stack)
* [Project Structure](#project-structure)
* [Database Configuration](#database-configuration)
* [API Documentation](#api-documentation)
* [Security Features](#security-features)
* [Testing Strategy](#testing-strategy)
* [Future Enhancements](#future-enhancements)
* [Team Work Distribution Summary](#team-work-distribution-summary)

---

## Project Overview
**NR Car Center** is a comprehensive web-based automotive management platform designed to digitize and optimize daily dealership operations. The system bridges the gap between car buyers and showroom administrators by facilitating vehicle showcases, multi-angle media galleries, brand organization, test drive booking management, financing application submissions, and unified communication channels.

Traditional car showroom operations suffer from manual inquiry handling, unorganized customer tracking, and scattered inventory metrics. NR Car Center resolves these pain points through a centralized digital environment that empowers administrators to oversee assets while offering clients a seamless vehicle browsing and purchasing journey.

The application is engineered using **Java Spring Boot** for robust backend ecosystems, **Spring Data JPA** coupled with **MySQL** for relational table handling, and a dynamic frontend built on **Thymeleaf**, **HTML5**, **CSS3**, and **Bootstrap**.

---

## Problem Statement
Managing a multi-brand car dealership manually introduces severe operational bottlenecks:
* Scattered inventory tracking with poor management of car brands and specifications.
* Inefficient booking pipelines for test drives and showroom consultations.
* Delayed or dropped responses to buyer inquiries.
* Complex and unstandardized processing for auto-financing and loan requests.
* Lack of automated notification systems to update buyers on their request status.

---

## Objectives
The core objectives of the NR Car Center platform are to:
* Centralize and digitize multi-brand car inventories and visual assets.
* Secure administrative control dashboards using Spring Security.
* Provide an intuitive interface for clients to schedule showroom appointments and test drives.
* Streamline buyer-to-dealer interaction via structured inquiry logs and replies.
* Simplify financing submission workflows for car buyers.
* Automate customer status alerts through targeted notification systems.

---

## Core Features

### Authentication & Access Control
* User registration and profile lifecycle management.
* Secure form-based portal authentication.
* Role-Based Access Control (RBAC) ensuring operational segregation.

### Showroom Roles
| Role | Access |
| :--- | :--- |
| **Admin** | Full system visibility (Inventory control, booking management, inquiry resolution, financing reviews). |
| **Customer** | View vehicle catalogs, book test drives, send inquiries, submit auto-financing forms. |

### Vehicle & Brand Inventory
* Structuring and showcasing car models grouped by manufacturer brands.
* Managing details such as engine specs, mileage, pricing, and availability.
* Multi-image visual galleries for comprehensive car inspections.

### Booking & Communications
* Scheduling and managing test-drive appointments.
* Submitting targeted vehicle inquiries directly from listing pages.
* Interactive inquiry dashboard for administrative replies.

### Financial Support & System Alerts
* Digitized financing request portal for vehicle payment assistance.
* Automated tracking updates and status change notifications.

Technology Stack
Backend Framework: Java, Spring Boot (Spring Web, Spring Data JPA, Spring Security)

Build/Dependency Tool: Maven

Database Management: MySQL

View Template Engine: Thymeleaf

User Interface: HTML5, CSS3, Bootstrap 5, JavaScript

Development Utilities: Spring Boot DevTools (LiveReload)

Project Structure:

NR_Car_Center/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── nrcarcenter/
│   │   │           ├── controller/      # Web Controllers & Rest APIs
│   │   │           ├── model/           # JPA Entities (Vehicle, User, Appointment, etc.)
│   │   │           ├── repository/      # Spring Data JPA Repositories
│   │   │           └── service/         # Business Logic Implementations
│   │   └── resources/
│   │       ├── templates/               # Thymeleaf Dynamic Views
│   │       │   ├── dashboard.html
│   │       │   ├── vehicles.html
│   │       │   ├── appointments.html
│   │       │   └── inquiries.html
│   │       ├── static/                  # Static assets (CSS, JS, Uploaded Vehicle Images)
│   │       └── application.properties   # Database and App Configuration
├── pom.xml                              # Project Dependency Tree
└── README.md


Database Configuration
Configure your localized MySQL setup within the src/main/resources/application.properties framework:

spring.datasource.url=jdbc:mysql://localhost:3306/autodrive_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=tariqul_islam_parbat
spring.datasource.password=1200874

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect


## System Architecture
```text
Frontend (Thymeleaf/Bootstrap) ──> Spring MVC Controller ──> Service Layer ──> Spring Data JPA Repository ──> MySQL Database

API Documentation
Users & Management
GET /api/users - Fetch system user listing

POST /api/users - Create a customer/admin record

Vehicle Inventory
GET /api/vehicles - Retrieve catalog listings

POST /api/vehicles - Add a new vehicle to the showroom

Inquiries & Bookings
POST /api/inquiries - Submit a car detail inquiry

POST /api/appointments - Reserve a date for a test drive

Security Features:
BCrypt Password Hashing: Secures administrative and client portal credentials natively.

Route Level Protections: Uses Spring Security rules to restrict operational backends to authorized roles.

Secure File/Image Handling: Controls path parsing for car profile galleries securely.

Testing Strategy:
Postman / Thunder Client: Validation mappings for appointments, financing payloads, and inquiry routes.

Browser Dev Tools: Interface rendering checks, responsive layout checks, and network activity auditing.

Future Enhancements:
Live Chat Integration: Real-time web messaging for immediate customer support.

Virtual Showroom 360°: Interactive spatial car previews directly within browser profiles.

VIN Decoder Integration: Auto-populating car specifications via external vehicle API endpoints.

Team Work Distribution Summary:
Each team member owns specific database tables and additional DB-related responsibilities as listed below:
Member Name            Student ID   DB Tables OwnedAdd
Tariqul Islam Parbat   2023200000635  users, brands 
Sayma Hossain Tamim    2023200000637   appointments
Farhana Akter          2023200000644   vehicles, vehicle_images
Md. Wasiu Rahman Siyam 2023200000646   inquiries, inquiry_replies
Sadiya Yasmin          2023200000650   financing_requests, notifications
