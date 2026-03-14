# 🚀 Complete Setup Guide - From Scratch to Running

This guide will take you from a fresh machine to a fully running Movie Ticket Booking System.

---

## 📋 Table of Contents

1. [Prerequisites Check](#prerequisites-check)
2. [Installing Required Tools](#installing-required-tools)
3. [Database Setup](#database-setup)
4. [Getting the Application](#getting-the-application)
5. [Running the Application](#running-the-application)
6. [Verification](#verification)
7. [Troubleshooting](#troubleshooting)

---

## 1. Prerequisites Check

### System Requirements

- **Operating System:** macOS, Linux, or Windows
- **RAM:** 4GB minimum (8GB recommended)
- **Disk Space:** 2GB free
- **Internet Connection:** Required for downloads

---

## 2. Installing Required Tools

### 2.1 Java 17 (Required)

**Check if Java is installed:**

```bash
java -version
```

**Expected output:** `java version "17.x.x"` or higher

**If not installed:**

**On macOS:**
```bash
brew install openjdk@17

# Set JAVA_HOME
echo 'export JAVA_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"' >> ~/.zshrc
source ~/.zshrc
```

**On Linux:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Verify:**
```bash
java -version
```

---

### 2.2 Maven (Required for Backend)

**Check if Maven is installed:**

```bash
mvn -version
```

**Expected output:** `Apache Maven 3.x.x`

**If not installed:**

**On macOS:**
```bash
brew install maven
```

**On Linux:**
```bash
sudo apt install maven
```

**Verify:**
```bash
mvn -version
```

---

### 2.3 Node.js & npm (Required for Frontend)

**Check if Node.js is installed:**

```bash
node -v
npm -v
```

**Expected output:** Node `v18.x` or higher, npm `9.x` or higher

**If not installed:**

**On macOS:**
```bash
brew install node
```

**On Linux:**
```bash
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

**Verify:**
```bash
node -v
npm -v
```

---

### 2.4 Redis (Required for Seat Locking)

**Check if Redis is installed:**

```bash
redis-cli --version
```

**If not installed:**

**On macOS:**
```bash
brew install redis
brew services start redis
```

**On Linux:**
```bash
sudo apt update
sudo apt install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server
```

**Verify Redis is running:**
```bash
redis-cli ping
```

**Expected output:** `PONG`

---

### 2.5 Database (Choose One)

#### Option A: MySQL (Recommended for Production)

**Check if MySQL is installed:**

```bash
mysql --version
```

**Install MySQL:**

**On macOS:**
```bash
brew install mysql@8.0
brew services start mysql@8.0
```

**On Linux:**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

**Secure MySQL installation:**
```bash
sudo mysql_secure_installation
```

---

#### Option B: H2 Database (Easiest - No Installation Needed)

H2 is included in the project. No installation required!

---

## 3. Database Setup

### Option A: MySQL Setup

**Step 1: Connect to MySQL**

```bash
# Try without password first
mysql -u root

# If that fails, try with password
mysql -u root -p

# If still fails, try with sudo
sudo mysql -u root
```

**Step 2: Create Database and User**

Once in MySQL prompt (`mysql>`), run:

```sql
CREATE DATABASE IF NOT EXISTS ticket_booking 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'ticketuser'@'localhost' IDENTIFIED BY 'ticketpass123';

GRANT ALL PRIVILEGES ON ticket_booking.* TO 'ticketuser'@'localhost';

FLUSH PRIVILEGES;

SHOW DATABASES;

EXIT;
```

**Step 3: Verify Connection**

```bash
mysql -u ticketuser -pticketpass123 ticket_booking -e "SELECT DATABASE();"
```

You should see:
```
+----------------+
| DATABASE()     |
+----------------+
| ticket_booking |
+----------------+
```

---

### Option B: H2 Setup (File-Based)

**No setup needed!** The application will create the database automatically.

The database file will be created at: `backend/data/ticket_booking.mv.db`

---

## 4. Getting the Application

### Option 1: If You Have the Source Code

```bash
cd "/path/to/Ticket Booking"
```

### Option 2: Clone from Repository (if applicable)

```bash
git clone <repository-url>
cd ticket-booking
```

---

## 5. Running the Application

### 5.1 Install Dependencies First

**⚠️ IMPORTANT: Run these commands first time or after fresh clone**

#### Install Backend Dependencies

**Navigate to backend directory:**

```bash
cd backend
```

**Download Maven dependencies:**

```bash
mvn clean install -DskipTests
```

This will:
- Download all Java libraries (~200MB)
- Compile the project
- Create `target/` folder

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX s
```

---

#### Install Frontend Dependencies

**Navigate to frontend directory:**

```bash
cd frontend
```

**Download npm packages:**

```bash
npm install
```

This will:
- Download all Node.js packages (~233MB)
- Create `node_modules/` folder
- Generate `package-lock.json`

**Expected output:**
```
added XXX packages in XXs
```

---

### 5.2 Start Backend

**Navigate to backend directory:**

```bash
cd backend
```

**For MySQL:**
```bash
export JAVA_HOME="/path/to/java17"
mvn spring-boot:run
```

**For H2:**
```bash
export JAVA_HOME="/path/to/java17"
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

**Wait for:**
```
Started MovieTicketBookingApplication in X.XXX seconds
```

**Backend will be running on:** http://localhost:8080

---

### 5.3 Start Frontend (New Terminal)

**Open a NEW terminal window**

**Navigate to frontend directory:**

```bash
cd frontend
```

**Start development server:**

```bash
npm run dev
```

**Wait for:**
```
VITE v5.x.x  ready in XXX ms
➜  Local:   http://localhost:3000/
```

**Frontend will be running on:** http://localhost:3000

---

## 6. Verification

### 6.1 Check All Services

**Backend API:**
```bash
curl http://localhost:8080/api/movies
```

Expected: `[]` (empty array)

**Frontend:**
```bash
curl http://localhost:3000
```

Expected: HTML page

**Redis:**
```bash
redis-cli ping
```

Expected: `PONG`

**Database (MySQL):**
```bash
mysql -u ticketuser -pticketpass123 ticket_booking -e "SHOW TABLES;"
```

Expected: List of tables

**Database (H2):**
Open: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/ticket_booking`
- Username: `sa`
- Password: (empty)

---

### 6.2 Access the Application

**Open browser and go to:**

```
http://localhost:3000
```

You should see the Movie Ticket Booking home page!

---

## 7. Troubleshooting

### Java Issues

**Error: "java: command not found"**

Solution:
```bash
# Find Java installation
/usr/libexec/java_home -V

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

---

### Maven Issues

**Error: "mvn: command not found"**

Solution:
```bash
brew install maven
```

---

### Port Already in Use

**Error: "Port 8080 is already in use"**

Solution:
```bash
# Find and kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or use different port
mvn spring-boot:run -Dserver.port=8081
```

**Error: "Port 3000 is already in use"**

Solution:
```bash
# Find and kill process on port 3000
lsof -ti:3000 | xargs kill -9
```

---

### Redis Issues

**Error: "Could not connect to Redis"**

Solution:
```bash
# Start Redis
brew services start redis

# Or on Linux
sudo systemctl start redis-server

# Verify
redis-cli ping
```

---

### MySQL Connection Issues

**Error: "Access denied for user 'root'@'localhost'"**

Solution:
```bash
# Reset MySQL root password
brew services stop mysql@8.0
sudo mysqld_safe --skip-grant-tables --skip-networking &
sleep 5
mysql -u root -e "FLUSH PRIVILEGES; ALTER USER 'root'@'localhost' IDENTIFIED BY ''; FLUSH PRIVILEGES;"
sudo killall mysqld
brew services start mysql@8.0
```

---

### Missing Dependencies Issues

**Error: "Cannot find module" or "package not found"**

**Frontend:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

**Backend:**
```bash
cd backend
rm -rf target ~/.m2/repository
mvn clean install -DskipTests
```

**Error: "npm install fails"**

Solution:
```bash
# Clear npm cache
npm cache clean --force

# Try install again
npm install

# If still fails, use legacy peer deps
npm install --legacy-peer-deps
```

**Error: "Maven build fails"**

Solution:
```bash
# Clean Maven cache
rm -rf ~/.m2/repository

# Retry with fresh download
mvn clean install -U -DskipTests
```

---

### Frontend Build Issues

**Error: "Cannot find module"**

Solution:
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

---

### Backend Build Issues

**Error: "Build failure"**

Solution:
```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run
```

---

## 🎯 Quick Start Commands Summary

### Complete Setup in One Go

```bash
# 1. Install all tools (macOS)
brew install openjdk@17 maven node redis mysql@8.0

# 2. Start services
brew services start redis
brew services start mysql@8.0

# 3. Setup database
mysql -u root << 'EOF'
CREATE DATABASE ticket_booking;
CREATE USER 'ticketuser'@'localhost' IDENTIFIED BY 'ticketpass123';
GRANT ALL PRIVILEGES ON ticket_booking.* TO 'ticketuser'@'localhost';
FLUSH PRIVILEGES;
EOF

# 4. Start backend (in one terminal)
cd backend
export JAVA_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
mvn spring-boot:run

# 5. Start frontend (in another terminal)
cd frontend
npm install
npm run dev
```

---

## 📚 Next Steps

After successful setup:

1. **Read:** `ADMIN_GUIDE.md` - Learn how to set up as admin
2. **Read:** `USER_GUIDE.md` - Learn how to use the application
3. **Read:** `BACKEND_GUIDE.md` - Understand backend architecture
4. **Read:** `FRONTEND_GUIDE.md` - Understand frontend architecture

---

## ✅ Setup Complete Checklist

- [ ] Java 17 installed and verified
- [ ] Maven installed and verified  
- [ ] Node.js & npm installed and verified
- [ ] Redis installed, running, and verified
- [ ] Database (MySQL or H2) set up
- [ ] Backend running on port 8080
- [ ] Frontend running on port 3000
- [ ] All services verified
- [ ] Application accessible in browser

---

**🎉 Congratulations! Your Movie Ticket Booking System is now running!**

For detailed guides on using the system, see:
- `ADMIN_GUIDE.md` - For administrators
- `USER_GUIDE.md` - For end users

