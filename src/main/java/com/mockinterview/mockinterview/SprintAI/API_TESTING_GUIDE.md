# 🚀 SprintAI - API Testing Guide

## First API: User Registration

### 📋 Prerequisites
1. MongoDB running on `localhost:27017`
2. Application running on `http://localhost:8080`

---

## 🏃 Step-by-Step Setup

### 1. Start MongoDB
```bash
# Option 1: If MongoDB installed locally
mongod

# Option 2: Using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Option 3: MongoDB Atlas (cloud)
# Update application.properties with your connection string
```

### 2. Update application.properties
Make sure your `src/main/resources/application.properties` has:
```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/sprintai

# JWT Secret (CHANGE THIS!)
jwt.secret=MySecretKeyForSprintAIApplication2024MustBeAtLeast256BitsLong
jwt.expiration=86400000

# Server
server.port=8080
```

### 3. Build and Run
```bash
# Using Gradle
./gradlew bootRun

# Or build JAR and run
./gradlew bootJar
java -jar build/libs/sprintai-1.0.0.jar
```

You should see:
```
🚀 SprintAI is running!
📚 API Documentation: http://localhost:8080/swagger-ui.html
🧪 Test endpoint: http://localhost:8080/api/auth/test
```

---

## 🧪 Testing the API

### Test 1: Check if API is Running

**Request:**
```bash
curl http://localhost:8080/api/auth/test
```

**Expected Response:**
```json
{
  "message": "SprintAI API is running! 🚀",
  "timestamp": "2024-02-05T12:30:45.123"
}
```

✅ **If you see this, your API is working!**

---

### Test 2: Register a New User

**Request (using curl):**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rakesh Tashildar",
    "email": "rakesh@sprintai.com",
    "password": "password123",
    "role": "DEVELOPER"
  }'
```

**Request (using Postman):**
```
Method: POST
URL: http://localhost:8080/api/auth/register
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "name": "Rakesh Tashildar",
  "email": "rakesh@sprintai.com",
  "password": "password123",
  "role": "DEVELOPER"
}
```

**Expected Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWtlc2hAc3ByaW50YWkuY29tIiwiaWF0IjoxNzA3MTI2NjQ1LCJleHAiOjE3MDcyMTMwNDV9.xyz...",
  "type": "Bearer",
  "id": "65c0f1a2b3d4e5f6a7b8c9d0",
  "name": "Rakesh Tashildar",
  "email": "rakesh@sprintai.com",
  "role": "DEVELOPER",
  "teamId": null
}
```

✅ **Success! User registered and JWT token generated!**

---

### Test 3: Try to Register with Same Email (Should Fail)

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Another User",
    "email": "rakesh@sprintai.com",
    "password": "password456"
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "error": "Email already exists: rakesh@sprintai.com"
}
```

✅ **Validation working!**

---

### Test 4: Register with Invalid Data

**Request (missing required field):**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@test.com",
    "password": "123"
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "name": "Name is required",
  "password": "Password must be between 6 and 40 characters"
}
```

✅ **Field validation working!**

---

### Test 5: Register Different Roles

**SCRUM_MASTER:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@sprintai.com",
    "password": "secure123",
    "role": "SCRUM_MASTER"
  }'
```

**PRODUCT_OWNER:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@sprintai.com",
    "password": "secure456",
    "role": "PRODUCT_OWNER"
  }'
```

**QA_ENGINEER:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mike Tester",
    "email": "mike@sprintai.com",
    "password": "secure789",
    "role": "QA_ENGINEER"
  }'
```

---

## 🔍 Verify Data in MongoDB

### Option 1: MongoDB Compass (GUI)
1. Download: https://www.mongodb.com/products/compass
2. Connect to: `mongodb://localhost:27017`
3. Database: `sprintai`
4. Collection: `users`
5. You should see your registered users!

### Option 2: MongoDB Shell
```bash
mongosh

use sprintai
db.users.find().pretty()
```

**Expected Output:**
```json
{
  "_id": ObjectId("65c0f1a2b3d4e5f6a7b8c9d0"),
  "name": "Rakesh Tashildar",
  "email": "rakesh@sprintai.com",
  "password": "$2a$10$abcdefghijklmnopqrstuvwxyz...", // Hashed!
  "role": "DEVELOPER",
  "teamId": null,
  "createdAt": ISODate("2024-02-05T07:30:45.123Z"),
  "active": true,
  "_class": "com.sprintai.model.User"
}
```

✅ **Password is hashed with BCrypt!**

---

## 📊 What Just Happened?

### Backend Flow:
```
1. Request → AuthController.registerUser()
2. Controller → UserService.registerUser()
3. UserService checks if email exists
4. UserService hashes password with BCrypt
5. UserService saves user to MongoDB
6. JwtUtil.generateToken() creates JWT
7. AuthResponse sent back to client
```

### Security Features:
✅ Password hashed with BCrypt (never stored plain text)
✅ JWT token generated for authentication
✅ Email uniqueness validated
✅ Field validation (@Valid, @NotBlank, @Email)
✅ CORS enabled for frontend integration

---

## 🎯 Success Checklist

- ✅ MongoDB running
- ✅ Application starts without errors
- ✅ Test endpoint returns success
- ✅ User registration works
- ✅ Duplicate email rejected
- ✅ Validation works for invalid data
- ✅ Different roles can be assigned
- ✅ Data visible in MongoDB
- ✅ Password hashed in database
- ✅ JWT token generated

---

## 🐛 Common Issues & Solutions

### Issue 1: "Connection refused to MongoDB"
**Solution:**
```bash
# Start MongoDB
mongod

# Or check if it's running
ps aux | grep mongod
```

### Issue 2: "JWT secret must be at least 256 bits"
**Solution:** Update `application.properties`:
```properties
jwt.secret=MyVeryLongSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongToWorkProperly
```

### Issue 3: "Port 8080 already in use"
**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

### Issue 4: "Could not autowire PasswordEncoder"
**Solution:** Make sure `SecurityConfig.java` has the `@Bean` annotation on `passwordEncoder()`

---

## 🎓 Understanding the Response

### JWT Token Breakdown:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWtlc2hAc3ByaW50YWkuY29tIn0.signature
└─────────┬─────────┘ └────────────┬─────────────┘ └────┬────┘
        Header              Payload (user email)      Signature
```

**How to use the token:**
```bash
# Save token from response
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Use in subsequent requests (for future APIs)
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/users
```

---

## 🚀 Next Steps

Now that registration works, you can:

1. ✅ **Test with Postman** - Create a collection
2. ✅ **Test with Swagger UI** - Go to http://localhost:8080/swagger-ui.html
3. ⏭️ **Build Login API** - Authenticate existing users
4. ⏭️ **Build Team APIs** - Create and manage teams
5. ⏭️ **Build Sprint APIs** - Start creating sprints!

---

## 💡 Pro Tips

### Save JWT Token for Testing:
```bash
# Register and save token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","password":"password123"}' \
  | jq -r '.token')

echo $TOKEN
```

### Postman Environment Variables:
Create these variables:
- `baseUrl`: http://localhost:8080
- `token`: (paste your JWT token here)

Then use: `{{baseUrl}}/api/auth/register`

---

**🎉 Congratulations! Your first API is working!**

Ready to build the next API? 🚀
