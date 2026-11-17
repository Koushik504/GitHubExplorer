# GitHub Explorer

Mini project for **Modern Android Application Development**, built using Kotlin and Jetpack Compose.

## Team Members and Contributions

| Name | SRN | Contribution |
| --- | --- | --- |
| Sachin B R | PES1UG25CS839 | Favorites using Room, settings and theme persistence using SharedPreferences |
| Anuvidya | PES1UG25CS804 | Repository search, sorting, pagination and repository details |
| Koushik Bestha | PES1UG23CS142 | User search, profiles, avatars, user repository lists, browser and sharing actions |

All members contributed through the shared GitHub repository.

## Project Description

GitHub Explorer uses the GitHub REST API to search users and repositories. Users can explore profiles, view repository details, save favorite repositories locally, and customize the app's theme.

## Features

- Search GitHub users and view their avatars.
- View profiles, biographies, locations, followers and following counts.
- Browse a user's public repositories.
- Search repositories by keyword.
- Sort repository results by best match, stars, forks or recent updates.
- Load additional repository results.
- View repository descriptions, languages, stars, forks, subscribers, issues, licenses and other details.
- Add, remove and clear favorite repositories.
- Keep favorites saved after restarting the app.
- Switch between light and dark themes and preserve the selected setting.
- Open profiles or repositories in a browser.
- Share repository links through Android's sharing menu.
- Display loading, empty-result and error states.

## Technologies Used

| Technology | Purpose |
| --- | --- |
| Kotlin | Application programming language |
| Jetpack Compose and Material 3 | UI and navigation |
| Retrofit | GitHub REST API requests |
| Gson | Convert JSON responses into Kotlin models |
| Glide | Load user and repository-owner avatars |
| Room Database | Store favorite repositories locally |
| SharedPreferences | Save the dark theme preference |
| ViewModel | Manage screen state and application logic |
| Kotlin Coroutines and Flow | Asynchronous requests and database updates |
| Android resources | String and color resources |
| Implicit intents | Open browser links and share repository links |

## Requirements

- Android Studio with the SDK required by the project.
- A JDK compatible with the project's Gradle configuration.
- Android 10 (API 29) or newer.
- Internet access for GitHub searches and details.
- An Android emulator or physical Android device.

## Setup and Run

1. Clone the repository:

   ```bash
   git clone https://github.com/Koushik504/GitHubExplorer.git
   ```

2. Open the `GitHubExplorer` folder in Android Studio.
3. Allow Gradle sync to finish.
4. Install any required SDK components when prompted.
5. Start an emulator or connect an Android phone using USB or wireless debugging.
6. Select the device and the `app` run configuration.
7. Click **Run**.

## Verification

The merged app was manually checked for:

- Repository search, details, sorting and pagination.
- Adding and removing favorites.
- Favorites remaining saved after reopening.
- Browser opening and repository sharing.
- Dark theme persistence.

The app was also installed and run on a physical **iQOO Z7 Pro 5G**.

## API Limitations

GitHub may temporarily limit API requests. When this happens, the app displays a request-limit message. Internet access is required to retrieve new GitHub data; saved favorites are stored locally.

## Submission Materials

- [App demonstration video](Demo.mp4)
- [Project report with team details and screenshots](GitHubExplorer_Project_Report.pdf)
