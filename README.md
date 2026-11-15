## Team Members

| Name | SRN |
|---|---|
| Sachin B R | PES1UG25CS839 |
| Anuvidya | PES1UG25CS804 |
| Koushik Bestha | PES1UG23CS142 |

**Course:** Modern Android Application Development
# GitHub Explorer

### Modern Android Application Development – Mini Project

GitHub Explorer is an Android application developed using **Kotlin and Jetpack Compose**. It provides a convenient way to explore GitHub users and their public repositories using the GitHub REST API.

Users can search for GitHub profiles, view profile information, explore repositories, check repository details, and save their favorite repositories. The application also includes settings to customize the experience with dark mode support.

## Features

- **User Search:** Search for GitHub users using their usernames.
- **Profile Details:** View profile pictures, usernames, biographies, followers, following, and public repository counts.
- **Repository Explorer:** Browse public repositories with descriptions, programming languages, stars, and forks.
- **Favorites:** Save and remove repositories from a personalized favorites list.
- **Settings:** Manage application preferences and switch between light and dark themes.
- **Repository Actions:** Open repositories in a browser and share repository links.

## Technologies Used

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material 3
- **Networking:** Retrofit
- **JSON Parsing:** Gson
- **API:** GitHub REST API
- **Local Storage:** SharedPreferences
- **Architecture:** ViewModel and Kotlin Coroutines

## Requirements

Before setting up the project, make sure you have:

- Android Studio (recent stable version recommended)
- Android SDK
- JDK compatible with the project's Gradle configuration
- Git installed
- Internet connection
- Android emulator or a physical Android device

## Project Setup

### 1. Clone the Repository

Open a terminal and execute:

```bash
git clone https://github.com/Koushik504/GitHubExplorer.git
```

### 2. Open in Android Studio

1. Launch Android Studio.
2. Select **Open** from the welcome screen.
3. Navigate to the cloned `GitHubExplorer` folder.
4. Select the project and open it.
5. Wait for Gradle Sync to complete.

### 3. Configure the Android SDK

- Make sure the Android SDK required by the project is installed.
- If Android Studio displays a missing SDK or Gradle JDK warning, install or select the required version through its settings.

### 4. Run the Application

1. Open Device Manager in Android Studio.
2. Create or start an Android Virtual Device (AVD), or connect a physical Android device with USB debugging enabled.
3. Select the `app` run configuration.
4. Click the **Run ▶** button.
5. Wait for the application to install and launch.



