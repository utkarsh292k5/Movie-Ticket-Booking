# 🚀 Quick Start Guide

## ⚡ First Time Setup (Run Once)

### 1. Install Dependencies

**Backend:**
```bash
cd backend
mvn clean install -DskipTests
```

**Frontend:**
```bash
cd frontend
npm install
```

---

## 🎬 Running the Application

### Start Backend (Terminal 1)

**With MySQL:**
```bash
cd backend
export JAVA_HOME="/Users/ayushtiwari/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home"
mvn spring-boot:run
```

**With H2 (lightweight):**
```bash
cd backend
export JAVA_HOME="/Users/ayushtiwari/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home"
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

### Start Frontend (Terminal 2)

```bash
cd frontend
npm run dev
```

---

## 🌐 Access URLs

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **H2 Console:** http://localhost:8080/h2-console (if using H2)

---

## 🛑 Stopping the Application

Press `Ctrl+C` in both terminal windows.

---

## 📦 Making It Lightweight (Optional)

Remove heavy folders to save space:

```bash
# Remove frontend dependencies (233MB)
rm -rf frontend/node_modules

# Remove backend build (59MB)
rm -rf backend/target
```

**Note:** Run the install commands again before starting the app next time.

---

## 🔧 Quick Troubleshooting

**Port already in use:**
```bash
# Backend (8080)
lsof -ti:8080 | xargs kill -9

# Frontend (3000)
lsof -ti:3000 | xargs kill -9
```

**Fresh install:**
```bash
# Frontend
cd frontend
rm -rf node_modules package-lock.json
npm install

# Backend
cd backend
mvn clean install -DskipTests
```

---

## 📚 Need More Help?

- **Complete Setup:** See `1_SETUP_FROM_SCRATCH.md`
- **Backend Info:** See `2_BACKEND_GUIDE.md`
- **Frontend Info:** See `3_FRONTEND_GUIDE.md`
- **Admin Guide:** See `4_ADMIN_GUIDE.md`
- **User Guide:** See `5_USER_GUIDE.md`

