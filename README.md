# Digital Wallet Backend - Spring Boot

A clean, production-ready backend system for a digital wallet application, built using **Spring Boot**, **PostgreSQL**, and **Spring Security**.

---

## 🏗️ Technical Architecture
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.4
- **ORM**: Spring Data JPA (Hibernate)
- **Database**: PostgreSQL
- **Security**: Stateless JWT-based authentication
- **Tools**: SLF4J, ZXing

---

## 🌟 Key Features
- **User Management**: Signup and login with JWT tokens.
- **Auto-created Wallet**: Every user gets a wallet upon registration.
- **Peer-to-Peer Transfers**: Secure money transfers between registered users.
- **Balance Inquiries**: Real-time balance updates and wallet history.
- **QR Code Generation**: Generate a unique QR code for every user.
- **Analytics Dashboard**: Total and monthly spending insights.
- **Global Exception Handling**: Centralized error management for a professional API response.

---

## 📂 Project Configuration
Your `src/main/resources/application.properties` should follow this structure:

```properties
# Database settings
spring.datasource.url=jdbc:postgresql://localhost:5432/walletdb
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT settings
jwt.secret= your_jwt_secret
jwt.expiration=86400000

# Logging level
logging.level.com.wallet=DEBUG
```

---

## 🚀 API Endpoint Reference

### 🔐 Authentication (Public)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/signup` | Create a new account |
| `POST` | `/api/auth/login` | Authenticate and get a JWT token |

### 💰 Wallet (Requires Token)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/wallet/balance` | Get your current balance |
| `POST` | `/api/wallet/add-money` | Add funds to your own wallet |

### 💸 Transactions (Requires Token)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/transactions/send` | Send money to another user (via ID) |
| `GET` | `/api/transactions/history` | View all your sent/received history |

### 📊 Analytics & Utilities (Requires Token)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/qr/generate?userId={id}` | Get your user ID as a PNG QR code |
| `GET` | `/api/analytics/total-spent` | View sum of all outgoing transfers |
| `GET` | `/api/analytics/monthly-spending` | Grouped monthly spending statistics |

---

## 💻 How to Run Locally
1.  **Clone the Repo**: `git clone <your-repo-url>`
2.  **Create Database**: Run `CREATE DATABASE walletdb;` in PostgreSQL.
3.  **Build**: `mvn clean install`
4.  **Run**: `mvn spring-boot:run`

---

## ⚠️ Notes
> [!IMPORTANT]
> **Frontend in Progress**: 🖥️ A matching React/Vite frontend for this project is currently in development!

---

Developed with ❤️ as a secure, scalable Digital Wallet solution.
