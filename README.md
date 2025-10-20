# 🛒 E-Commerce Product Catalog Platform

A full-stack product management system with role-based access control, JWT authentication, and cloud storage integration built with Spring Boot and React.

## 🎯 Overview

This application provides separate interfaces for Administrators and Customers to manage and browse products. 
Admins can perform CRUD operations on products and categories, while customers can browse the product catalog with  pagination.

## ✨ Key Features

### 🔐 Authentication & Authorization
- JWT-based authentication with access and refresh tokens
- Access tokens: 15-minute expiry for security
- Refresh tokens: 15-day expiry for user convenience
- Automatic token refresh on 401 responses (seamless user experience)
- Role-based access control (RBAC) for Admin and User roles

### 👨‍💼 Admin Features
- Product Management: Create, Read, Update, Delete products
- Category Management: Organize products into categories
- Image Upload: Upload product images to AWS S3
- Server-side pagination with configurable page size and sorting

### 👤 User Features
- Browse product catalog with responsive design
- Filter products by category
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
- **Framework:** React 18 with TypeScript
- **UI Library:** Material-UI (MUI)
- **HTTP Client:** Axios with interceptors
- **Routing:** React Router v6
- **State Management:** React Hooks (useState, useEffect)

## 📸 Screenshots

### Admin Dashboard
![Admin Dashboard](screenshots/admin-dashboard.png)
*Admin can manage products with DataGrid interface*

### Product Catalog (User View)
![Product Catalog](screenshots/user-catalog.png)
*Responsive product cards with pagination*

### Login Page
![Login](screenshots/login.png)
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
git clone https://github.com/yatinlakkaraju123/product-catalog-springboot-react.git
cd product-catalog-springboot-react/backend
```

2. **Configure application.properties**
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password

# AWS S3 Configuration
aws.s3.bucket-name=your-bucket-name
cloud.aws.credentials.access-key=your-access-key
cloud.aws.credentials.secret-key=your-secret-key
cloud.aws.region.static=your-region

# JWT Secret
secret=your-jwt-secret-key
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
cd ../frontend
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
npm start
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

## 🚧 Future Enhancements

- [ ] Add Redis caching for frequently accessed products
- [ ] Implement Elasticsearch for advanced product search
- [ ] Add unit and integration tests (JUnit, Mockito)
- [ ] Docker containerization
- [ ] CI/CD pipeline with GitHub Actions
- [ ] Shopping cart functionality
- [ ] Order management system
- [ ] Payment gateway integration

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
- NeetCode for DSA learning

---

⭐ If you find this project helpful, please consider giving it a star!
```

