🏨 Hotel Management System (HMS)
📌 Project Overview

The Hotel Management System (HMS) is a Java-based desktop application developed as part of the Software Engineering (GL) module.
The project aims to design and implement a modular, extensible, and well-structured system that simulates real hotel operations while strictly following the Model–View–Controller (MVC) architecture and applying multiple object-oriented design patterns.

This project serves both as:

A functional hotel management application

An academic case study demonstrating clean software design and architecture

🎓 Academic Context

Faculty: Computer Science

Department: Artificial Intelligence and Data Science

Academic Level: 3rd Year – AI Engineering (ING IA)

Module: Software Engineering (GL)

Academic Year: 2025/2026

👥 Team Members

Akkouchi Nesrine

Ben Zahia Malak

Tebani Hiba

Delhoum Lina Fatma Zohra

🛠️ Technologies & Tools

Programming Language: Java

GUI Framework: JavaFX

Architecture: Model–View–Controller (MVC)

Data Persistence: JSON files

Serialization: Gson

🧱 System Architecture

The application follows a strict MVC structure:

src/
 ├── model/
 ├── controller/
 ├── view/
 ├── data/
 └── main/


This structure ensures:

Clear separation of concerns

Low coupling and high cohesion

Easy maintenance and extensibility

🎯 Project Objectives

Apply the MVC architectural pattern

Implement multiple design patterns coherently

Develop a fully functional GUI-based application

Ensure data persistence

Produce clean, maintainable, and well-documented code

⚙️ Core Features
👤 User Management

User registration (Sign Up)

User authentication (Sign In)

Role-based access control (ADMIN / USER)

Secure session management

🏨 Room Management

Manage room details (type, price, features, status)

Automatic availability updates

Support for individual rooms and room groups

🔍 Room Filtering

Filter by:

Room type

Price

Availability

Features

Combine multiple filters dynamically

📅 Reservation Management

Reservation creation with date validation

Availability and conflict prevention

Reservation lifecycle management:

Active

Cancelled

Completed

💰 Pricing System

Dynamic pricing calculation

Strategy-based pricing (normal / discount)

Optional services (restaurant, spa, parking)

💬 Messaging System

Client-to-admin messaging

Message persistence

Real-time updates using Observer pattern

Admin conversation management

📊 Statistics & Monitoring (Admin)

User statistics

Room statistics (available, occupied, maintenance)

Reservation monitoring

Controlled deletion operations

🧩 Design Patterns Used
Pattern	Usage
Singleton	DataManager & SessionManager
Strategy	Pricing & Room Filtering
Composite	Room and Room Groups
Observer	Messaging & Notifications
🖥️ Graphical User Interface

Fully GUI-based using JavaFX

No business logic inside views

Role-based dashboards

Reusable UI components

User-friendly and modern layout

▶️ How to Run the Project

Install Java JDK

Install JavaFX

Clone or download the project

Open the project in your IDE (IntelliJ IDEA / Eclipse)

Run the Main class

Interact with the application through the GUI

📁 Data Management

All data is stored in JSON files

Centralized access through DataManager (Singleton)

Automatic loading and saving

Ensures consistency and persistence

🚧 Project Constraints

Strict MVC compliance

Mandatory design patterns

GUI-only interaction

No external frameworks

Academic evaluation requirements

✅ Conclusion

The Hotel Management System successfully demonstrates the practical application of software engineering principles, MVC architecture, and design patterns in a real-world inspired system.
The project meets all academic requirements and serves as a strong foundation for scalable and maintainable Java applications.

