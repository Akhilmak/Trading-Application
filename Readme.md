
![Logo](./Assets/Logo.png )

<div style="justify-content: center; align-items: center; width:80vh">

Welcome to **TradeMind**, a cryptocurrency trading application built with a modern stack using *Shadcn/ui* for the frontend and *Spring Boot* for the backend. TradeMind is designed to provide a seamless and efficient trading experience for users, enabling them to manage assets, track orders, and conduct transactions in the ever-evolving world of cryptocurrency.


## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Api Reference](#api-reference)
- [Contributing](#contributing)

## Features

- **User Authentication**: Secure login and registration with JWT authentication.
- **Asset Management**: View and manage various cryptocurrency assets.
- **Order Management**: Create, track, and manage buy/sell orders.
- **Payment Processing**: Seamless integration for processing payments and transactions.
- **Watchlist**: Add assets to a personal watchlist for easy tracking.
- **Two-Factor Authentication**: Enhanced security for user accounts.

## Tech Stack

**Client:** React, TailwindCSS, Shadcn/ui

**Server:** Spring Boot

**Database**: MySQL

**Security**: JWT (JSON Web Tokens)


## Installation

### Prerequisites

- Java 11 or higher
- Node.js (for frontend)
- MySQL

### Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Akhilmak/TradeMind
   ```

2. Navigate to the backend directory:
   ```bash
   cd trademind/Application-backend
   ```

3. Update the `application.properties` file with your database and environment configurations.

4. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd trademind/Application-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

## Usage

1. **Register** a new user via the registration endpoint.
2. **Login** to your account and receive a JWT token.
3. Use the token to access protected routes for managing orders, assets, and transactions.
4. Explore the UI built with Shadcn/ui to interact with the application.


## API Reference

### SignUp

```http
  POST localhost:8080/auth/signup
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `fullName` | `String` | **Required**. |
| `email` | `String` | **Required**. |
| `password` | `String` | **Required**. |


### Get User Profile By JWT

```http
  GET localhost:8080/api/user/profile
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `{{JWT}}`      | `string` | **Required** |

#### Get Coin by ID(*CoinName*)

```http
  GET localhost:8080/coins/details/bitcoin
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `coinName`      | `string` | **Required** |







## Contributing

We welcome contributions! If you have suggestions or improvements, please fork the repository and submit a pull request.

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

</div>