# MateDroid - Android App for Teslamate

## Overview

MateDroid is a native Android application that displays Tesla vehicle data and statistics from a self-hosted [Teslamate](https://github.com/adriankumpf/teslamate) instance via the [TeslamateApi](https://github.com/tobiasehlert/teslamateapi).

The app provides a clean, modern interface for viewing:
- Real-time vehicle status
- Charging history and statistics
- Drive/trip history and efficiency metrics
- Battery health tracking
- Software update history

---

## Technology Stack Recommendation

### Language: **Kotlin**
- Official language for Android development since 2019
- Concise, expressive, and null-safe
- Excellent coroutine support for async operations
- Massive documentation and LLM training data coverage

### UI Framework: **Jetpack Compose**
- Modern declarative UI toolkit (official Google recommendation)
- Less boilerplate than XML-based layouts
- Excellent for building charts and custom visualizations
- Hot reload support for faster development
- Material Design 3 built-in

### Build System: **Gradle (Kotlin DSL)**
- Full CLI support (`./gradlew build`, `./gradlew installDebug`)
- Works perfectly on Linux terminal
- No IDE required (though Android Studio available if wanted)

### Networking: **Retrofit + OkHttp**
- Industry standard for REST APIs
- Kotlin coroutines integration
- Easy JSON parsing with Moshi/Kotlinx.serialization

### Charts: **Vico**
- Modern Jetpack Compose-native charting library
- Beautiful, customizable charts
- Active development and good documentation

### Architecture: **MVVM + Clean Architecture**
- ViewModels for UI state management
- Repository pattern for data layer
- Use cases for business logic
- Easy to test and maintain

### Dependency Injection: **Hilt**
- Official Android DI solution
- Reduces boilerplate
- Compile-time verification

---

## Development Environment Setup

### Required Tools (Linux)

```bash
# 1. Install Java 17 (required for Android development)
sudo apt install openjdk-17-jdk

# 2. Install Android SDK command-line tools
# Download from: https://developer.android.com/studio#command-tools
mkdir -p ~/Android/Sdk/cmdline-tools
cd ~/Android/Sdk/cmdline-tools
unzip commandlinetools-linux-*.zip
mv cmdline-tools latest

# 3. Set environment variables (add to ~/.bashrc or ~/.zshrc)
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# 4. Accept licenses and install required SDK components
sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# 5. (Optional) Install Android Studio for visual debugging
# Download from: https://developer.android.com/studio
```

### CLI Workflow

```bash
# Build the project
./gradlew build

# Run unit tests
./gradlew test

# Install debug APK to connected device/emulator
./gradlew installDebug

# Create release APK
./gradlew assembleRelease

# Lint checks
./gradlew lint
```

---

## Project Structure

```
matedroid/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/matedroid/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── MateDroidApp.kt
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── TeslamateApi.kt          # Retrofit interface
│   │   │   │   │   │   └── models/                  # API response DTOs
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   └── TeslamateRepository.kt
│   │   │   │   │   └── local/
│   │   │   │   │       └── SettingsDataStore.kt     # Local preferences
│   │   │   │   │
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/                       # Domain models
│   │   │   │   │   └── usecase/                     # Business logic
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   │
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   └── NavGraph.kt
│   │   │   │   │   │
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── dashboard/
│   │   │   │   │   │   │   ├── DashboardScreen.kt
│   │   │   │   │   │   │   └── DashboardViewModel.kt
│   │   │   │   │   │   ├── charges/
│   │   │   │   │   │   │   ├── ChargesScreen.kt
│   │   │   │   │   │   │   ├── ChargeDetailScreen.kt
│   │   │   │   │   │   │   └── ChargesViewModel.kt
│   │   │   │   │   │   ├── drives/
│   │   │   │   │   │   │   ├── DrivesScreen.kt
│   │   │   │   │   │   │   ├── DriveDetailScreen.kt
│   │   │   │   │   │   │   └── DrivesViewModel.kt
│   │   │   │   │   │   ├── battery/
│   │   │   │   │   │   │   ├── BatteryHealthScreen.kt
│   │   │   │   │   │   │   └── BatteryViewModel.kt
│   │   │   │   │   │   ├── updates/
│   │   │   │   │   │   │   └── UpdatesScreen.kt
│   │   │   │   │   │   └── settings/
│   │   │   │   │   │       ├── SettingsScreen.kt
│   │   │   │   │   │       └── SettingsViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   └── components/
│   │   │   │   │       ├── StatCard.kt
│   │   │   │   │       ├── ChargeChart.kt
│   │   │   │   │       ├── EfficiencyChart.kt
│   │   │   │   │       └── LoadingIndicator.kt
│   │   │   │   │
│   │   │   │   └── di/
│   │   │   │       ├── AppModule.kt
│   │   │   │       └── NetworkModule.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── colors.xml
│   │   │   │   └── drawable/
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/                                    # Unit tests
│   │
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts                                 # Root build file
├── settings.gradle.kts
├── gradle.properties
├── local.properties                                 # SDK path (gitignored)
└── README.md
```

---

## TeslamateApi Integration

### API Endpoints to Implement

| Priority | Endpoint | Purpose |
|----------|----------|---------|
| P0 | `GET /api/v1/cars` | List vehicles (needed first) |
| P0 | `GET /api/v1/cars/:id/status` | Real-time vehicle status |
| P0 | `GET /api/v1/cars/:id/charges` | Charging history |
| P0 | `GET /api/v1/cars/:id/drives` | Drive history |
| P1 | `GET /api/v1/cars/:id/battery-health` | Battery degradation |
| P1 | `GET /api/v1/cars/:id/charges/:id` | Charge session detail |
| P1 | `GET /api/v1/cars/:id/drives/:id` | Drive detail |
| P2 | `GET /api/v1/cars/:id/updates` | Software updates |
| P2 | `POST /api/v1/cars/:id/wake_up` | Wake vehicle |
| P3 | `POST /api/v1/cars/:id/command/:cmd` | Vehicle commands |

### Authentication

The API supports token-based authentication:
- Header: `Authorization: Bearer <token>`
- Query param: `?token=<token>`

The app will store the API URL and token securely in encrypted SharedPreferences (DataStore).

### Retrofit Interface

```kotlin
interface TeslamateApi {
    @GET("api/v1/cars")
    suspend fun getCars(): Response<CarsResponse>

    @GET("api/v1/cars/{carId}/status")
    suspend fun getCarStatus(@Path("carId") carId: Int): Response<CarStatus>

    @GET("api/v1/cars/{carId}/charges")
    suspend fun getCharges(
        @Path("carId") carId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<ChargesResponse>

    @GET("api/v1/cars/{carId}/drives")
    suspend fun getDrives(
        @Path("carId") carId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<DrivesResponse>

    @GET("api/v1/cars/{carId}/battery-health")
    suspend fun getBatteryHealth(@Path("carId") carId: Int): Response<BatteryHealth>
}
```

---

## App Screens & Features

### 1. Settings / Onboarding (First Launch)

**Purpose:** Configure connection to TeslamateApi instance

**UI Elements:**
- Server URL input field (e.g., `https://teslamate.example.com`)
- API Token input field (password masked)
- "Test Connection" button
- Save button

**Behavior:**
- Validates URL format
- Tests connection with `/api/ping`
- Stores credentials in encrypted DataStore
- Navigates to Dashboard on success

---

### 2. Dashboard Screen

**Purpose:** At-a-glance vehicle status

**UI Elements:**
- Vehicle name and image/icon
- Battery level with circular progress indicator
- Charging state (charging, not charging, plugged in)
- Current location (if available)
- Odometer reading
- Inside/outside temperature
- Last seen timestamp
- Quick stats cards:
  - Today's drives (count + distance)
  - This month's charges (count + kWh)
  - Efficiency (Wh/km)

**Data Source:** `GET /api/v1/cars/:id/status`

---

### 3. Charges Screen

**Purpose:** Charging history with statistics

**UI Elements:**
- Summary card at top:
  - Total charges count
  - Total energy added (kWh)
  - Average charge cost (if available)
  - Total cost (if available)
- Filter chips: Last 7 days / 30 days / 90 days / All time
- Line chart: Energy added over time
- List of charge sessions:
  - Date/time
  - Location
  - Energy added (kWh)
  - Duration
  - Cost (if available)
  - Start/end battery %

**Data Source:** `GET /api/v1/cars/:id/charges`

**Detail Screen:**
- Full charge session details
- Charge curve chart (if data available)
- Cost breakdown

---

### 4. Drives Screen

**Purpose:** Trip history with efficiency metrics

**UI Elements:**
- Summary card:
  - Total drives count
  - Total distance
  - Average efficiency (Wh/km)
  - Total duration
- Filter chips: Last 7 days / 30 days / 90 days / All time
- Bar chart: Daily/weekly distance
- List of drives:
  - Date/time
  - Start → End location
  - Distance
  - Duration
  - Efficiency (Wh/km)
  - Battery used %

**Data Source:** `GET /api/v1/cars/:id/drives`

**Detail Screen:**
- Full drive details
- Route map (if coordinates available)
- Speed/efficiency graphs

---

### 5. Battery Health Screen

**Purpose:** Monitor battery degradation over time

**UI Elements:**
- Current battery health percentage
- Original vs current capacity
- Line chart: Battery health over time
- Statistics:
  - Total charge cycles
  - Battery age
  - Degradation rate

**Data Source:** `GET /api/v1/cars/:id/battery-health`

---

### 6. Software Updates Screen

**Purpose:** Track software update history

**UI Elements:**
- Current software version
- List of past updates:
  - Version number
  - Update date
  - Time between updates

**Data Source:** `GET /api/v1/cars/:id/updates`

---

## Implementation Phases

### Phase 1: Foundation (MVP)
1. ✅ Create plan document
2. Project scaffolding with Gradle
3. Implement Settings screen (server config)
4. Basic API client with Retrofit
5. Dashboard screen with vehicle status
6. Basic error handling and loading states

**Deliverable:** App that connects to TeslamateApi and shows vehicle status

---

### Phase 2: Core Features
1. Charges screen with list and summary stats
2. Drives screen with list and summary stats
3. Pull-to-refresh functionality
4. Date filtering for charges/drives
5. Detail screens for individual charges/drives

**Deliverable:** Full browsing of charge and drive history

---

### Phase 3: Visualizations
1. Integrate Vico charting library
2. Charge history line chart
3. Drive distance bar chart
4. Battery level trends
5. Efficiency trends

**Deliverable:** Rich data visualizations

---

### Phase 4: Battery & Updates
1. Battery health screen with degradation tracking
2. Software updates history screen
3. Battery health trend chart

**Deliverable:** Complete vehicle health monitoring

---

### Phase 5: Polish & Extras
1. Dark/light theme with Material You
2. Multi-vehicle support (vehicle selector)
3. Offline caching with Room database
4. Widget for home screen (battery status)
5. Notifications for charge completion (optional)
6. Vehicle commands (wake, etc.) - requires careful consideration

**Deliverable:** Production-ready polished app

---

## Dependencies (build.gradle.kts)

```kotlin
dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // DataStore (encrypted preferences)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Charts
    implementation("com.patrykandpatrick.vico:compose-m3:1.13.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.9")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
```

---

## Design Guidelines

### Visual Style
- Material Design 3 (Material You)
- Support dynamic color theming on Android 12+
- Dark and light mode support
- Tesla-inspired color accents (red for Model S/X, blue for Model 3/Y)

### Color Palette
```
Primary: #E31937 (Tesla Red)
Secondary: #171A20 (Tesla Dark)
Surface: Material default
On-Surface: Material default
Accent Blue: #3E6AE1 (for Model 3/Y)
Success: #4CAF50 (battery full, charge complete)
Warning: #FF9800 (low battery)
Error: #F44336 (connection issues)
```

### Typography
- Roboto (Android default)
- Large numbers for key metrics
- Clear hierarchy with Material type scale

### Icons
- Material Icons (filled style)
- Custom battery/charging icons where needed

---

## Security Considerations

1. **Token Storage:** Use EncryptedSharedPreferences via AndroidX Security
2. **Network Security:** Enforce HTTPS (network security config)
3. **No Sensitive Logging:** Mask tokens in debug logs
4. **Certificate Pinning:** Optional for self-hosted instances

---

## Testing Strategy

Testing will be set up from Phase 1 and expanded as features are added.

### Unit Tests (src/test/)
**What:** Test business logic in isolation without Android framework

| Component | What to Test |
|-----------|--------------|
| ViewModels | State updates, error handling, data transformations |
| Repositories | Data fetching logic, caching behavior |
| Use Cases | Business rules, calculations (efficiency, totals) |
| API Models | JSON parsing, null handling |

**Tools:**
- JUnit 5 - Test framework
- MockK - Kotlin-friendly mocking
- Kotlinx Coroutines Test - Testing suspend functions
- Turbine - Testing Kotlin Flows

**Example:**
```kotlin
@Test
fun `dashboard shows error state when API fails`() = runTest {
    // Given
    coEvery { repository.getCarStatus(any()) } throws IOException()

    // When
    viewModel.loadDashboard()

    // Then
    assertIs<DashboardState.Error>(viewModel.state.value)
}
```

### Integration Tests (src/androidTest/)
**What:** Test components working together with real Android framework

| Test | Purpose |
|------|---------|
| API Client | Verify Retrofit correctly parses real API responses |
| Repository | Test data flow from API to domain models |
| DataStore | Verify settings persistence |

**Tools:**
- MockWebServer - Fake HTTP server for API tests
- Hilt Testing - DI in tests

### UI Tests (src/androidTest/)
**What:** Test Compose UI behavior and navigation

**Tools:**
- Compose UI Test - Find elements, perform clicks, verify state
- Navigation Testing - Verify screen transitions

**Example:**
```kotlin
@Test
fun settingsScreen_validUrl_enablesSaveButton() {
    composeTestRule.setContent {
        SettingsScreen()
    }

    composeTestRule
        .onNodeWithTag("urlInput")
        .performTextInput("https://teslamate.example.com")

    composeTestRule
        .onNodeWithTag("saveButton")
        .assertIsEnabled()
}
```

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run unit tests with coverage report
./gradlew testDebugUnitTest jacocoTestReport

# Run instrumented tests (requires emulator)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests "com.matedroid.ui.DashboardViewModelTest"
```

---

## Development Workflow

### Prerequisites
- Android Studio installed ✓
- Android Emulator configured ✓
- Java 17 installed

### Daily Development Cycle

#### 1. Start the Emulator
**Option A - Android Studio:**
1. Open Android Studio
2. **Tools → Device Manager**
3. Click the play button next to your emulator

**Option B - Command Line:**
```bash
# List available emulators
emulator -list-avds

# Start emulator (replace with your AVD name)
emulator -avd Pixel_7_API_34 &
```

#### 2. Build and Run the App

**Option A - Android Studio (Recommended for beginners):**
1. Open the `matedroid` folder in Android Studio
2. Wait for Gradle sync (bottom progress bar)
3. Click the green **Run ▶** button (top toolbar)
4. Select your emulator from the dropdown
5. App launches automatically

**Option B - Terminal:**
```bash
# Build and install debug APK
./gradlew installDebug

# Launch the app
adb shell am start -n com.matedroid/.MainActivity
```

#### 3. See Changes

**Hot Reload (Compose Preview):**
- In Android Studio, Compose `@Preview` functions render live
- Changes to UI code update instantly in the preview pane
- No need to rebuild for visual tweaks

**Apply Changes (Running App):**
- Android Studio: Click **Apply Changes** (⚡ button) for code changes
- Or **Apply Code Changes** (⚡⚡) for structural changes
- Full rebuild only needed for manifest/resource changes

**Manual Rebuild:**
```bash
# Rebuild and reinstall
./gradlew installDebug
```

#### 4. View Logs

**Android Studio:**
- **View → Tool Windows → Logcat**
- Filter by app: Select `com.matedroid` from dropdown

**Terminal:**
```bash
# All logs from the app
adb logcat | grep -i matedroid

# Or with pidcat (cleaner output, install separately)
pidcat com.matedroid
```

### Android Studio Tips for Beginners

| Task | How |
|------|-----|
| Open project | File → Open → Select `matedroid` folder |
| Run app | Green ▶ button or `Shift+F10` |
| Stop app | Red ■ button or `Ctrl+F2` |
| View logs | View → Tool Windows → Logcat |
| Compose preview | Open a file with `@Preview`, see right panel |
| Rebuild project | Build → Rebuild Project |
| Sync Gradle | Click "Sync Now" when prompted, or File → Sync Project |
| Find files | Double-tap `Shift`, then type filename |
| Find in files | `Ctrl+Shift+F` |

### Inspecting the Emulator

The emulator behaves like a real phone:
- **Swipe** to navigate
- **Click** to tap
- **Extended Controls** (... button): Simulate location, battery, network conditions
- **Screenshot**: Click camera icon in emulator toolbar
- **Screen Recording**: Click video icon for recordings

### Debugging

1. Set breakpoints: Click in the gutter (left of line numbers)
2. Run in debug mode: Click **Debug** (bug icon) instead of Run
3. Inspect variables when breakpoint hits
4. Step through code with F8 (step over) / F7 (step into)

---

## Future Considerations (Out of Scope for v1)

- iOS version with Kotlin Multiplatform
- Wear OS companion app
- Android Auto integration
- Geofencing (notifications when arriving/leaving locations)
- Integration with home automation (MQTT)
- Cost tracking with electricity rates
- Comparison with other vehicles

---

## Resources

- [TeslamateApi Documentation](https://github.com/tobiasehlert/teslamateapi)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Vico Charts](https://patrykandpatrick.com/vico/)
- [Android Developers Guide](https://developer.android.com/guide)

---

## Getting Started (Next Steps)

1. Install Java 17 and Android SDK command-line tools
2. Initialize the project structure
3. Configure Gradle build files
4. Implement the Settings screen for API configuration
5. Create the API client and test connection
6. Build the Dashboard screen

Ready to proceed with implementation when you are!
