# 🎬 User Guide - Booking Movie Tickets

Complete guide for customers to book movie tickets.

---

## 📋 Table of Contents

1. [Getting Started](#getting-started)
2. [Creating an Account](#creating-an-account)
3. [Browsing Movies](#browsing-movies)
4. [Booking Process](#booking-process)
5. [Managing Bookings](#managing-bookings)
6. [Cancellation Policy](#cancellation-policy)
7. [FAQs](#faqs)

---

## 1. Getting Started

### Accessing the Application

**Open your web browser and go to:**
```
http://localhost:3000
```

You'll see the home page with available movies.

---

## 2. Creating an Account

### Step 1: Click "Signup"

Located in the top-right corner of the navigation bar.

### Step 2: Fill in Your Details

**Required Information:**
- **Name:** Your full name
  - Example: "John Doe"
- **Email:** Your email address
  - Example: "john.doe@gmail.com"
  - ⚠️ Must be unique
- **Password:** Choose a strong password
  - Minimum 6 characters
  - Example: "MySecurePass123"
- **Phone:** Your phone number
  - Example: "9876543210"
  - 10 digits

### Step 3: Click "Sign Up"

You'll be automatically logged in and redirected to the home page.

---

### Logging In (Returning Users)

**Step 1:** Click "Login" in navigation bar

**Step 2:** Enter your credentials
- Email
- Password

**Step 3:** Click "Log in"

You're now logged in!

---

## 3. Browsing Movies

### Home Page

**Two tabs available:**

**Now Showing:**
- Movies currently playing in theatres
- Click on any movie card to see details

**Coming Soon:**
- Upcoming movies
- Pre-book if shows available

---

### Movie Details Page

**Click on any movie to see:**
- Large poster
- Full description
- Genre (Action, Comedy, Drama, etc.)
- Duration (e.g., 148 minutes)
- Rating (G, PG, PG-13, R, NC-17)
- Release date
- Trailer (if available)

**Action:**
- Click **"Book Tickets"** to proceed

---

## 4. Booking Process

### Step 1: Select Show

**You'll see:**
- **Filters:**
  - City: Select your city
  - Date: Choose date

**Show List:**
Each show displays:
- Theatre name & location
- Screen name
- Show time (e.g., 6:00 PM)
- Ticket price (e.g., ₹200)
- Available seats count

**Action:**
- Click **"Select Seats"** for your preferred show

---

### Step 2: Select Seats

**Seat Map:**

```
        🎬 SCREEN 🎬

Row A:  ⬜ ⬜ ⬜ ⬜ ⬜ ⬜ ⬜ ⬜
Row B:  ⬜ ⬜ 🟢 🟢 ⬜ ⬜ ⬜ ⬜
Row C:  ⬜ 🔴 🔴 ⬜ ⬜ ⬜ ⬜ ⬜
Row D:  ⬜ ⬜ ⬜ ⬜ ⬜ ⬜ ⬜ ⬜
```

**Seat Colors:**
- ⬜ **Gray (Available)** - Click to select
- 🟢 **Green (Selected)** - Your selection
- 🔴 **Red (Booked)** - Already booked
- 🟡 **Yellow (Locked)** - Temporarily locked by another user

**Instructions:**
1. Click on gray seats to select
2. Click again to deselect
3. View selected seats and total at top
4. You have 5 minutes to complete booking

**⏱️ Time Limit:**
- Your seats are locked for **5 minutes**
- Complete payment within this time
- Timer shown at top of page
- After timeout, seats are released

**Action:**
- Click **"Proceed to Payment"** when done selecting

---

### Step 3: Payment

**Booking Summary:**
- Movie name
- Theatre & screen
- Date & time
- Seat numbers
- Total amount

**Payment Methods:**
- 💳 Credit Card
- 📱 UPI
- 🏦 Net Banking
- 👛 Wallet

**Instructions:**
1. Select payment method
2. Click **"Pay Now"**
3. Payment processed (mock system)

⚠️ **Note:** This is a demo system with mock payments

---

### Step 4: Booking Confirmation

**Success! 🎉**

**You'll receive:**
- ✅ Confirmation message
- **Booking ID** (e.g., BK1234567890)
- **QR Code** (for theatre entry)
- Complete booking details
- Email confirmation (in production)

**Booking Details:**
- Movie name
- Theatre, screen, city
- Date & time
- Seat numbers
- Amount paid
- Transaction ID
- Status: CONFIRMED

**Actions Available:**
- **"View My Bookings"** - See all your bookings
- **"Back to Home"** - Browse more movies

💡 **Tip:** Screenshot or save your Booking ID!

---

## 5. Managing Bookings

### View Your Bookings

**Step 1:** Click **"My Bookings"** in navigation bar

**Step 2:** View all your bookings

**Each booking shows:**
- Movie poster & title
- Booking ID
- Theatre, screen
- Date & time
- Seat numbers
- Status badge:
  - ✅ **CONFIRMED** - Active booking
  - ❌ **CANCELLED** - Cancelled booking
- Amount paid
- Actions:
  - **"View Details"** - Full information
  - **"Cancel Booking"** - Cancel ticket (if allowed)

---

### View Booking Details

**Click "View Details" to see:**
- Complete booking information
- QR code for entry
- Transaction details
- Cancellation policy

---

## 6. Cancellation Policy

### When Can You Cancel?

✅ **Allowed:**
- Show is **more than 2 hours away**
- Booking status is CONFIRMED

❌ **Not Allowed:**
- Show is **less than 2 hours away**
- Show has already started
- Booking already cancelled

---

### How to Cancel

**Step 1:** Go to **"My Bookings"**

**Step 2:** Find the booking you want to cancel

**Step 3:** Click **"Cancel Booking"** button

**Step 4:** Confirm cancellation in popup

**Result:**
- Booking status changes to CANCELLED
- Seats released for other users
- Refund processed (in production system)

---

## 7. FAQs

### Q: Can multiple people book the same seat?

**A:** No! Our system prevents double booking using:
- Real-time seat locking (5 minutes)
- Database constraints
- Only one person can book each seat

---

### Q: What happens if I don't complete payment in 5 minutes?

**A:** 
- Your seat locks expire automatically
- Seats become available for others
- You need to start booking again

---

### Q: Can I select non-adjacent seats?

**A:** Yes! Select any available seats, anywhere in the theatre.

---

### Q: Can I modify my booking after confirmation?

**A:** No. You can only:
- Cancel (if >2 hours before show)
- Then make a new booking

---

### Q: What if someone else is selecting the same seat?

**A:** 
- First person to lock gets the seat
- You'll see it as **yellow (locked)**
- Wait 5 minutes for it to unlock, or choose different seats

---

### Q: Can I book tickets without creating an account?

**A:** No. You must sign up/login to book tickets.

---

### Q: How do I find shows in my city?

**A:** 
1. Go to show selection page
2. Use city filter dropdown
3. Select your city
4. Only shows in your city will appear

---

### Q: What payment methods are supported?

**A:**
- Credit/Debit Cards
- UPI
- Net Banking
- Wallets

*Note: In demo mode, all payments are mock/simulated*

---

### Q: Will I receive a confirmation email?

**A:** In production, yes. In demo mode, view confirmation on screen.

---

### Q: How do I enter the theatre?

**A:** 
- Show QR code (from booking confirmation)
- Or provide Booking ID
- Theatre staff will verify and let you in

---

### Q: Can I cancel after the movie has started?

**A:** No. Cancellation is not allowed once show starts or within 2 hours of show time.

---

### Q: What if I accidentally close the browser during booking?

**A:** 
- If payment not completed: Seats auto-release after 5 min
- If payment completed: Check "My Bookings"
- Your booking is saved if payment succeeded

---

### Q: Can I book tickets for someone else?

**A:** Yes! Enter their details when signing up, or book using your account.

---

### Q: Are there any booking fees?

**A:** No additional fees in this system. You pay only the ticket price.

---

### Q: Can I get a refund for cancelled bookings?

**A:** In production: Yes, full refund.
In demo: Status changes to CANCELLED.

---

## 🎯 Complete Booking Example

### Scenario: Book 2 tickets for Inception

**Step 1: Browse**
```
1. Open http://localhost:3000
2. See "Inception" in "Now Showing"
3. Click on movie card
```

**Step 2: View Details**
```
1. Read description
2. Check duration, rating
3. Click "Book Tickets"
```

**Step 3: Select Show**
```
1. Select City: Mumbai
2. Select Date: Tomorrow
3. See available shows
4. Choose: PVR Phoenix - 6:00 PM - ₹250
5. Click "Select Seats"
```

**Step 4: Select Seats**
```
1. View seat map
2. Click seat A5 (turns green)
3. Click seat A6 (turns green)
4. See: "Selected: A5, A6 | Total: ₹500"
5. Click "Proceed to Payment"
```

**Step 5: Pay**
```
1. Review summary
2. Select: Credit Card
3. Click "Pay Now"
4. Wait for confirmation
```

**Step 6: Success!**
```
1. See Booking ID: BK1234567890
2. See QR code
3. Screenshot confirmation
4. Go to theatre at show time!
```

---

## ✅ Booking Checklist

Before you go to the theatre:
- [ ] Check booking confirmation
- [ ] Note down Booking ID
- [ ] Save QR code screenshot
- [ ] Verify show date & time
- [ ] Check theatre address
- [ ] Reach 15 minutes early

---

## 📱 Contact Support

For help with:
- Booking issues
- Payment problems
- Cancellation queries
- Technical errors

Contact: support@moviebox.com (in production)

---

## 🎉 Enjoy Your Movie!

Thank you for using our Movie Ticket Booking System!

**Happy watching! 🍿🎬**

