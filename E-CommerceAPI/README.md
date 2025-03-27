# 🛒 E-Commerce API Project

## 📖 Project Description

This project develops a streamlined e-commerce REST API using Java, JDBC, Javalin, and PostgreSQL. It includes core functionalities such as user authentication, product management, cart operations, and order processing. Role-based access control (ADMIN and USER) is implemented to manage user privileges effectively. Additionally, user session management is utilized to store and maintain user authentication details.

---

## 🛠️ Technologies Used

* **Java** - (JDK 17 or later)
* **SQL** - PostgreSQL
* **PostgreSQL**
* **Javalin**
* **JDBC**
* **JUnit**
* **Mockito**
* **Git**
* **AWS RDS** (Optional, for deployment)
* **Sendgrid** (For email notifications)

---

## ✨ Features

### 🔑 **User Authentication**
* Secure user registration and login with role-based access control (ADMIN and USER).
* Password reset functionality with verification code sent via email.

### 👨‍💻 **User Management**
* Admin can add new users.
* Admin can deactivate (inactive) users.
* CRUD operations for user data (Update and reset password).
* Users can manage multiple addresses (Create, Read, Update, Inactive).

### 📋 **Product Category Management**
* CRUD operations for category products (Create, Read, Update, Inactive) accessible by ADMINs.

### 📦 **Product Management**
* CRUD operations for products (Create, Read, Update, Inactive) accessible by ADMINs.

### 📝 **Product Reviews Management**
* Users who have purchased a product can leave reviews.
* Admins can only read reviews but cannot create them.

### 🎁 **Discount Management**
* CRUD operations for discounts (Create, Read, Update, Inactive) accessible by ADMINs.

### 🛒 **Cart Operations**
* Add, remove, and view items in the user's shopping cart.

### 💳 **Order Processing**
* Place orders based on cart contents and track order status.

### 🔐 **User Session Management**
* Maintain user authentication state across requests.

---

## 🔄 Getting Started

1.  **Clone the repository:**

    ```bash
    git clone <your-repository-url>.git
    cd <your-repository-directory>
    ```

2.  **Set up PostgreSQL:**

    * Ensure PostgreSQL is installed and running.
    * Create a database for the project.
    * Update the `application.properties` with your database credentials.

3.  **Configure Environment Variables or application.properties:**

    * Create a `application.properties` file in `src/main/resources` or set environment variables.
    * Include database connection details, port number, and other necessary configurations.

    ```properties
    jdbc.url=jdbc:postgresql://<host>:<port>/<database>
    jdbc.user=<username>
    jdbc.password=<password>
    server.port=7000
    ```

4.  **Build with Maven:**

    ```bash
    mvn clean install
    ```

5.  **Run the application:**

    ```bash
    mvn javalin:run
    ```

    * The API will be accessible at `http://localhost:7000`.

6.  **Database Setup:**
    * Execute the SQL scripts (if any) to create the necessary database tables.

---

## 📚 Usage

After starting the application, you can use Postman or any other API testing tool to interact with the API endpoints.

**Example Endpoints (using Postman collection):**

* **Get All Users:** `GET /users`
* **Get User Addresses:** `GET /users/address`
* **Get User Orders:** `GET /users/orders`
* **Register User:** `POST /users/register` (Requires firstName, lastName, email, password in the body)
* **Login User:** `POST /users/login` (Requires email, password in the body)
* **Generate Reset Code:** `POST /users/resetCode` (Requires email in the body, sends a verification email)
* **Update User:** `PUT /users` (Requires firstName, lastName, email, password in the body)
* **Update User Status:** `PUT /users/{id}/status` (Requires status in the body)
* **Reset Password:** `PUT /users/password` (Requires resetCode, password in the body, verification via email)

---

## 📚 THIS REPOSITORY HAS BEEN CLONED FROM:
https://github.com/RodrigoPimienta/E-CommerceJavaAPI

