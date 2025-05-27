# 🌦️ WeatherNow – Real-Time Weather App

WeatherNow is a simple and clean Android application that displays real-time weather information based on your current location or a city of your choice. It uses the OpenWeatherMap API to fetch accurate weather details like **temperature**, **humidity**, and **wind speed**, along with dynamic weather icons.

![WeatherNow Screenshot](screenshot.png) <!-- Replace with your screenshot -->

---

## 📱 Features

* 📍 **Current Location Weather**
  Automatically detects your location and displays the latest weather data.

* 🏙️ **Search by City**
  Enter any city name to retrieve and view real-time weather updates.

* 🌡️ **Comprehensive Weather Info**

  * Temperature (°C)
  * Humidity (%)
  * Wind Speed (km/h)
  * Weather icon and description

* 🌐 **Powered by OpenWeatherMap**
  Ensures accurate and up-to-date information globally.

* 📷 **Dynamic Icons**
  Weather condition icons load automatically from OpenWeatherMap’s API.

---

## 🧰 Tech Stack

* **Language:** Java
* **SDK:** Android SDK
* **Location Services:** Google Play Services (FusedLocationProviderClient)
* **Networking:** Volley
* **Image Loading:** Glide
* **Weather API:** [OpenWeatherMap](https://openweathermap.org/api)

---

## 📸 Screenshots

| Current Location                          | City Search                                  |
| ----------------------------------------- | -------------------------------------------- |
| ![Screenshot 1](screenshots/location.png) | ![Screenshot 2](screenshots/city_search.png) |

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/weathernow.git
cd weathernow
```

### 2. Open in Android Studio

* Open Android Studio
* Select **"Open an existing project"**
* Navigate to the `weathernow` folder

### 3. Get an API Key

* Sign up at [OpenWeatherMap](https://openweathermap.org/api)
* Replace the `API_KEY` in `MainActivity.java` with your key:

```java
private static final String API_KEY = "YOUR_API_KEY_HERE";
```

### 4. Build and Run

* Make sure your device or emulator has Internet and Location access
* Run the app!

---

## 🔐 Permissions Used

* `ACCESS_FINE_LOCATION` – To detect your location
* `INTERNET` – To fetch data from OpenWeatherMap

---

## 🙋 FAQ

**Q: What happens if location permission is denied?**
A: You can still search for any city's weather manually.

**Q: How frequently is the weather updated?**
A: The app fetches the latest data on each search or app open.

---

## 📄 License

MIT License. See `LICENSE` for details.

---

Let me know if you want this README exported to a file or customized with your app icon and screenshots.
