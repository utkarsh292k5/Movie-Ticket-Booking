-- Initial Setup Script for Movie Ticket Booking System

-- Create Admin User (password: admin123)
-- Note: You need to generate BCrypt hash for 'admin123'
-- Use online tool or Spring Security's BCryptPasswordEncoder
INSERT INTO users (email, password, name, phone_number, active, created_at, updated_at) 
VALUES ('admin@moviebox.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Admin User', '1234567890', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Assign ADMIN and USER roles
INSERT INTO user_roles (user_id, role) 
VALUES 
((SELECT id FROM users WHERE email = 'admin@moviebox.com'), 'ADMIN'),
((SELECT id FROM users WHERE email = 'admin@moviebox.com'), 'USER');

-- Insert Sample Movies
INSERT INTO movies (title, description, poster_url, language, genre, duration, rating, release_date, status, active, created_at, updated_at)
VALUES 
('The Matrix Resurrections', 'Return to a world of two realities: one, everyday life; the other, what lies behind it.', 'https://image.tmdb.org/t/p/w500/8c4a8kE7PizaGQQnditMmI1xbRp.jpg', 'English', 'Sci-Fi', 148, 'R', '2024-01-15', 'NOW_SHOWING', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dune: Part Two', 'Paul Atreides unites with Chani and the Fremen while seeking revenge.', 'https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg', 'English', 'Sci-Fi', 166, 'PG-13', '2024-03-01', 'NOW_SHOWING', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Inception', 'A thief who steals corporate secrets through dream-sharing technology.', 'https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg', 'English', 'Thriller', 148, 'PG-13', '2023-12-20', 'NOW_SHOWING', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Avatar 3', 'The continuation of the Avatar saga.', 'https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg', 'English', 'Sci-Fi', 180, 'PG-13', '2024-12-20', 'UPCOMING', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Sample Theatres
INSERT INTO theatres (name, city, address, zip_code, phone_number, active, created_at, updated_at)
VALUES 
('Cineplex Downtown', 'New York', '123 Broadway, Manhattan', '10001', '212-555-0100', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cineplex Uptown', 'New York', '456 Park Avenue', '10022', '212-555-0200', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Megaplex Central', 'Los Angeles', '789 Hollywood Blvd', '90028', '323-555-0100', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IMAX Theatre', 'San Francisco', '321 Market Street', '94102', '415-555-0100', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Screens for Theatres
-- Theatre 1: Cineplex Downtown
INSERT INTO screens (theatre_id, name, total_seats, active, created_at, updated_at)
VALUES 
((SELECT id FROM theatres WHERE name = 'Cineplex Downtown'), 'Screen 1', 100, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
((SELECT id FROM theatres WHERE name = 'Cineplex Downtown'), 'Screen 2', 80, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Theatre 2: Cineplex Uptown
INSERT INTO screens (theatre_id, name, total_seats, active, created_at, updated_at)
VALUES 
((SELECT id FROM theatres WHERE name = 'Cineplex Uptown'), 'Screen 1', 120, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Theatre 3: Megaplex Central
INSERT INTO screens (theatre_id, name, total_seats, active, created_at, updated_at)
VALUES 
((SELECT id FROM theatres WHERE name = 'Megaplex Central'), 'Screen 1', 150, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Seats for Screen 1 (Cineplex Downtown)
-- A simple 10x10 grid (rows A-J, seats 1-10)
DO $$
DECLARE
    screen_id_var BIGINT;
    row_char CHAR(1);
    seat_num INT;
    seat_type_var VARCHAR(20);
BEGIN
    SELECT id INTO screen_id_var FROM screens WHERE name = 'Screen 1' AND theatre_id = (SELECT id FROM theatres WHERE name = 'Cineplex Downtown');
    
    FOR i IN 0..9 LOOP
        row_char := CHR(65 + i); -- A to J
        FOR seat_num IN 1..10 LOOP
            -- VIP for first 2 rows, PREMIUM for middle rows, REGULAR for back
            IF i < 2 THEN
                seat_type_var := 'VIP';
            ELSIF i < 5 THEN
                seat_type_var := 'PREMIUM';
            ELSE
                seat_type_var := 'REGULAR';
            END IF;
            
            INSERT INTO seats (screen_id, row_label, seat_number, seat_type, active, created_at)
            VALUES (screen_id_var, row_char, seat_num, seat_type_var, true, CURRENT_TIMESTAMP);
        END LOOP;
    END LOOP;
END $$;

-- Insert Shows
-- Show 1: The Matrix Resurrections at Cineplex Downtown
INSERT INTO shows (movie_id, screen_id, show_date, show_time, regular_price, premium_price, vip_price, available_seats, status, active, created_at, updated_at)
VALUES 
(
    (SELECT id FROM movies WHERE title = 'The Matrix Resurrections'),
    (SELECT id FROM screens WHERE name = 'Screen 1' AND theatre_id = (SELECT id FROM theatres WHERE name = 'Cineplex Downtown')),
    CURRENT_DATE,
    '14:00:00',
    10.00,
    15.00,
    20.00,
    100,
    'SCHEDULED',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Show 2: The Matrix Resurrections evening show
INSERT INTO shows (movie_id, screen_id, show_date, show_time, regular_price, premium_price, vip_price, available_seats, status, active, created_at, updated_at)
VALUES 
(
    (SELECT id FROM movies WHERE title = 'The Matrix Resurrections'),
    (SELECT id FROM screens WHERE name = 'Screen 1' AND theatre_id = (SELECT id FROM theatres WHERE name = 'Cineplex Downtown')),
    CURRENT_DATE,
    '19:00:00',
    12.00,
    18.00,
    25.00,
    100,
    'SCHEDULED',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Show 3: Dune Part Two
INSERT INTO shows (movie_id, screen_id, show_date, show_time, regular_price, premium_price, vip_price, available_seats, status, active, created_at, updated_at)
VALUES 
(
    (SELECT id FROM movies WHERE title = 'Dune: Part Two'),
    (SELECT id FROM screens WHERE name = 'Screen 2' AND theatre_id = (SELECT id FROM theatres WHERE name = 'Cineplex Downtown')),
    CURRENT_DATE,
    '15:30:00',
    11.00,
    16.00,
    22.00,
    80,
    'SCHEDULED',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Show 4: Inception
INSERT INTO shows (movie_id, screen_id, show_date, show_time, regular_price, premium_price, vip_price, available_seats, status, active, created_at, updated_at)
VALUES 
(
    (SELECT id FROM movies WHERE title = 'Inception'),
    (SELECT id FROM screens WHERE name = 'Screen 1' AND theatre_id = (SELECT id FROM theatres WHERE name = 'Cineplex Uptown')),
    CURRENT_DATE,
    '18:00:00',
    10.00,
    15.00,
    20.00,
    120,
    'SCHEDULED',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Create a regular test user (password: user123)
INSERT INTO users (email, password, name, phone_number, active, created_at, updated_at) 
VALUES ('user@test.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Test User', '9876543210', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_roles (user_id, role) 
VALUES ((SELECT id FROM users WHERE email = 'user@test.com'), 'USER');

COMMIT;

-- Verification Queries
SELECT 'Movies:' as check_item, COUNT(*) as count FROM movies;
SELECT 'Theatres:' as check_item, COUNT(*) as count FROM theatres;
SELECT 'Screens:' as check_item, COUNT(*) as count FROM screens;
SELECT 'Seats:' as check_item, COUNT(*) as count FROM seats;
SELECT 'Shows:' as check_item, COUNT(*) as count FROM shows;
SELECT 'Users:' as check_item, COUNT(*) as count FROM users;

