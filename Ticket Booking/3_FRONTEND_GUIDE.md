# ⚛️ Frontend Architecture Guide

Complete guide to understanding the Movie Ticket Booking System frontend built with React.

---

## 📋 Table of Contents

1. [Technology Stack](#technology-stack)
2. [Project Structure](#project-structure)
3. [Key Features](#key-features)
4. [Page Components](#page-components)
5. [State Management](#state-management)
6. [API Integration](#api-integration)
7. [Styling & UI](#styling--ui)
8. [Routing](#routing)

---

## 1. Technology Stack

### Core Technologies

- **React 18** - UI library
- **Vite** - Build tool & dev server
- **React Router DOM 6** - Client-side routing
- **Axios** - HTTP client
- **Ant Design 5** - UI component library
- **Tailwind CSS** - Utility-first CSS

### Development Tools

- **ESLint** - Code linting
- **PostCSS** - CSS processing
- **npm** - Package manager

---

## 2. Project Structure

```
frontend/
├── public/               # Static assets
├── src/
│   ├── components/      # Reusable components
│   │   └── Navbar.jsx   # Navigation bar
│   ├── hooks/           # Custom React hooks
│   │   └── useAuth.js   # Authentication hook
│   ├── pages/           # Page components
│   │   ├── admin/       # Admin pages
│   │   │   ├── AdminDashboard.jsx
│   │   │   └── MovieManagement.jsx
│   │   ├── BookingConfirmation.jsx
│   │   ├── Home.jsx
│   │   ├── Login.jsx
│   │   ├── MovieDetails.jsx
│   │   ├── Payment.jsx
│   │   ├── SeatSelection.jsx
│   │   ├── ShowSelection.jsx
│   │   ├── Signup.jsx
│   │   └── UserBookings.jsx
│   ├── services/        # API services
│   │   └── api.js       # Axios instance & API calls
│   ├── utils/           # Utility functions
│   │   └── helpers.js   # Helper functions
│   ├── App.jsx          # Main app component
│   ├── main.jsx         # Entry point
│   └── index.css        # Global styles
├── index.html           # HTML template
├── package.json         # Dependencies
├── vite.config.js       # Vite configuration
└── tailwind.config.js   # Tailwind configuration
```

---

## 3. Key Features

### 3.1 User Authentication

**Login/Signup Flow:**
1. User enters credentials
2. API call to backend
3. JWT token received
4. Token stored in localStorage
5. User redirected to home page

**Auth Persistence:**
- Token stored in localStorage
- Checked on every page load
- Auto-redirect if expired

### 3.2 Protected Routes

```jsx
<Route path="/bookings/:id/confirm" element={
  <ProtectedRoute>
    <BookingConfirmation />
  </ProtectedRoute>
} />
```

**Admin-only Routes:**
```jsx
<Route path="/admin/*" element={
  <ProtectedRoute adminOnly={true}>
    <AdminDashboard />
  </ProtectedRoute>
} />
```

### 3.3 Real-Time Seat Selection

**Features:**
- Visual seat map
- Color-coded seat status
- Real-time lock/unlock
- 5-minute countdown timer
- Prevents double booking

**Seat States:**
- 🟦 **Available** - Gray, clickable
- 🟩 **Selected** - Green, user's selection
- 🟥 **Booked** - Red, cannot select
- 🟨 **Locked** - Yellow, locked by another user

### 3.4 Responsive Design

**Breakpoints:**
- Mobile: < 640px
- Tablet: 640px - 1024px
- Desktop: > 1024px

**Features:**
- Collapsible navbar on mobile
- Stacked layout on small screens
- Touch-friendly buttons
- Optimized images

---

## 4. Page Components

### 4.1 Public Pages

#### **Home Page (`Home.jsx`)**

**Purpose:** Landing page showing movies

**Features:**
- Tabs: "Now Showing" / "Coming Soon"
- Movie grid layout
- Search/filter functionality
- Click to view details

**Key Code:**
```jsx
const [movies, setMovies] = useState([]);
const [activeTab, setActiveTab] = useState('NOW_SHOWING');

useEffect(() => {
  const fetchMovies = async () => {
    const response = await movieAPI.getByStatus(activeTab);
    setMovies(response.data);
  };
  fetchMovies();
}, [activeTab]);
```

---

#### **Movie Details (`MovieDetails.jsx`)**

**Purpose:** Show full movie information

**Features:**
- Large poster display
- Full description
- Genre, duration, rating
- Trailer link
- "Book Tickets" button

**Data Flow:**
```
URL: /movies/:id
  ↓
useParams() to get movie ID
  ↓
API call: movieAPI.getById(id)
  ↓
Display movie details
```

---

#### **Show Selection (`ShowSelection.jsx`)**

**Purpose:** Choose show time & theatre

**Features:**
- City filter dropdown
- Date picker
- List of available shows
- Theatre & screen info
- Pricing display
- Seat availability count

**Key State:**
```jsx
const [shows, setShows] = useState([]);
const [selectedCity, setSelectedCity] = useState('');
const [selectedDate, setSelectedDate] = useState(new Date());
```

---

#### **Seat Selection (`SeatSelection.jsx`)**

**Purpose:** Interactive seat selection

**Features:**
- Visual seat grid
- Real-time locking
- Countdown timer (5 min)
- Selected seats summary
- Total price calculation
- "Proceed to Payment" button

**Seat Locking Logic:**
```jsx
const handleSeatClick = async (seat) => {
  if (seat.status === 'AVAILABLE') {
    // Lock seat via API
    const response = await bookingAPI.lockSeats({
      showId,
      seatIds: [seat.id],
      userId: user.id
    });
    
    // Update local state
    setSelectedSeats([...selectedSeats, seat]);
    
    // Start 5-min countdown
    startCountdown();
  }
};
```

---

#### **Payment (`Payment.jsx`)**

**Purpose:** Process payment

**Features:**
- Booking summary
- Payment method selection
- Mock payment processing
- Transaction ID generation

**Payment Flow:**
```jsx
const handlePayment = async () => {
  // 1. Create payment
  const payment = await paymentAPI.create(transactionId, method);
  
  // 2. Simulate payment processing
  await simulatePayment();
  
  // 3. Confirm booking
  const booking = await bookingAPI.confirm(bookingId, transactionId);
  
  // 4. Navigate to confirmation
  navigate(`/bookings/${booking.id}/confirm`);
};
```

---

#### **Booking Confirmation (`BookingConfirmation.jsx`)**

**Purpose:** Show booking success

**Features:**
- Booking ID display
- QR code generation
- Complete booking details
- Email confirmation message
- Download ticket button
- "View My Bookings" link

---

### 4.2 User Pages (Protected)

#### **User Bookings (`UserBookings.jsx`)**

**Purpose:** Show user's booking history

**Features:**
- List of all bookings
- Status badges (Confirmed/Cancelled)
- Booking details
- Cancel button (policy-based)
- Filter by status

**Data Fetching:**
```jsx
useEffect(() => {
  const fetchBookings = async () => {
    const response = await bookingAPI.getUserBookings(user.id);
    setBookings(response.data);
  };
  fetchBookings();
}, [user.id]);
```

---

### 4.3 Admin Pages (Admin Only)

#### **Admin Dashboard (`admin/AdminDashboard.jsx`)**

**Purpose:** Admin overview

**Features:**
- Statistics cards:
  - Total Movies
  - Total Theatres
  - Total Bookings
  - Total Revenue
- Quick action links
- Recent activity

**Statistics API:**
```jsx
const [stats, setStats] = useState({
  movies: 0,
  theatres: 0,
  bookings: 0,
  revenue: 0
});

useEffect(() => {
  const fetchStats = async () => {
    const [movies, theatres, bookings] = await Promise.all([
      movieAPI.getAll(),
      theatreAPI.getAll(),
      bookingAPI.getAll()
    ]);
    
    setStats({
      movies: movies.data.length,
      theatres: theatres.data.length,
      bookings: bookings.data.length,
      revenue: calculateRevenue(bookings.data)
    });
  };
  fetchStats();
}, []);
```

---

#### **Movie Management (`admin/MovieManagement.jsx`)**

**Purpose:** CRUD operations for movies

**Features:**
- List all movies
- Search/filter
- Add new movie form
- Edit existing movie
- Delete movie (with confirmation)
- Status toggle

**Form Handling:**
```jsx
const handleSubmit = async (values) => {
  try {
    if (editingMovie) {
      await movieAPI.update(editingMovie.id, values);
      message.success('Movie updated');
    } else {
      await movieAPI.create(values);
      message.success('Movie created');
    }
    fetchMovies();
    closeModal();
  } catch (error) {
    message.error('Operation failed');
  }
};
```

---

## 5. State Management

### 5.1 Local State (useState)

Used for:
- Form inputs
- Loading states
- Modal visibility
- Temporary data

Example:
```jsx
const [loading, setLoading] = useState(false);
const [movies, setMovies] = useState([]);
const [selectedSeats, setSelectedSeats] = useState([]);
```

### 5.2 Context (useContext)

Not implemented yet, but could be used for:
- Global theme
- User preferences
- Cart state

### 5.3 localStorage

Used for:
- JWT token
- User data
- Session persistence

```jsx
// Store
localStorage.setItem('token', token);
localStorage.setItem('user', JSON.stringify(user));

// Retrieve
const token = localStorage.getItem('token');
const user = JSON.parse(localStorage.getItem('user'));

// Remove
localStorage.removeItem('token');
localStorage.removeItem('user');
```

---

## 6. API Integration

### 6.1 Axios Instance (`services/api.js`)

**Base Configuration:**
```jsx
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
});
```

**Request Interceptor:**
```jsx
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

**Response Interceptor:**
```jsx
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired, redirect to login
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### 6.2 API Methods

**Movie API:**
```jsx
export const movieAPI = {
  getAll: () => api.get('/movies'),
  getById: (id) => api.get(`/movies/${id}`),
  getByStatus: (status) => api.get(`/movies/status/${status}`),
  create: (movie) => api.post('/admin/movies', movie),
  update: (id, movie) => api.put(`/admin/movies/${id}`, movie),
  delete: (id) => api.delete(`/admin/movies/${id}`)
};
```

**Booking API:**
```jsx
export const bookingAPI = {
  lockSeats: (request) => api.post(`/shows/${request.showId}/lock-seats`, request),
  unlockSeats: (showId, seatIds) => api.post(`/shows/${showId}/unlock-seats`, seatIds),
  initiate: (request) => api.post('/bookings/initiate', request),
  confirm: (bookingId, transactionId) => api.post('/bookings/confirm', null, {
    params: { bookingId, transactionId }
  }),
  getUserBookings: (userId) => api.get('/users/me/bookings', { params: { userId } }),
  cancel: (id) => api.post(`/bookings/${id}/cancel`)
};
```

---

## 7. Styling & UI

### 7.1 Ant Design Components

**Used Components:**
- Layout (Header, Content, Footer)
- Form, Input, Select, DatePicker
- Button, Card, Modal
- Table, List
- Message, Notification
- Spin (Loading)
- Tag, Badge
- Tabs, Menu

**Theme Configuration:**
```jsx
<ConfigProvider
  theme={{
    token: {
      colorPrimary: '#1890ff',
      borderRadius: 6,
    }
  }}
>
  <App />
</ConfigProvider>
```

### 7.2 Tailwind CSS

**Common Classes:**
```jsx
// Layout
className="container mx-auto px-4"
className="grid grid-cols-1 md:grid-cols-3 gap-4"
className="flex items-center justify-between"

// Spacing
className="p-6 m-4"
className="mt-8 mb-4"

// Colors
className="bg-blue-500 text-white"
className="hover:bg-blue-600"

// Responsive
className="hidden md:block"
className="text-sm md:text-base lg:text-lg"
```

### 7.3 Custom Styles

**Global Styles (`index.css`):**
```css
/* Tailwind directives */
@tailwind base;
@tailwind components;
@tailwind utilities;

/* Custom utilities */
@layer utilities {
  .seat-available {
    @apply bg-gray-300 hover:bg-gray-400 cursor-pointer;
  }
  
  .seat-selected {
    @apply bg-green-500 text-white;
  }
  
  .seat-booked {
    @apply bg-red-500 text-white cursor-not-allowed;
  }
}
```

---

## 8. Routing

### 8.1 Route Structure

```jsx
<BrowserRouter>
  <Routes>
    {/* Public Routes */}
    <Route path="/" element={<Home />} />
    <Route path="/login" element={<Login />} />
    <Route path="/signup" element={<Signup />} />
    <Route path="/movies/:id" element={<MovieDetails />} />
    
    {/* Protected Routes */}
    <Route path="/bookings/:id/confirm" element={
      <ProtectedRoute><BookingConfirmation /></ProtectedRoute>
    } />
    
    {/* Admin Routes */}
    <Route path="/admin/*" element={
      <ProtectedRoute adminOnly><AdminDashboard /></ProtectedRoute>
    } />
  </Routes>
</BrowserRouter>
```

### 8.2 Navigation

**Programmatic Navigation:**
```jsx
import { useNavigate } from 'react-router-dom';

const navigate = useNavigate();

// Navigate to page
navigate('/movies/123');

// Navigate with state
navigate('/payment', { state: { bookingData } });

// Go back
navigate(-1);
```

**Route Parameters:**
```jsx
import { useParams } from 'react-router-dom';

const { id } = useParams();
// URL: /movies/123 → id = "123"
```

**Query Parameters:**
```jsx
import { useSearchParams } from 'react-router-dom';

const [searchParams] = useSearchParams();
const city = searchParams.get('city');
// URL: /shows?city=Mumbai → city = "Mumbai"
```

---

## 🎨 UI/UX Best Practices

1. **Loading States** - Show spinners during API calls
2. **Error Handling** - Display user-friendly error messages
3. **Form Validation** - Real-time validation with feedback
4. **Confirmation Dialogs** - Before destructive actions
5. **Toast Notifications** - Success/error messages
6. **Responsive Design** - Works on all devices
7. **Accessibility** - ARIA labels, keyboard navigation

---

## 🚀 Performance Optimizations

1. **Code Splitting** - Lazy load routes
2. **Image Optimization** - Lazy loading, WebP format
3. **Memoization** - useMemo, useCallback for expensive operations
4. **Debouncing** - Search inputs
5. **Pagination** - Large lists
6. **Caching** - Store frequently accessed data

---

## 📦 Build & Deployment

**Development:**
```bash
npm run dev
```

**Production Build:**
```bash
npm run build
```

**Build Output:**
```
dist/
├── index.html
├── assets/
│   ├── index-[hash].js
│   └── index-[hash].css
└── vite.svg
```

---

**For more details, see the source code in `frontend/src/`**

