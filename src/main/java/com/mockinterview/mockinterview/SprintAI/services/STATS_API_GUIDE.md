# 📊 Statistics & Count APIs - Complete Guide

## 🎯 Overview

Complete statistics and count APIs for SprintAI dashboard, sprints, and users.

---

## 📦 New Files Added (5)

### **DTOs (3):**
1. ✅ **DashboardStatsResponse.java** - Overall dashboard statistics
2. ✅ **SprintStatsResponse.java** - Sprint-specific statistics  
3. ✅ **UserStatsResponse.java** - User-specific statistics

### **Service & Controller (2):**
4. ✅ **StatsService.java** - Statistics calculation logic
5. ✅ **StatsController.java** - REST API endpoints

---

## 🚀 API Endpoints

### **1. Dashboard Statistics**
```bash
GET /api/stats/dashboard
```

**Response:**
```json
{
  "totalSprints": 15,
  "activeSprints": 2,
  "completedSprints": 10,
  "plannedSprints": 3,
  
  "totalStories": 87,
  "todoStories": 12,
  "inProgressStories": 8,
  "inReviewStories": 5,
  "doneStories": 60,
  "blockedStories": 2,
  
  "totalTasks": 245,
  "todoTasks": 45,
  "inProgressTasks": 30,
  "doneTasks": 170,
  
  "totalStoryPoints": 340,
  "completedStoryPoints": 280,
  "inProgressStoryPoints": 35,
  
  "totalTeamMembers": 12,
  "totalTeams": 3,
  
  "averageVelocity": 28,
  "completionRate": 68.96,
  
  "currentSprintId": "sprint123",
  "currentSprintName": "Sprint 23",
  "currentSprintProgress": 65
}
```

---

### **2. Sprint Statistics**
```bash
GET /api/stats/sprint/{sprintId}
```

**Example:**
```bash
GET /api/stats/sprint/sprint123
```

**Response:**
```json
{
  "sprintId": "sprint123",
  "sprintName": "Sprint 23 - Authentication",
  "sprintGoal": "Implement user authentication system",
  "status": "ACTIVE",
  "startDate": "2024-02-05",
  "endDate": "2024-02-19",
  
  "totalStories": 8,
  "todoStories": 1,
  "inProgressStories": 3,
  "inReviewStories": 2,
  "doneStories": 2,
  "blockedStories": 0,
  
  "totalTasks": 24,
  "todoTasks": 6,
  "inProgressTasks": 8,
  "doneTasks": 10,
  
  "totalStoryPoints": 34,
  "completedStoryPoints": 12,
  "remainingStoryPoints": 22,
  
  "completionPercentage": 35,
  
  "teamMembersCount": 5,
  
  "totalDays": 14,
  "daysRemaining": 7,
  "daysElapsed": 7
}
```

---

### **3. User Statistics**
```bash
GET /api/stats/user/{userId}
```

**Example:**
```bash
GET /api/stats/user/user456
```

**Response:**
```json
{
  "userId": "user456",
  "userName": "Rakesh Tashildar",
  "userEmail": "rakesh@sprintai.com",
  "role": "DEVELOPER",
  
  "assignedStories": 12,
  "completedStories": 8,
  "inProgressStories": 3,
  
  "assignedTasks": 28,
  "completedTasks": 18,
  "inProgressTasks": 6,
  "todoTasks": 4,
  
  "totalStoryPoints": 45,
  "completedStoryPoints": 30,
  
  "averageTaskCompletionHours": 4,
  "taskCompletionRate": 64.29,
  
  "activeTasksCount": 6,
  "activeStoriesCount": 3
}
```

---

### **4. Quick Counts**
```bash
GET /api/stats/counts
```

**Response:**
```json
{
  "totalSprints": 15,
  "totalStories": 87,
  "totalUsers": 12,
  "totalTeams": 3,
  "activeSprints": 2,
  "doneStories": 60
}
```

---

### **5. Count by Type**
```bash
GET /api/stats/count/{entityType}
```

**Supported Types:**
- `sprints` - Total sprints
- `stories` - Total stories
- `users` - Total users
- `teams` - Total teams
- `active-sprints` - Active sprints
- `done-stories` - Completed stories

**Examples:**
```bash
GET /api/stats/count/sprints
GET /api/stats/count/active-sprints
GET /api/stats/count/users
```

**Response:**
```json
{
  "type": "sprints",
  "count": 15
}
```

---

## 💡 Use Cases

### **1. Dashboard Loading**
```javascript
// Fetch dashboard stats
const stats = await fetch('/api/stats/dashboard');

// Display stats cards
<StatCard title="Total Sprints" value={stats.totalSprints} />
<StatCard title="Active Sprints" value={stats.activeSprints} />
<StatCard title="Completed Stories" value={stats.doneStories} />
<StatCard title="Team Velocity" value={stats.averageVelocity} />
```

### **2. Sprint Detail Page**
```javascript
// Fetch sprint stats
const sprintStats = await fetch(`/api/stats/sprint/${sprintId}`);

// Show sprint progress
<ProgressBar 
  percentage={sprintStats.completionPercentage}
  completed={sprintStats.completedStoryPoints}
  total={sprintStats.totalStoryPoints}
/>

// Show days remaining
<DaysRemaining days={sprintStats.daysRemaining} />
```

### **3. User Profile/Performance**
```javascript
// Fetch user stats
const userStats = await fetch(`/api/stats/user/${userId}`);

// Show user performance
<UserPerformance 
  completionRate={userStats.taskCompletionRate}
  assignedTasks={userStats.assignedTasks}
  completedTasks={userStats.completedTasks}
/>

// Show workload
<Workload 
  activeTasks={userStats.activeTasksCount}
  activeStories={userStats.activeStoriesCount}
/>
```

### **4. Simple Counters**
```javascript
// Just need quick counts for badges
const counts = await fetch('/api/stats/counts');

<Badge>Active Sprints: {counts.activeSprints}</Badge>
<Badge>Total Stories: {counts.totalStories}</Badge>
```

### **5. Individual Count**
```javascript
// Just need one specific count
const response = await fetch('/api/stats/count/users');
// { "type": "users", "count": 12 }

<Heading>Team Members ({response.count})</Heading>
```

---

## 📊 Statistics Explained

### **Dashboard Stats:**
| Field | Description |
|-------|-------------|
| `totalSprints` | All sprints ever created |
| `activeSprints` | Sprints with ACTIVE status |
| `completedSprints` | Sprints with COMPLETED status |
| `totalStories` | All stories across all sprints |
| `doneStories` | Stories with DONE status |
| `totalTasks` | All tasks in all stories |
| `totalStoryPoints` | Sum of all story points |
| `averageVelocity` | Average points completed per sprint |
| `completionRate` | % of stories completed |

### **Sprint Stats:**
| Field | Description |
|-------|-------------|
| `completionPercentage` | (completed points / total points) * 100 |
| `daysRemaining` | Days until sprint end date |
| `daysElapsed` | Days since sprint start date |
| `teamMembersCount` | Unique assignees in sprint |

### **User Stats:**
| Field | Description |
|-------|-------------|
| `assignedStories` | Stories where user is assignedTo |
| `assignedTasks` | Tasks where user is assignedTo |
| `taskCompletionRate` | (completed tasks / assigned tasks) * 100 |
| `averageTaskCompletionHours` | Average of actualHours for completed tasks |
| `activeTasksCount` | Tasks currently IN_PROGRESS |

---

## 🎯 Testing the APIs

### **Test 1: Dashboard Stats**
```bash
curl http://localhost:8080/api/stats/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Test 2: Sprint Stats**
```bash
# First create a sprint, then:
curl http://localhost:8080/api/stats/sprint/SPRINT_ID \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Test 3: User Stats**
```bash
# Get stats for current user
curl http://localhost:8080/api/stats/user/USER_ID \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Test 4: Quick Counts**
```bash
curl http://localhost:8080/api/stats/counts \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Test 5: Specific Count**
```bash
curl http://localhost:8080/api/stats/count/sprints \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🎨 Frontend Integration

### **React Hook Example:**
```javascript
import { useState, useEffect } from 'react';
import axios from 'axios';

function useDashboardStats() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    const fetchStats = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('/api/stats/dashboard', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setStats(response.data);
      } catch (error) {
        console.error('Error fetching stats:', error);
      } finally {
        setLoading(false);
      }
    };
    
    fetchStats();
  }, []);
  
  return { stats, loading };
}

// Usage in component:
function Dashboard() {
  const { stats, loading } = useDashboardStats();
  
  if (loading) return <Loading />;
  
  return (
    <div>
      <StatCard title="Total Sprints" value={stats.totalSprints} />
      <StatCard title="Active Sprints" value={stats.activeSprints} />
      <StatCard title="Velocity" value={stats.averageVelocity} />
    </div>
  );
}
```

---

## 📈 Performance Considerations

### **Caching:**
```java
// Add caching to StatsService (optional)
@Cacheable("dashboard-stats")
public DashboardStatsResponse getDashboardStats() {
  // ...
}
```

### **When to Use Each Endpoint:**

| Endpoint | Use Case | Frequency |
|----------|----------|-----------|
| `/api/stats/dashboard` | Dashboard page load | Once per page load |
| `/api/stats/sprint/{id}` | Sprint detail page | Once per sprint view |
| `/api/stats/user/{id}` | User profile page | Once per profile view |
| `/api/stats/counts` | Quick badges/counters | Multiple times |
| `/api/stats/count/{type}` | Single counter badge | Real-time updates |

**Recommendation:** Use `/api/stats/counts` for real-time updates (it's faster).

---

## 🔧 Setup Instructions

### **Step 1: Add Files**
```
src/main/java/com/sprintai/
├── dto/
│   ├── DashboardStatsResponse.java     ← NEW
│   ├── SprintStatsResponse.java        ← NEW
│   └── UserStatsResponse.java          ← NEW
├── service/
│   └── StatsService.java               ← NEW
└── controller/
    └── StatsController.java            ← NEW
```

### **Step 2: Build & Run**
```bash
./gradlew clean build
./gradlew bootRun
```

### **Step 3: Test**
```bash
# Test dashboard stats
curl http://localhost:8080/api/stats/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN"

# Should return comprehensive statistics
```

---

## 🎯 Real-World Examples

### **Example 1: Sprint Burndown Chart**
```javascript
// Fetch sprint stats daily
const sprintStats = await fetch(`/api/stats/sprint/${sprintId}`);

// Use for burndown chart
const burndownData = {
  totalPoints: sprintStats.totalStoryPoints,
  completed: sprintStats.completedStoryPoints,
  remaining: sprintStats.remainingStoryPoints,
  daysElapsed: sprintStats.daysElapsed,
  daysRemaining: sprintStats.daysRemaining
};
```

### **Example 2: Team Leaderboard**
```javascript
// Fetch stats for all team members
const team = await fetch('/api/users');
const leaderboard = await Promise.all(
  team.map(user => fetch(`/api/stats/user/${user.id}`))
);

// Sort by task completion rate
leaderboard.sort((a, b) => b.taskCompletionRate - a.taskCompletionRate);
```

### **Example 3: Sprint Progress Widget**
```javascript
const sprintStats = await fetch(`/api/stats/sprint/current`);

<Widget>
  <Progress value={sprintStats.completionPercentage} />
  <Text>{sprintStats.daysRemaining} days remaining</Text>
  <Text>{sprintStats.blockedStories} blocked stories</Text>
</Widget>
```

---

## ✅ Success Checklist

- [ ] All 5 files added to project
- [ ] Application builds without errors
- [ ] `/api/stats/dashboard` returns data
- [ ] `/api/stats/sprint/{id}` returns sprint stats
- [ ] `/api/stats/user/{id}` returns user stats
- [ ] `/api/stats/counts` returns quick counts
- [ ] `/api/stats/count/{type}` works for all types
- [ ] Dashboard UI uses these APIs
- [ ] Stats update correctly when data changes

---

## 🎉 You Now Have:

- ✅ Complete dashboard statistics
- ✅ Sprint-specific metrics
- ✅ User performance stats
- ✅ Quick count APIs
- ✅ Velocity tracking
- ✅ Completion rates
- ✅ Progress percentages
- ✅ Team analytics

**Perfect for data-driven sprint management!** 📊🚀
