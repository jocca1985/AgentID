# Acme Bank Web API

A Node.js API for the Acme Bank application, used for checking balance, deposits, and withdrawals.

## Features

- Check account balance
- Make deposits with authentication
- Make withdrawals with authentication
- JWT-based authentication
- Modern web interface

## Technologies Used

- Node.js
- TypeScript
- Hono (lightweight web framework)
- JSON Web Tokens (JWT) for authentication

## Project Structure

The project follows a clean architecture with separation of concerns:

- **Models**: Define data structures
- **Services**: Contain business logic
- **Controllers**: Handle HTTP requests and responses
- **Middleware**: Process requests before they reach controllers
- **Utils**: Utility functions and helpers

## Getting Started

### Prerequisites

- Node.js 18+ installed
- npm or yarn

### Installation

1. Clone the repository
2. Install dependencies:

```bash
npm install
```

3. Build the TypeScript code:

```bash
npm run build
```

4. Start the server:

```bash
npm start
```

The server will start on port 5025 by default. You can access the web interface at http://localhost:5025.

## API Endpoints

### `GET /api/balance`

Returns the current account balance.

**Response:**
```json
{
  "balance": 150000
}
```

### `POST /api/deposit`

Deposits money into the account. Requires authentication.

**Request:**
```json
{
  "amount": 1000
}
```

**Response:**
```json
{
  "success": true,
  "amount": 1000,
  "newBalance": 151000
}
```

### `POST /api/withdraw`

Withdraws money from the account. Requires authentication.

**Request:**
```json
{
  "amount": 500
}
```

**Response:**
```json
{
  "success": true,
  "amount": 500,
  "newBalance": 150500
}
```

## Authentication

Protected endpoints require a valid JWT token to be included in the Authorization header:

```
Authorization: Bearer your.jwt.token
```

## Development

For development with auto-restart:

```bash
npm run dev
```

## License

This project is licensed under the MIT License.