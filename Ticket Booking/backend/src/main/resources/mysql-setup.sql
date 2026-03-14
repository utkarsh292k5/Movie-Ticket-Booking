-- Quick Setup for MySQL

-- Create database
CREATE DATABASE IF NOT EXISTS ticket_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ticket_booking;

-- The tables will be auto-created by Hibernate
-- This script just adds initial test data

-- Wait for tables to be created by Spring Boot, then run:
-- Admin password: admin123
-- User password: user123
-- Both use the same BCrypt hash for demo purposes

-- To generate a new BCrypt hash for 'admin123':
-- Use: https://bcrypt-generator.com/ with rounds=10
-- Or use Spring Security BCryptPasswordEncoder in your code

