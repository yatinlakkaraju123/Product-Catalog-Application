# 🛒 E-Commerce Product Catalog Platform

A full-stack product management system with role-based access control, JWT authentication, and cloud storage integration built with Spring Boot and React.

## 🎯 Overview

This application provides separate interfaces for Administrators and Customers to manage and browse products. 
Admins can perform CRUD operations on products and categories, while customers can browse the product catalog with pagination.

## ✨ Key Features

### 🔐 Authentication & Authorization
- JWT-based authentication with access and refresh tokens
- Access tokens: 15-minute expiry for security
- Refresh tokens: 15-day expiry for user convenience
- Automatic token refresh on 401 responses (seamless user experience)
- Role-based access control (RBAC) for Admin and User roles

### 👨‍💼 Admin Features
- Product Management: Create, Read, Update, Delete products
- Category Management: Create, Read , Update and Delete Categories and also Assign or change Categories of Products
- Image Upload: Upload product images to AWS S3
- Server-side pagination with configurable page size and sorting

### 👤 User Features
- Browse product catalog with responsive design
- Paginated product listing
- View product details with images

### 🚀 Technical Highlights
- RESTful API architecture
- Server-side pagination using Spring Data JPA Pageable
- AWS S3 integration for scalable image storage
- One-to-Many JPA entity relationships (Category ↔ Product)
- Centralized exception handling with @RestControllerAdvice
- CORS configuration for secure cross-origin requests

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.x
- **Security:** Spring Security with JWT
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Cloud Storage:** AWS S3

### Frontend
- **Framework:** React  with TypeScript
- **UI Library:** Material-UI (MUI)
- **HTTP Client:** Axios with interceptors
- **Routing:** React Router v6
- **State Management:** React Hooks (useState, useEffect)

## 📸 Screenshots

### Admin 

#### Admin Dashboard
<img width="1917" height="606" alt="image" src="https://github.com/user-attachments/assets/d6e65615-2bcb-4534-bc97-2db9e7845087" />

*Admin can view the number of categories and products in this dashboard*

#### Admin Product Manager
<img width="1913" height="707" alt="image" src="https://github.com/user-attachments/assets/abd6d9c2-776c-43a9-9654-14ba57553fde" />

*Admin can Perform CRUD Operations on Products in this page*

#### Admin Category Manager
<img width="1910" height="682" alt="image" src="https://github.com/user-attachments/assets/33a0324d-3a5d-401f-86fc-121e7873e730" />

*Admin can perform CRUD Operations on Categories*
### User

#### User Dashboard
<img width="1918" height="716" alt="image" src="https://github.com/user-attachments/assets/d2d40b84-0803-4297-929b-a33baf29e319" />
*User can view the number of categories and products in this dashboard and he can go to the view products page*

#### User Products Page
<img width="1633" height="801" alt="image" src="https://github.com/user-attachments/assets/f3d2f24a-1d68-4454-849f-67a2bb70fc59" />

*Responsive product cards with pagination*

### Login Page
<img width="1118" height="787" alt="image" src="https://github.com/user-attachments/assets/3865a7ce-423c-46cc-8c09-f4c67fbc8e38" />

*JWT-based authentication*

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Node.js 16+ and npm
- PostgreSQL database
- AWS Account with S3 bucket configured

### Backend Setup

1. **Clone the repository**
```bash
git clone https://github.com/yatinlakkaraju123/Product-Catalog-Application.git
cd Product-Catalog-Application/server
```

2. **Configure env variables**
```create a file called .env and add the following to that file
DB_URL=(database URL)
DB_USERNAME=(database username)
DB_PASSWORD=(database password)
JWT_SECRET=(jwt secret)
S3_BUCKETNAME=(aws s3 bucket name)
S3_ACCESSKEY=(aws s3 access key)
S3_SECRETKEY=(aws s3 secret key)
S3_REGION=(aws s3 region )

```

3. **Run the application**
```bash
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:9007`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd ../client/client
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure API endpoint**

Update `src/services/api/ApiClient.ts`:
```typescript
const API_BASE_URL = "http://localhost:9007";
```

4. **Start the development server**
```bash
npm run dev
```

Frontend will start on `http://localhost:5173`

### Default Users

**Admin Account:**
- Username: `admin`
- Password: `admin123`

**User Account:**
- Username: `user`
- Password: `user123`

*(Note: Create these users manually or via signup endpoint)*

## 📡 API Endpoints

### Authentication
```
POST   /auth/v1/signup          - Register new user
POST   /auth/v1/login           - Login and get JWT tokens
POST   /auth/v1/refreshToken    - Refresh access token
POST   /auth/v1/logout          - Logout and invalidate refresh token
```

### Categories (Admin only)
```
GET    /category/               - Get all categories (paginated)
GET    /category/{id}           - Get category by ID
POST   /category/               - Create new category
PUT    /category/{id}           - Update category
DELETE /category/{id}           - Delete category
```

### Products
```
GET    /products/                           - Get all products (paginated)
GET    /products/{id}                       - Get product by ID
GET    /products/categories/{id}/products   - Get products by category
POST   /products/                           - Create product (Admin only)
PUT    /products/{id}                       - Update product (Admin only)
DELETE /products/{id}                       - Delete product (Admin only)
```

### Query Parameters (Pagination)
```
?page=0              - Page number (default: 0)
?size=10             - Page size (default: 10)
?sortField=id        - Sort field (default: id)
?direction=ASC       - Sort direction (ASC/DESC)
```

## 🏗️ Architecture

### Backend Architecture
```
├── controllers/     # REST API endpoints
├── services/        # Business logic layer
├── repositories/    # Data access layer (JPA)
├── entities/        # JPA entity models
├── dtos/            # Data transfer objects
├── config/          # Security, CORS, AWS S3 config
├── filters/         # JWT authentication filter
└── exceptions/      # Global exception handling
```

### Frontend Architecture
```
├── pages/           # Page components (Admin, User)
├── components/      # Reusable UI components
├── services/        # API service layer
│   ├── api/         # API client and service methods
│   └── Auth.tsx     # Authentication context
└── hooks/           # Custom React hooks
```

## 🔒 Security Features

- Password encryption using BCrypt
- JWT tokens with signature verification
- Refresh token rotation for enhanced security
- CORS configuration for trusted origins
- SQL injection prevention via JPA parameterized queries
- CSRF protection for state-changing operations
- Secure file upload validation

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Full-stack development with modern frameworks
- ✅ RESTful API design and implementation
- ✅ JWT authentication with refresh token mechanism
- ✅ Role-based authorization
- ✅ Cloud storage integration (AWS S3)
- ✅ Database relationships and JPA
- ✅ React TypeScript with Material-UI
- ✅ Axios interceptors for token refresh
- ✅ Server-side pagination and sorting



## 👨‍💻 Author

**Yatin Lakkaraju**

- 📧 Email: lakkarajuyatin@gmail.com
- 💼 LinkedIn: [linkedin.com/in/yatinlakkaraju](https://linkedin.com/in/yatinlakkaraju)
- 🐙 GitHub: [github.com/yatinlakkaraju123](https://github.com/yatinlakkaraju123)

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- Spring Boot Documentation
- React Documentation
- Material-UI Components
- AWS S3 SDK
  

---

⭐ If you find this project helpful, please consider giving it a star!
```

