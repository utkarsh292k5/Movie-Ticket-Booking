# 🎬 Movie Ticket Booking System

A complete, production-ready movie ticket booking system with Spring Boot backend and React frontend.

---

## 📚 Documentation

**All documentation has been organized into 5 comprehensive guides:**

### 1. 📦 [SETUP_FROM_SCRATCH.md](1_SETUP_FROM_SCRATCH.md)
**For: New Users / Developers setting up on fresh machine**

Complete installation guide covering:
- Installing Java, Maven, Node.js, Redis
- Database setup (MySQL or H2)
- Running the application
- Verification steps
- Troubleshooting common issues

**Start here if you're setting up for the first time!**

---

### 2. 🔧 [BACKEND_GUIDE.md](2_BACKEND_GUIDE.md)
**For: Developers / Technical Understanding**

Backend architecture documentation:
- Technology stack
- Project structure
- Database schema & entities
- API endpoints
- Security implementation (JWT, BCrypt)
- Seat locking mechanism (Redis)
- Configuration details

**Read this to understand how the backend works!**

---

### 3. ⚛️ [FRONTEND_GUIDE.md](3_FRONTEND_GUIDE.md)
**For: Frontend Developers / UI Understanding**

Frontend architecture documentation:
- React components
- State management
- API integration (Axios)
- Routing (React Router)
- Styling (Ant Design + Tailwind)
- Page components breakdown

**Read this to understand how the frontend works!**

---

### 4. 👨‍💼 [ADMIN_GUIDE.md](4_ADMIN_GUIDE.md)
**For: System Administrators / Theatre Managers**

Complete admin manual:
- Getting admin access
- Movie management (CRUD)
- Theatre & screen management
- Seat layout design
- Show scheduling
- Booking management
- Reports & analytics
- Best practices

**Read this to manage the system as an admin!**

---

### 5. 🎫 [USER_GUIDE.md](5_USER_GUIDE.md)
**For: End Users / Customers**

User manual for booking tickets:
- Creating an account
- Browsing movies
- Complete booking process
- Seat selection
- Payment
- Managing bookings
- Cancellation policy
- FAQs

**Read this to learn how to book tickets!**

---

## 🚀 Quick Start

### ⚡ Super Quick (Already Configured)
```bash
# See QUICK_START.md for commands
```

### For First-Time Setup:
```bash
# 1. Install dependencies (first time only)
cd backend && mvn clean install -DskipTests
cd ../frontend && npm install

# 2. Start backend
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=h2

# 3. Start frontend (new terminal)
cd frontend
npm run dev

# 4. Access application
Open: http://localhost:3000
```

### 📦 Lightweight Mode

**Remove heavy modules after use:**
```bash
rm -rf frontend/node_modules backend/target
# Saves ~292MB disk space
# Run install commands again before next start
```

---

## 📖 Documentation Map

```
Need to...                          → Read This Guide
─────────────────────────────────────────────────────────
Quick commands only                 → QUICK_START.md ⚡
Install everything from scratch     → 1_SETUP_FROM_SCRATCH.md
Understand backend architecture     → 2_BACKEND_GUIDE.md
Understand frontend architecture    → 3_FRONTEND_GUIDE.md
Manage the system as admin          → 4_ADMIN_GUIDE.md
Book tickets as a customer          → 5_USER_GUIDE.md
```

---

## ✨ Key Features

### 🔐 Security
- JWT-based authentication
- Role-based authorization (USER/ADMIN)
- BCrypt password encryption

### 🎟️ Seat Booking
- Real-time seat locking (Redis, 5-min timeout)
- Prevents double booking
- Visual seat map
- Concurrent user handling

### 💳 Payment
- Multiple payment methods
- Mock payment gateway
- Transaction tracking

---

## 🛠️ Technology Stack

**Backend:** Java 17, Spring Boot, Spring Security, JPA, MySQL/H2, Redis
**Frontend:** React 18, Vite, Ant Design, Tailwind CSS, Axios

---

## 🎯 Getting Help

**Setup issues?** → `1_SETUP_FROM_SCRATCH.md` → Troubleshooting
**Admin tasks?** → `4_ADMIN_GUIDE.md`
**Booking tickets?** → `5_USER_GUIDE.md`
**Technical details?** → `2_BACKEND_GUIDE.md` + `3_FRONTEND_GUIDE.md`

---

**Happy Booking! 🍿🎬**

