# 🏨 Hotel Room Booking Management System (Core Java)

A professional console-based Hotel Room Booking Management System built using Core Java.

This project simulates the workflow of a hotel reservation system where users can search rooms, create reservations, perform guest check-in/check-out, cancel bookings, generate invoices, and maintain booking records.

---

# 📌 Project Overview

The Hotel Room Booking Management System is designed to demonstrate real-world implementation of Object-Oriented Programming concepts using Java.

The application follows a modular layered architecture that separates business logic, data management, validation, and presentation.

It provides an efficient booking workflow while preventing duplicate room allocation through date-overlap validation.

---

# 🚀 Features

- Room Inventory Management
- Search Available Rooms
- Room Reservation System
- Guest Information Management
- Booking Reference ID Generation
- Reservation Cancellation
- Guest Check-In
- Guest Check-Out
- Invoice Generation
- Tax Calculation
- Booking Status Tracking
- Persistent Booking Log
- Input Validation
- Date Validation
- Email Validation
- Phone Number Validation
- Layered Architecture
- Exception Handling
- Modular Java Design

---

# 🛠 Tech Stack

- Java
- Core Java
- Object Oriented Programming (OOP)
- Java Collections Framework
- Java File Handling
- Java Time API
- BigDecimal
- Exception Handling
- Console Based UI

---

# 🧠 OOP Concepts Used

- Classes & Objects
- Encapsulation
- Abstraction
- Enums
- Composition
- Layered Architecture
- Method Overloading
- Data Encapsulation

---

# 📂 Project Structure

```
Hotel-Room-Booking-Management-System/
│
├── src/
│
├── model/
│   ├── Room.java
│   ├── Guest.java
│   ├── Booking.java
│   ├── RoomType.java
│   ├── RoomStatus.java
│   └── BookingStatus.java
│
├── repository/
│   └── DataRepository.java
│
├── service/
│   └── HotelService.java
│
├── utility/
│   ├── InputValidator.java
│   └── FileManager.java
│
├── main/
│   └── Main.java
│
├── data/
│   └── bookings.txt
│   └── FileManager.java
└── README.md
```

---

# 📌 Functional Modules

### Room Module

- Room Categories
- Room Status
- Room Pricing

### Guest Module

- Guest Details
- Email Validation
- Phone Validation

### Booking Module

- Reservation
- Booking Status
- Invoice
- Tax Calculation

### Repository Module

- Room Storage
- Booking Storage

### Service Module

- Business Logic
- Room Availability Algorithm
- Check-In
- Check-Out
- Cancellation

### Utility Module

- Input Validation
- File Management
- Booking Record Storage

---

# 📈 Workflow

```
Application Start
        │
        ▼
Load Hotel Inventory
        │
        ▼
Display Main Menu
        │
        ▼
Search Available Rooms
        │
        ▼
Create Reservation
        │
        ▼
Generate Booking ID
        │
        ▼
Guest Check-In
        │
        ▼
Generate Invoice
        │
        ▼
Guest Check-Out
        │
        ▼
Save Booking Record
```

---

# 📚 Java Concepts Demonstrated

- Packages
- Enums
- Collections
- Maps
- Lists
- Scanner
- FileWriter
- BufferedWriter
- DateTimeFormatter
- LocalDate
- BigDecimal
- Exception Handling

---

# ▶️ How to Run

Compile

```bash
javac -d bin src/model/*.java src/repository/*.java src/utility/*.java src/service/*.java src/main/Main.java
```

Run

```bash
java -cp bin main.Main
```

---

# Future Improvements

- JDBC Database Integration
- MySQL Support
- Admin Login
- Employee Module
- Customer Login
- Payment Gateway
- Email Notifications
- GUI Version (JavaFX/Swing)
- REST API
- Spring Boot Migration

---

# Learning Outcomes

- Core Java Development
- Object-Oriented Programming
- Layered Architecture
- Business Logic Implementation
- Java Collections
- File Handling
- Input Validation
- Console Application Development

---

# Acknowledgement

I would like to express my sincere gratitude to **Mr. Umesh Yadav Sir**, Founder of **Indian Institute of Placement (IIP)**, for his continuous guidance, mentorship, and encouragement throughout the development of this project.

This project was completed under the guidance of **Indian Institute of Placement (IIP)** in collaboration with **EDC IIT Delhi**. The knowledge, practical approach, and industry-oriented learning provided through this program greatly contributed to the successful completion of this project.

Thank you for inspiring students to build real-world software solutions and continuously encouraging practical learning.

---

# Author

**Ayush Kumar Dubey**
Computer Science & Engineering Student
BIT Gorakhpur

GitHub:
https://github.com/ayushkrdubey-23