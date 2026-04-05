# Cafe Management System

A full-stack **Cafe Management System** for managing products, categories, orders, bills, and users efficiently. Built with Angular (frontend), Spring Boot (backend), and MySQL (database).

---


## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Frontend](#frontend)
- [Backend](#backend)
- [Database](#database)
- [Setup Instructions](#setup-instructions)
- [Screenshots](#screenshots)
- [Author](#author)

---

## Project Overview
The Cafe Management System allows cafe owners and staff to manage day-to-day operations like:
- Adding and updating **categories and products**
- Placing and tracking **orders**
- Generating **bills** with PDF download
- Managing **users** with authentication
- Viewing **dashboard statistics**

This system improves operational efficiency and automates manual tasks in cafe management.

---

## Features
- User authentication (login & role-based access)
- Category management (add, edit, delete)
- Product management (add, edit, delete)
- Order management with real-time updates
- Bill generation (PDF)
- Dashboard with statistics and reports

---

## Technologies Used

### Frontend
- **Framework:** Angular 16
- **Languages:** HTML, CSS, TypeScript
- **Libraries:** Angular Material, ngx-toastr, RxJS

### Backend
- **Framework:** Spring Boot 3
- **Language:** Java 17
- **Security:** Spring Security, JWT authentication
- **Logging:** SLF4J & Logback
- **Build Tool:** Maven

### Database
- **Database:** MySQL 8
- **ORM:** Hibernate / JPA
- **Entities:** Users, Products, Categories, Orders, Bills

---

## Frontend Structure

src/

├─ app/

│ ├─ components/

│ │ ├─ login/

│ │ ├─ dashboard/

│ │ ├─ category/

│ │ ├─ product/

│ │ ├─ order/

│ │ ├─ bill/

│ │ └─ user/

│ ├─ services/

│ │ ├─ auth.service.ts

│ │ ├─ dashboard.service.ts

│ │ ├─ category.service.ts

│ │ ├─ product.service.ts

│ │ ├─ order.service.ts

│ │ └─ bill.service.ts

│ └─ app-routing.module.ts


---

## Backend Structure

src/main/java/com/in/cafe/

├─ controller/

│ ├─ UserController.java

│ ├─ ProductController.java

│ ├─ CategoryController.java

│ ├─ OrderController.java

│ └─ BillController.java

├─ service/

│ ├─ UserService.java

│ └─ ... (other services)

├─ serviceImpl/

│ └─ UserServiceImpl.java

├─ repository/

│ └─ UserRepository.java

├─ entity/

│ ├─ User.java

│ ├─ Product.java

│ ├─ Category.java

│ ├─ Order.java

│ └─ Bill.java

├─ security/

│ ├─ JwtFilter.java

│ ├─ JwtUtil.java

│ └─ SecurityConfig.java

└─ CafeManagementSystemApplication.java


---

## Database
- **Database Name:** cafe_management
- **Tables:**
  - `users` – stores user info with encrypted password
  - `categories` – product categories
  - `products` – products with price and category
  - `orders` – order details
  - `bills` – bill records with UUID

```sql
CREATE DATABASE cafe_management;

USE cafe_management;

CREATE TABLE users (...);
CREATE TABLE categories (...);
CREATE TABLE products (...);
CREATE TABLE orders (...);
CREATE TABLE bills (...);

Passwords are hashed using BCryptPasswordEncoder for security.

Setup Instructions
Backend
Clone the repository:
git clone https://github.com/akashgadekar680-dot/Cafe_Management_System.git
Navigate to backend folder:
cd Cafe_Management_System/backend
Configure MySQL credentials in application.properties.
Build and run the Spring Boot app:
mvn clean install
mvn spring-boot:run
Frontend
Navigate to frontend folder:
cd Cafe_Management_System/frontend
Install dependencies:
npm install
Run Angular app:
ng serve
Open in browser: http://localhost:4200
Screenshots


## Screenshots

### Login Page
![Login Page](https://github.com/user-attachments/assets/d418b6c2-c705-4986-a1f1-7e74652f3c61)

### Dashboard Page
![Dashboard Page](https://github.com/user-attachments/assets/165332c6-64c6-41b0-8648-c1ecca5af231)

### Order Page
![Order Page 1](https://github.com/user-attachments/assets/c44222be-b9f3-466a-b53c-32745cb60f81)
![Order Page 2](https://github.com/user-attachments/assets/dabefe49-22d3-4bfa-931f-5366d5beb604)
![Order Page 3](https://github.com/user-attachments/assets/19b5b9d1-8532-4ba4-90df-50e1de0f6c55)

### Bill Page
![Bill Page 1](https://github.com/user-attachments/assets/4524c79f-9272-4f79-be18-6665a50667e2)
![Bill Page 2](https://github.com/user-attachments/assets/265547a3-9691-4b91-913d-ec9164e246c4)
![Bill Page 3](https://github.com/user-attachments/assets/75a0f45e-f542-4147-95d1-7017f10d94d4)
![Bill Page 4](https://github.com/user-attachments/assets/d8f60745-d0ab-42bb-a8f2-04fb0949ed41)
![Bill Page 5](https://github.com/user-attachments/assets/14d961f9-36e8-4411-9dfe-acbdca831c52)

Author

Akash Gadekar – Full Stack Developer
📧 akash@gmail.com

License

This project is licensed under the MIT License.


---

If you want, I can also **make a super polished version** that includes **badges for Angular, Spring Boot, MySQL, build status, and GitHub stars**—ready for GitHub to impress anyone viewing your project.  

Do you want me to make that version too?
