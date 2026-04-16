Here is the **complete, professional, final `README.md`** for your project **E-Shopping-Cart** ✅
You can copy-paste it directly into your GitHub repository.

---

 
# 🛒 E-Shopping-Cart

A full-stack e-commerce shopping cart application built using **React (Vite)** for the frontend and **Spring Boot** for the backend.  
The project includes **REST APIs, state-managed shopping cart, checkout workflow, and automated testing (UI + API)** with **Selenium, RestAssured, TestNG & Allure Reporting**.  
CI/CD is integrated via **GitHub Actions**, ensuring high-quality, production-ready builds.

---

## ✅ Key Features

### 🖥️ Frontend (React + TypeScript + Vite)
- Product listing page with real-time data from backend API
- Add to cart, remove from cart, and update quantity
- Global state management using **React Context API**
- Toast notifications for cart actions
- Fully responsive UI

### ⚙️ Backend (Spring Boot + JPA + H2 DB)
- REST API for products and checkout
- Auto schema generation using Hibernate
- In-memory H2 database
- DTO-based API response structure
- Proper HTTP status + request validation

### 🧪 Test Automation (Java)
| Layer | Framework | Coverage |
|--------|-----------|----------|
| UI Tests | Selenium + TestNG | Add to Cart, Cart Persist, Checkout Flow |
| API Tests | RestAssured | GET /products, POST /checkout |
| Reporting | Allure Reports | Full execution history, step logs |

### 🚀 DevOps & CI/CD
- GitHub Actions: Build + Test pipeline
- Maven test automation execution
- Automatic Allure report generation
- Docker support for backend (optional)

---

## 🏗️ Architecture Overview

```

```
           ┌─────────────┐
           │   Frontend  │  React + Vite (5173)
           └──────┬──────┘
                  │ REST API calls (JSON)
           ┌──────▼──────┐
           │   Backend   │  Spring Boot (8080)
           └──────┬──────┘
                  │ JPA / Hibernate
           ┌──────▼──────┐
           │   H2 DB     │  In-memory file DB
           └─────────────┘
```

```

---

## 🧰 Tech Stack

| Category | Tools |
|----------|-------|
| Frontend | React, TypeScript, Vite, Axios, Context API |
| Backend | Spring Boot 3, Java 17, JPA, H2 |
| Build Tools | Maven, Node |
| Testing | Selenium, RestAssured, TestNG, Allure Reports |
| CI/CD | GitHub Actions |
| Dev | IntelliJ, VS Code, Postman |

---

## 📂 Folder Structure

```

E-Shopping-Cart
├── backend/
│   ├── src/main/java/com/verto/shop/...
│   ├── src/main/resources/application.properties
│   └── pom.xml
├── frontend/
│   ├── src/components/...
│   ├── src/context/...
│   └── package.json
├── tests/
│   ├── ui-tests (Selenium)
│   ├── api-tests (RestAssured)
│   └── testng.xml
└── README.md

````

---

## ⚙️ Setup & Run

### 1️⃣ Clone Project
```sh
git clone https://github.com/AnujGadekar1/E-Shopping-Cart.git
cd E-Shopping-Cart
````

### 2️⃣ Start Backend (Spring Boot)

```sh
cd backend
mvn clean spring-boot:run
```

Backend runs at: `http://localhost:8080/api`

### 3️⃣ Start Frontend (React)

```sh
cd frontend
npm install
npm run dev
```

Frontend runs at: `http://localhost:5173`

---

## 🧪 Running Automated Tests

### ✅ Run All Tests

```sh
mvn clean test
```

### ✅ Generate Allure Report

```sh
allure serve allure-results
```

---

## 🛠️ API Endpoints

| Method | Endpoint        | Description           |
| ------ | --------------- | --------------------- |
| GET    | `/api/products` | Get all products      |
| POST   | `/api/checkout` | Submit checkout order |

---

## 🔄 CI/CD (GitHub Actions)

The repository includes a workflow that:

✔️ Builds backend & runs tests
✔️ Generates test reports
✔️ Fails PR if tests fail

Workflow file:
`.github/workflows/maven-test.yml`

 

## 📌 Future Enhancements

* JWT authentication & user login
* MongoDB / PostgreSQL instead of H2
* Redis caching for products
* Deployment via Docker Compose
* Add SonarQube quality gate
* Add performance testing (JMeter / k6)

---

## 👨‍💻 Author

**Anuj Gadekar**
 
