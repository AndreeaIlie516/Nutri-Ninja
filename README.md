# NutriNinja: Advanced Food Recognition and Nutritional Tracking

## Overview

Welcome to the **NutriNinja** repository! 
This project is focused on enhancing dietary management through image processing machine learning techniques. 
NutriNinja is an engaging nutrition-tracking application that uses semantic segmentation for precise food recognition and integrates this with a user-friendly mobile application to monitor and analyse daily nutritional intake.

## Screenshots

<img width="400" alt="Main_screen_1" src="https://github.com/user-attachments/assets/03f6fbff-0e43-4664-884c-500868f3f27e">

<img width="400" alt="Main_screen_2" src="https://github.com/user-attachments/assets/3ae407f8-da8d-4b18-b8a7-7db6902f3aae">

<img width="400" alt="Search_screen_1" src="https://github.com/user-attachments/assets/c875ae45-3e19-4377-822b-8970663aa379">

<img width="400" alt="Search_screen_2" src="https://github.com/user-attachments/assets/bdf00d09-dad9-44a1-a09c-43f347c60ec6">

## Features

### 1. Image-Based Food Recognition
- **Semantic Segmentation**: NutriNinja uses a deep learning model to identify and segment food items directly from user-uploaded images. This feature simplifies the meal-logging process by automatically detecting food types, reducing the need for manual data entry.

### 2. Automated Nutritional Analysis
- **Comprehensive Nutritional Information**: After identifying food items, the application provides a detailed breakdown of calories, proteins, carbohydrates, and fats for each meal. This information helps users stay informed about their dietary habits.
- **Real-Time Feedback**: Users receive immediate nutritional analysis as they log their meals, enabling them to make better dietary choices throughout the day.

### 3. Grocery List Automation
- **Personalized Grocery Lists**: NutriNinja offers grocery list support, which will soon be updated to automatically generate based on the user’s meal logs from the previous week. The application will analyze food consumption patterns and suggest items to purchase, streamlining the meal planning process.
- **Editable Lists**: Users can review, edit, and customize their grocery lists, ensuring that all necessary ingredients are accounted for.

### 4. User-Friendly Interface
- **Engaging Android App**: The mobile application is developed in Kotlin using Jetpack Compose, following the Model-View-ViewModel (MVVM) architecture. This design ensures a responsive, intuitive, and aesthetically pleasing user experience.
- **Meal Logging Options**: Users can log meals manually by searching the database or uploading images for automatic recognition. The application allows for flexible meal tracking, accommodating different user preferences.

### 5. Scalable Backend
- **API-Driven Architecture**: The backend, developed using Golang with Gin and Python with Flask, handles user data management and meal logging and integrates with external APIs (such as FatSecret and Unsplash) to retrieve accurate nutritional data.

## Architecture Overview

NutriNinja is designed with a layered architecture to ensure modularity and scalability:

- **Client Layer (Android Application)**:
  - Developed in Kotlin, using Jetpack Compose.
  - Follows the MVVM architecture pattern.
  - Provides a responsive and intuitive user interface.
  
- **Backend Layer (APIs)**:
  - Composed of multiple APIs developed using Golang (Gin framework) and Flask.
  - Handles user data, meal information, and machine learning operations.
  - Integrates with external APIs for fetching nutritional information.

- **Database Layer**:
  - Utilizes PostgreSQL for secure and efficient data storage.
  - Manages user profiles, meal logs, and food item information.

## Getting Started

### Prerequisites
- **Android Studio**: To develop and test the Android application.
- **Golang**: For running the backend APIs.
- **Python**: Required for the Flask-based APIs and machine learning models.
- **PostgreSQL**: For the database setup.

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/AndreeaIlie516/Nutri-Ninja
    cd Nutri-Ninja
    ```

2. **Set up the Android application**:
    - Open the project in Android Studio.
    - Sync the project with Gradle files.
    - Build and run the application on an emulator or physical device.

3. **Set up the backend**:
    - Navigate to the `Nutri-Ninja-API/`, `FoodAPI/`, or `Food-Recognition-API/` directory.
    - Install the required Go and Python dependencies:
      ```bash
      go mod download
      pip install -r requirements.txt
      ```
    - Configure PostgreSQL and update the connection settings in the backend configuration files.
    - Run the backend services:
      ```bash
      go run main.go  # For the Golang-based API
      python app.py   # For the Flask-based APIs
      ```

4. **Database Setup**:
    - Set up PostgreSQL and create the necessary databases and tables.
    - Migrate the database schema using the provided migration scripts.

5. **Run the Application**:
    - Once all services are running, use the Android application to interact with the backend and start logging meals.

## Usage

- **Tracker Screen**: Monitor daily calorie intake and macronutrients, with detailed logs for each meal.
- **Search Screen**: Manually add meals via a search feature or use the image recognition module to automate the process.
- **Grocery List**: Editable grocery list.
