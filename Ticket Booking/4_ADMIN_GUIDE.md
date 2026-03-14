# 👨‍💼 Admin Guide - Complete Management Manual

Step-by-step guide for administrators to manage the Movie Ticket Booking System.

---

## 📋 Table of Contents

1. [Getting Admin Access](#getting-admin-access)
2. [Admin Dashboard Overview](#admin-dashboard-overview)
3. [Movie Management](#movie-management)
4. [Theatre Management](#theatre-management)
5. [Screen Management](#screen-management)
6. [Seat Layout Design](#seat-layout-design)
7. [Show Management](#show-management)
8. [Booking Management](#booking-management)
9. [Reports & Analytics](#reports--analytics)
10. [Best Practices](#best-practices)

---

## 1. Getting Admin Access

### Method 1: Database Update (Existing User)

**Step 1: Sign up normally**
1. Go to http://localhost:3000/signup
2. Create account with your email
3. Complete signup

**Step 2: Access database**

**For MySQL:**
```bash
mysql -u ticketuser -pticketpass123 ticket_booking
```

**For H2:**
- Open: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/ticket_booking`
- Username: `sa`
- Password: (empty)

**Step 3: Make yourself admin**
```sql
-- Check your user ID
SELECT id, email FROM users WHERE email = 'your@email.com';

-- Add ADMIN role (keep USER role too)
INSERT INTO user_roles (user_id, role) 
VALUES (<your_user_id>, 'ADMIN');

-- Verify
SELECT u.email, ur.role FROM users u JOIN user_roles ur ON u.id = ur.user_id WHERE u.email = 'utkarsh@gemail.com';
```

**Step 4: Logout and login again**

---

### Method 2: Direct Insert (New User)

**Run in database:**

```sql
-- Create admin user
INSERT INTO users (id, name, email, password, phone_number, active, created_at, updated_at) 
VALUES (
  999,
  'Admin User',
  'admin@moviebox.com',
  '$2a$10$X1.5K3yJEQKzJz2Z3xQ9gOZEr8fQq7vZ9KxN2YwF6dM5h8L3P2qLO',
  '1234567890',
  true,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- Add both roles
INSERT INTO user_roles (user_id, role) VALUES (999, 'USER');
INSERT INTO user_roles (user_id, role) VALUES (999, 'ADMIN');
```

**Login with:**
- Email: `admin@moviebox.com`
- Password: `admin123`

---

## 2. Admin Dashboard Overview

**Access:** http://localhost:3000/admin/dashboard

### Dashboard Sections

**Statistics Cards:**
- 🎬 **Total Movies** - Count of all movies
- 🏢 **Total Theatres** - Count of all theatres
- 🎟️ **Total Bookings** - Count of all bookings
- 💰 **Total Revenue** - Sum of all confirmed bookings

**Quick Actions:**
- Manage Movies
- Manage Theatres
- Manage Shows

---

## 3. Movie Management

### 3.1 View All Movies

**Navigation:** Admin Dashboard → Manage Movies

**You'll see:**
- List of all movies
- Search bar
- Filter options
- Add New Movie button

---

### 3.2 Add New Movie

**Step 1: Click "Add New Movie" button**

**Step 2: Fill in movie details**

**Required Fields:**
- **Title:** Movie name (e.g., "Inception")
- **Description:** Brief synopsis (min 10 characters)
- **Genre:** Select from dropdown
  - Action
  - Comedy
  - Drama
  - Horror
  - Sci-Fi
  - Romance
  - Thriller
- **Duration:** Length in minutes (e.g., 148)
- **Release Date:** Select from calendar
- **Rating:** Select from dropdown
  - G (General Audiences)
  - PG (Parental Guidance)
  - PG-13 (Parents Strongly Cautioned)
  - R (Restricted)
  - NC-17 (Adults Only)
- **Status:** Select from dropdown
  - NOW_SHOWING (appears in "Now Showing")
  - UPCOMING (appears in "Coming Soon")
  - ENDED (hidden from customers)

**Optional Fields:**
- **Poster URL:** Link to poster image
  - Example: `https://image.tmdb.org/t/p/w500/poster.jpg`
  - Or use placeholder: `https://via.placeholder.com/300x450?text=Movie+Poster`
- **Trailer URL:** YouTube or video link
  - Example: `https://www.youtube.com/watch?v=YoHD9XEInc0`

**Step 3: Click "Create Movie"**

**Result:** Movie added and appears in list

---

### 3.3 Edit Movie

**Step 1:** Find movie in list

**Step 2:** Click **Edit** button (pencil icon)

**Step 3:** Update any fields

**Step 4:** Click "Update Movie"

---

### 3.4 Delete Movie

**Step 1:** Find movie in list

**Step 2:** Click **Delete** button (trash icon)

**Step 3:** Confirm deletion

⚠️ **Warning:** Deletes associated shows and bookings!

---

### 3.5 Change Movie Status

**Use Cases:**
- Movie released → Change to NOW_SHOWING
- Movie ended → Change to ENDED
- New movie announced → Set as UPCOMING

---

## 4. Theatre Management

### 4.1 Add New Theatre

**Step 1: Click "Add New Theatre"**

**Step 2: Fill in details**

**Required Fields:**
- **Name:** Theatre name
  - Example: "PVR Cinemas - Phoenix Mall"
  - Best practice: Include location in name
- **City:** City name
  - Example: "Mumbai", "Delhi", "Bangalore"
  - ⚠️ Use consistent spelling
- **Address:** Full address
  - Example: "Phoenix Market City, Kurla West, Mumbai 400070"
- **Total Screens:** Number of screens
  - Example: 5

**Step 3: Click "Create Theatre"**

---

### 4.2 Edit Theatre

**Step 1:** Find theatre in list

**Step 2:** Click **Edit** button

**Step 3:** Update fields

**Step 4:** Click "Update Theatre"

---

### 4.3 Delete Theatre

**Step 1:** Find theatre in list

**Step 2:** Click **Delete** button

**Step 3:** Confirm deletion

⚠️ **Warning:** Cannot delete if:
- Theatre has screens with shows
- Active bookings exist

---

## 5. Screen Management

### 5.1 Add Screen to Theatre

**Step 1:** View theatre details

**Step 2:** Click "Add Screen"

**Step 3:** Fill in details**

**Required Fields:**
- **Screen Name:** Identifier
  - Examples: "Screen 1", "IMAX Screen", "4DX Hall", "Gold Class"
- **Total Seats:** Number of seats
  - Example: 150
  - Note: Must match actual seat layout

**Step 4:** Click "Create Screen"

---

### 5.2 Screen Naming Best Practices

**Good Names:**
- "Screen 1", "Screen 2" (simple numbering)
- "IMAX Screen" (special format)
- "4DX Hall" (special experience)
- "Gold Class" (premium category)

**Avoid:**
- Just numbers (1, 2, 3)
- Too long names
- Special characters

---

## 6. Seat Layout Design

### 6.1 Creating Seat Layout

**After creating a screen, you need to define seats:**

**Step 1:** Click "Design Seat Layout"

**Step 2:** Choose layout method

---

### 6.2 Method A: Auto-Generate (Recommended)

**Step 1:** Specify parameters
- **Number of Rows:** (e.g., 10 for rows A-J)
- **Seats Per Row:** (e.g., 15)

**Step 2:** Click "Generate Layout"

**Result:** Seats created automatically
- Row A: A1, A2, A3, ..., A15
- Row B: B1, B2, B3, ..., B15
- ...
- Row J: J1, J2, J3, ..., J15

Total: 150 seats

---

### 6.3 Method B: Manual Entry

**For custom layouts:**

1. Define seat rows (A, B, C, etc.)
2. Specify seat numbers per row
3. Assign seat types:
   - **REGULAR** - Standard seats
   - **PREMIUM** - Better location/comfort
   - **RECLINER** - Luxury recliner seats

---

### 6.4 Seat Type Pricing

**Set different prices per seat type:**
- Regular: ₹200
- Premium: ₹300
- Recliner: ₹500

**Configure when creating show (next section)**

---

## 7. Show Management

### 7.1 Create New Show

**Prerequisites:**
- ✅ Movie added
- ✅ Theatre created
- ✅ Screen created
- ✅ Seat layout defined

**Step 1: Click "Add New Show"**

**Step 2: Fill in show details**

**Required Fields:**

**Movie & Location:**
- **Movie:** Select from dropdown
- **Theatre:** Select theatre
- **Screen:** Select screen

**Timing:**
- **Show Date:** Select date (must be today or future)
- **Show Time:** Select time
  - Common times: 9:00 AM, 12:00 PM, 3:00 PM, 6:00 PM, 9:00 PM

**Pricing:**
- **Base Price:** For REGULAR seats (e.g., ₹200)
- **Premium Price:** For PREMIUM seats (e.g., ₹300)
- **Recliner Price:** For RECLINER seats (e.g., ₹500)

**Step 3: Click "Create Show"**

**Result:** Show is live! Customers can book tickets.

---

### 7.2 Show Scheduling Best Practices

**Timing:**
- Allow 30 min gap between shows for cleaning
- Popular times: 12 PM, 3 PM, 6 PM, 9 PM
- Weekend: Add more shows
- Weekday: Fewer shows

**Pricing:**
- Weekend: Higher prices
- Morning shows: Lower prices
- Evening/Night: Premium prices
- IMAX/4DX: 30-50% higher

**Example Schedule:**
```
Screen 1 - Inception
- 9:00 AM  - ₹150 (Morning discount)
- 12:00 PM - ₹200 (Regular)
- 3:00 PM  - ₹200 (Regular)
- 6:00 PM  - ₹250 (Prime time)
- 9:00 PM  - ₹250 (Prime time)
```

---

### 7.3 Edit Show

**Step 1:** Find show in list

**Step 2:** Click **Edit** button

**Step 3:** Update details
- ⚠️ Cannot change if bookings exist
- Can update pricing

**Step 4:** Click "Update Show"

---

### 7.4 Cancel Show

**Step 1:** Find show in list

**Step 2:** Click **Delete/Cancel** button

**Step 3:** Confirm cancellation

**What Happens:**
- Show deleted
- All bookings cancelled
- Customers notified (in production)
- Refunds processed (in production)

⚠️ **Warning:** Only cancel if absolutely necessary!

---

## 8. Booking Management

### 8.1 View All Bookings

**Navigation:** Admin Dashboard → Bookings (or via shows)

**You'll see:**
- Booking ID
- Customer name & email
- Movie & show details
- Seats booked
- Amount paid
- Status (Confirmed/Cancelled)
- Booking date

---

### 8.2 View Show-Specific Bookings

**Step 1:** Go to specific show

**Step 2:** Click "View Bookings"

**You'll see:**
- All bookings for that show
- Seat map with booked seats highlighted
- Customer details

---

### 8.3 Force Cancel Booking (Emergency)

**Use Case:** Customer issues, technical problems

**Step 1:** Find booking

**Step 2:** Click "Cancel Booking"

**Step 3:** Provide reason (optional)

**Step 4:** Confirm cancellation

**Result:**
- Booking status → CANCELLED
- Seats released
- Customer notified

---

## 9. Reports & Analytics

### 9.1 Available Reports

**Revenue Reports:**
- Total revenue
- Revenue by movie
- Revenue by theatre
- Revenue by date range

**Booking Reports:**
- Total bookings
- Bookings by status
- Bookings by movie
- Occupancy rate

**Movie Performance:**
- Most booked movies
- Lowest performing movies
- Genre analysis

---

### 9.2 Generate Reports (Database Queries)

**Total Revenue:**
```sql
SELECT SUM(total_amount) as total_revenue
FROM bookings 
WHERE status = 'CONFIRMED';
```

**Revenue by Movie:**
```sql
SELECT m.title, SUM(b.total_amount) as revenue, COUNT(b.id) as bookings
FROM bookings b
JOIN shows s ON b.show_id = s.id
JOIN movies m ON s.movie_id = m.id
WHERE b.status = 'CONFIRMED'
GROUP BY m.id
ORDER BY revenue DESC;
```

**Occupancy Rate:**
```sql
SELECT 
  (COUNT(DISTINCT bs.seat_id) * 100.0 / 
   (SELECT COUNT(*) FROM seats WHERE screen_id = s.screen_id)) as occupancy_rate
FROM booking_seats bs
JOIN bookings b ON bs.booking_id = b.id
JOIN shows s ON bs.show_id = s.id
WHERE b.status = 'CONFIRMED' AND s.id = <show_id>;
```

---

## 10. Best Practices

### 10.1 Content Management

**Movies:**
- Use high-quality poster images
- Write engaging descriptions
- Update status promptly
- Remove ended movies regularly

**Theatres:**
- Consistent naming convention
- Accurate addresses
- Update contact information

---

### 10.2 Show Scheduling

**Avoid:**
- Overlapping shows on same screen
- Shows starting in the past
- Too many shows per day

**Do:**
- Plan 1-2 weeks ahead
- Monitor booking trends
- Adjust based on demand
- Add shows for popular movies

---

### 10.3 Pricing Strategy

**Dynamic Pricing:**
- Weekdays: Lower prices
- Weekends: Higher prices
- Morning: Discount prices
- Prime time (6-9 PM): Premium prices

**Seat-Based Pricing:**
- Front rows: Lowest
- Middle rows: Standard
- Back rows: Premium
- Recliners: Highest

---

### 10.4 Customer Service

**Handling Issues:**
- Respond to booking problems quickly
- Cancel shows early if needed
- Clear communication
- Fair refund policy

---

## 📊 Complete Workflow Example

### Setting Up a New Movie

**Day 1: Add Movie**
```
1. Admin Dashboard
2. Manage Movies → Add New Movie
3. Fill details:
   - Title: "Avengers: Endgame"
   - Genre: Action
   - Duration: 181 minutes
   - Status: UPCOMING
4. Save
```

**Day 7: Movie Released - Add Shows**
```
1. Change movie status to NOW_SHOWING
2. Manage Shows → Add New Show
3. Create shows:
   - PVR Phoenix Mall, Screen 1, 12:00 PM, ₹200
   - PVR Phoenix Mall, Screen 1, 3:00 PM, ₹200
   - PVR Phoenix Mall, Screen 1, 6:00 PM, ₹250
   - PVR Phoenix Mall, Screen 1, 9:00 PM, ₹250
4. Repeat for multiple theatres
```

**Week 2-4: Monitor & Adjust**
```
1. Check booking rates
2. Add more shows if popular
3. Reduce shows if low demand
4. Adjust pricing
```

**Week 5: Movie Ending**
```
1. Reduce number of shows
2. After last show:
   - Change status to ENDED
   - Movie hidden from customers
```

---

## ✅ Admin Checklist

### Daily Tasks
- [ ] Check new bookings
- [ ] Monitor revenue
- [ ] Respond to issues

### Weekly Tasks
- [ ] Add upcoming movies
- [ ] Schedule next week's shows
- [ ] Review performance
- [ ] Update pricing

### Monthly Tasks
- [ ] Generate reports
- [ ] Remove old movies
- [ ] Clean up cancelled bookings
- [ ] Backup database

---

## 🆘 Troubleshooting

**Problem:** Can't create show

**Solutions:**
- Check movie exists
- Check theatre/screen exists
- Verify seat layout created
- Ensure date is in future

---

**Problem:** Show deletion fails

**Solution:**
- Active bookings exist
- Cancel all bookings first
- Or wait until show completes

---

**Problem:** Revenue not showing

**Solution:**
- Check booking status (must be CONFIRMED)
- Verify payment records
- Run database query manually

---

**For more help, contact technical support or check database directly.**

