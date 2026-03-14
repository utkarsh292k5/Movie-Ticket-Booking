#!/bin/bash

# Complete Setup Script for Movie Ticket Booking System
echo "🎬 Movie Ticket Booking System - Complete Setup"
echo "================================================"

# Step 1: Check MySQL and get root password
echo ""
echo "Step 1: Setting up MySQL..."
echo "Checking MySQL status..."

# Try to find MySQL password or setup
if mysql -u root -e "SELECT 1" 2>/dev/null; then
    echo "✅ MySQL root has no password"
    MYSQL_PASS=""
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS ticket_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    echo "✅ Database 'ticket_booking' created"
else
    echo "⚠️  MySQL root requires password"
    echo "Please enter your MySQL root password (or press Enter if none):"
    read -s MYSQL_PASS
    
    if [ -z "$MYSQL_PASS" ]; then
        mysql -u root -e "CREATE DATABASE IF NOT EXISTS ticket_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
    else
        mysql -u root -p"$MYSQL_PASS" -e "CREATE DATABASE IF NOT EXISTS ticket_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    fi
    
    if [ $? -eq 0 ]; then
        echo "✅ Database 'ticket_booking' created"
    else
        echo "❌ Failed to create database. Please create it manually:"
        echo "   mysql -u root -p"
        echo "   CREATE DATABASE ticket_booking;"
        exit 1
    fi
fi

# Update application.properties with correct password
if [ ! -z "$MYSQL_PASS" ]; then
    sed -i '' "s/spring.datasource.password=.*/spring.datasource.password=$MYSQL_PASS/" "backend/src/main/resources/application.properties"
fi

# Step 2: Check Redis
echo ""
echo "Step 2: Checking Redis..."
if redis-cli ping > /dev/null 2>&1; then
    echo "✅ Redis is running"
else
    echo "⚠️  Starting Redis..."
    brew services start redis 2>/dev/null || redis-server --daemonize yes
    sleep 2
    if redis-cli ping > /dev/null 2>&1; then
        echo "✅ Redis started"
    else
        echo "❌ Failed to start Redis. Please start it manually:"
        echo "   redis-server"
        exit 1
    fi
fi

# Step 3: Set Java 17
echo ""
echo "Step 3: Setting up Java 17..."
export JAVA_HOME="/Users/ayushtiwari/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
echo "✅ Java 17 configured"
java -version

# Step 4: Build backend
echo ""
echo "Step 4: Building backend..."
cd backend
mvn clean install -DskipTests
if [ $? -eq 0 ]; then
    echo "✅ Backend built successfully"
else
    echo "❌ Backend build failed"
    exit 1
fi

# Step 5: Start backend in background
echo ""
echo "Step 5: Starting backend server..."
echo "This will take about 30 seconds..."
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend PID: $BACKEND_PID"

# Wait for backend to start
echo "Waiting for backend to start..."
for i in {1..30}; do
    if curl -s http://localhost:8080/api/movies > /dev/null 2>&1; then
        echo "✅ Backend started successfully on http://localhost:8080"
        break
    fi
    echo -n "."
    sleep 1
done
echo ""

# Step 6: Setup frontend
cd ../frontend
echo ""
echo "Step 6: Setting up frontend..."
if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies..."
    npm install
    if [ $? -eq 0 ]; then
        echo "✅ Frontend dependencies installed"
    else
        echo "❌ Failed to install frontend dependencies"
        exit 1
    fi
else
    echo "✅ Frontend dependencies already installed"
fi

# Step 7: Start frontend
echo ""
echo "Step 7: Starting frontend..."
echo "Frontend will start on http://localhost:3000"
npm run dev

# Cleanup on exit
trap "echo '\n\n🛑 Stopping servers...'; kill $BACKEND_PID 2>/dev/null; exit" INT TERM

