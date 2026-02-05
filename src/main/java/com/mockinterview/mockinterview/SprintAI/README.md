# SprintAI - AI-Powered Scrum Management Tool

## 📋 Project Overview
SprintAI is a modern Scrum project management tool that uses AI (Google Gemini) to automatically generate sprint summaries, standup reports, and retrospective insights. Built with Spring Boot and MongoDB.

## 🚀 Features
- ✅ Team & User Management
- ✅ Sprint Planning & Tracking
- ✅ Story/Task Management with Kanban board
- ✅ Daily Standup Updates
- ✅ **AI-Generated Sprint Summaries** (using Gemini API)
- ✅ Velocity Tracking & Analytics
- ✅ Blocker Detection & Management
- ✅ RESTful API with JWT Authentication
- ✅ Swagger UI for API Documentation

## 🛠️ Tech Stack
- **Backend:** Spring Boot 3.2.2, Java 17
- **Database:** MongoDB
- **Security:** Spring Security + JWT
- **AI Integration:** Google Gemini API
- **Documentation:** Springdoc OpenAPI (Swagger)
- **Build Tool:** Maven

## 📦 Prerequisites
- Java 17 or higher
- Maven 3.8+
- MongoDB 6.0+ (running locally or remote)
- Google Gemini API Key (free tier available)

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/sprintai.git
cd sprintai
```

### 2. Install MongoDB
**Option A: Local Installation**
```bash
# macOS
brew tap mongodb/brew
brew install mongodb-community

# Ubuntu/Debian
sudo apt-get install mongodb

# Start MongoDB
mongod --dbpath /path/to/data/directory
```

**Option B: Docker**
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

**Option C: MongoDB Atlas (Cloud)**
- Sign up at https://www.mongodb.com/cloud/atlas
- Create a free cluster
- Get connection string and update in application.properties

### 3. Get Google Gemini API Key
1. Go to https://ai.google.dev/
2. Click "Get API Key"
3. Create a new project (if needed)
4. Copy your API key

### 4. Configure Application
Edit `src/main/resources/application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/sprintai

# JWT Secret (CHANGE THIS!)
jwt.secret=YOUR_STRONG_SECRET_KEY_MIN_256_BITS

# Gemini API Key
gemini.api.key=YOUR_GEMINI_API_KEY_HERE
```

### 5. Build the Project
```bash
mvn clean install
```

### 6. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation
Once the application is running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## 🔑 Authentication Flow

### 1. Register a User
```bash
POST /api/auth/register
{
  "name": "Rakesh Tashildar",
  "email": "rakesh@example.com",
  "password": "password123",
  "role": "DEVELOPER"
}
```

### 2. Login
```bash
POST /api/auth/login
{
  "email": "rakesh@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "65abc123...",
  "email": "rakesh@example.com",
  "role": "DEVELOPER"
}
```

### 3. Use Token in Requests
```bash
GET /api/sprints
Header: Authorization: Bearer <your-token>
```

## 🎯 Core API Endpoints

### Teams
```
POST   /api/teams              - Create team
GET    /api/teams              - Get all teams
GET    /api/teams/{id}         - Get team by ID
PUT    /api/teams/{id}         - Update team
DELETE /api/teams/{id}         - Delete team
POST   /api/teams/{id}/members - Add member to team
```

### Users
```
GET    /api/users              - Get all users
GET    /api/users/{id}         - Get user by ID
GET    /api/users/team/{teamId} - Get users by team
PUT    /api/users/{id}         - Update user
```

### Sprints
```
POST   /api/sprints            - Create sprint
GET    /api/sprints/team/{teamId} - Get sprints by team
GET    /api/sprints/{id}       - Get sprint by ID
PUT    /api/sprints/{id}       - Update sprint
POST   /api/sprints/{id}/start - Start sprint
POST   /api/sprints/{id}/complete - Complete sprint
GET    /api/sprints/{id}/summary - **Generate AI Summary**
```

### Stories
```
POST   /api/stories            - Create story
GET    /api/stories/sprint/{sprintId} - Get stories by sprint
GET    /api/stories/{id}       - Get story by ID
PUT    /api/stories/{id}       - Update story
PUT    /api/stories/{id}/move  - Move story (TODO → IN_PROGRESS → DONE)
POST   /api/stories/{id}/block - Mark story as blocked
```

### Standup Updates
```
POST   /api/standups           - Submit standup update
GET    /api/standups/sprint/{sprintId}/date/{date} - Get daily standups
GET    /api/standups/user/{userId} - Get user's standups
GET    /api/standups/sprint/{sprintId}/summary - **Generate AI Daily Summary**
```

## 🤖 AI Integration Examples

### Generate Sprint Summary
```bash
GET /api/sprints/{sprintId}/summary
```

**AI Response Example:**
```
Sprint 23 Update:

✅ Completed: 12 out of 18 story points (67% completion rate)
- Successfully implemented JWT authentication (5 points)
- Added role-based access control (3 points)
- Fixed critical security vulnerabilities (4 points)

🔄 In Progress: 3 story points
- API documentation (3 points) - Expected completion: Tomorrow

⚠️ Blockers: 1 story blocked
- Email notification feature blocked due to SMTP configuration pending

📊 Velocity: On track to meet sprint goal. Team performance improved by 15% compared to last sprint.

💡 Recommendation: Clear the SMTP blocker today to maintain momentum.
```

### Generate Daily Standup Summary
```bash
GET /api/standups/sprint/{sprintId}/date/2024-02-07/summary
```

**AI Response Example:**
```
Daily Standup Summary - Feb 7, 2024

Team Activity:
- 3 stories completed yesterday (8 points)
- 2 stories in active development
- 1 new blocker identified

Highlights:
✅ Rakesh: Completed JWT authentication ahead of schedule
✅ Team velocity trending 20% above average

⚠️ Attention Needed:
- API Gateway integration blocked - needs DevOps support
- Code review backlog building up - need reviewers

🎯 Today's Focus:
- Clear the API Gateway blocker
- Complete RBAC implementation
- Catch up on code reviews
```

## 📁 Project Structure
```
sprintai/
├── src/
│   ├── main/
│   │   ├── java/com/sprintai/
│   │   │   ├── model/           # Entity classes
│   │   │   │   ├── User.java
│   │   │   │   ├── Team.java
│   │   │   │   ├── Sprint.java
│   │   │   │   ├── Story.java
│   │   │   │   └── StandupUpdate.java
│   │   │   ├── repository/      # MongoDB repositories
│   │   │   ├── service/         # Business logic
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── security/        # JWT & Security config
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   └── SprintAIApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Unit & Integration tests
├── pom.xml
└── README.md
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=SprintServiceTest
```

## 🐳 Docker Deployment

### Build Docker Image
```bash
docker build -t sprintai:1.0 .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

## 📊 Database Schema

### Collections:
1. **users** - User accounts and profiles
2. **teams** - Team information
3. **sprints** - Sprint planning and tracking
4. **stories** - User stories and tasks
5. **standup_updates** - Daily standup submissions

### Indexes:
- `users.email` - Unique index
- `teams.name` - Index
- `sprints.teamId + sprintNumber` - Composite index
- `stories.sprintId + status` - Composite index
- `standup_updates.userId + date` - Unique composite index

## 🔒 Security
- Password encryption using BCrypt
- JWT-based authentication
- Role-based access control (RBAC)
- CORS configuration for frontend integration
- API rate limiting (can be added)

## 🚀 Deployment

### Deploy to Heroku
```bash
heroku create sprintai-app
heroku addons:create mongolab
git push heroku main
```

### Deploy to AWS
- Use AWS Elastic Beanstalk for application
- Use AWS DocumentDB (MongoDB-compatible) for database
- Configure environment variables

## 📈 Future Enhancements
- [ ] Real-time WebSocket updates
- [ ] Slack/Teams integration
- [ ] Burndown charts and analytics dashboard
- [ ] Email notifications
- [ ] Mobile app (React Native)
- [ ] Voice-based standup updates
- [ ] GitHub/Jira integration

## 🤝 Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License
This project is licensed under the MIT License.

## 👨‍💻 Author
**Rakesh Narayan Tashildar**
- Email: tashildar563@gmail.com
- LinkedIn: [Your LinkedIn]
- GitHub: [tashildar563](https://github.com/tashildar563)

## 🙏 Acknowledgments
- Google Gemini AI for intelligent summaries
- Spring Boot team for excellent framework
- MongoDB for flexible data storage
- Open source community

---

**Built with ❤️ using Spring Boot & MongoDB**
