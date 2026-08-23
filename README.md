# FoodPlanner

FoodPlanner is an Android meal-planning application that helps users discover meals, search for recipes, save favorites, and organize meals throughout the week.

The application uses TheMealDB API for meal data and provides local storage for favorites and weekly planning.

## Features

### Authentication

* User registration and login using Firebase Authentication.
* Guest access for browsing meals.
* Persistent user sessions.
* Secure logout.

### Home

* Meal of the Day.
* Popular meals.
* Quick access to meal details.
* Search meals directly from the Home screen.

### Search

Users can search for meals by name and explore available recipes.

### Meal Details

Each meal provides:

* Meal name
* Category
* Country/Area
* Ingredients
* Cooking instructions
* Meal image
* YouTube video when available

### Favorites

* Add meals to Favorites.
* Remove meals from Favorites.
* Store favorites locally for offline access.

### Weekly Planner

* Select a day from the calendar.
* Add meals to the selected day.
* View planned meals.
* Remove meals from the plan.
* Keep planned meals available locally.
  
### Google Calendar Integration
* Meal name
* Selected date
* Scheduled time
* Description indicating that the meal was added from FoodPlanner

### Categories and Countries

* Browse meals by category.
* Explore meals by country/area.
* Country flags are used to improve the browsing experience.

### UI

The application follows a consistent dark Material Design style with:

* Custom colors and typography
* Rounded meal cards
* Bottom navigation
* Navigation drawer
* RecyclerViews
* Loading and error states

## Tech Stack

* Kotlin
* Android SDK
* Material Design 3
* Retrofit for API communication
* RxJava 3 for asynchronous operations
* Room Database for local persistence
* Firebase Authentication
* Glide for image loading
* TheMealDB API
* Git and GitHub

## Architecture

The project follows the MVVM architecture.

```text
UI
 ↓
ViewModel
 ↓
Repository
 ↓
API / Room / Firebase
```

Main project structure:

```text
com.example.foodplanner
│
├── data
│   ├── api
│   ├── local
│   ├── model
│   └── repository
│
├── ui
│   ├── auth
│   ├── home
│   ├── categories
│   ├── countries
│   ├── search
│   ├── details
│   ├── favorites
│   └── planner
│
├── base
├── utils
└── MainActivity.kt
```

## API

FoodPlanner uses TheMealDB API to retrieve meal information.

Supported operations include:

```text
Search meal by name
Lookup meal details by ID
Get random meal
List meal categories
List meal areas
List ingredients
Filter meals by category
Filter meals by area
Filter meals by ingredient
```

API documentation:

https://www.themealdb.com/api.php

## Local Storage

Room Database is used to store application data locally.

The local database supports:

```text
Favorites
Weekly Planner
```

This allows important user data to remain available when the device is offline.

## Firebase

Firebase Authentication is used for:

* User registration
* Login
* Session management
* Logout

User-specific data is associated with the currently authenticated user so that different users can maintain separate application data.

## Design System

The project uses a shared dark theme.

| Purpose         | Color     |
| --------------- | --------- |
| Background      | `#272936` |
| Surface         | `#202230` |
| Surface Variant | `#303243` |
| Primary         | `#4F6DFF` |
| Primary Dark    | `#3047C7` |
| Primary Light   | `#7088FF` |
| Accent          | `#6957FF` |
| Primary Text    | `#FFFFFF` |
| Secondary Text  | `#B8BAC6` |
| Favorite        | `#FF4D67` |
| Success         | `#4CAF50` |
| Error           | `#F44336` |

## Getting Started

### Prerequisites

Make sure you have:

* Android Studio
* Android SDK
* JDK compatible with the project
* A Firebase project configured for the application
* Internet connection for API requests


## Team Workflow

The project is developed collaboratively using GitHub.

Branches are organized by feature:

```text
main
develop
feature/auth
feature/meals
feature/favorites
feature/planner
```

Typical workflow:

```text
Pull latest changes
        ↓
Work on feature branch
        ↓
Commit
        ↓
Push
        ↓
Pull Request
        ↓
Review
        ↓
Merge
```

## Main Application Flow

```text
Splash
   ↓
Login / Register / Guest
   ↓
Home
   ├── Search
   ├── Meal of the Day
   ├── Categories
   ├── Countries
   ├── Favorites
   └── Weekly Planner
```

## Team Members

### Basmla Mahmoud — Person 1

Responsible for:

* Authentication
* Login and Registration
* Firebase Authentication
* Splash screen
* User session management

### Sama Safwat — Person 2

Responsible for:

* TheMealDB API integration
* Meal data
* Search
* Categories
* Countries
* Meal Details

### Jana Sadeek — Person 3

Responsible for:

* Room Database
* Favorites
* Offline storage
* Local data management

### Asmaa Abdallah — Person 4

Responsible for:

* Weekly Planner
* Calendar-based meal planning
* Planner UI
* Planner integration with local storage
* Firebase synchronization and integration

## Future Improvements

Possible future improvements include:

* Improved country and flag filtering.
* Better caching of API results.
* More advanced weekly calendar support.
* Improved Firebase synchronization.
* Enhanced offline support.
* Additional UI animations and accessibility improvements.


## License

This project was developed as an academic Android application project.
