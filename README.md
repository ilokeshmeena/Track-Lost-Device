# Track Lost Device

**Track Lost Device** is a native Android application designed to help users locate their lost devices using GPS tracking. It allows users to track their device in real-time, receive alerts, and manage multiple devices easily, all within a secure and user-friendly environment.

## Features

- **Real-Time Location Tracking**: Get the exact location of your lost device using its built-in GPS.
- **Geofence Alerts**: Receive notifications when the device enters or exits a designated area.
- **Multiple Device Support**: Manage and track more than one device from your account.
- **Secure**: Built with user authentication to ensure your data is safe.
- **Battery Efficiency**: Optimized to reduce power consumption while continuously tracking the device.

## Built With

- **Java**: Core functionality of the app.
- **Kotlin**: Used for modern Android components and asynchronous tasks.
- **Google Maps API**: For map integration and location services.
- **Firebase**: For real-time database, authentication, and cloud services.

## Getting Started

Follow the instructions below to set up the project locally.

### Prerequisites

- Android Studio [Download here](https://developer.android.com/studio)
- A Firebase account for backend services [Firebase Console](https://console.firebase.google.com)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ilokeshmeena/Track-Lost-Device.git
   cd Track-Lost-Device
   ```

2. Open the project in **Android Studio**:
   - Select `File` -> `Open...` and choose the project directory.

3. Set up Firebase:
   - Create a new Firebase project.
   - Enable Firebase Authentication (Google or Email).
   - Enable Firestore Database for real-time data.
   - Download the `google-services.json` file and place it in the `app/` directory.

4. Build and run the app:
   - In Android Studio, click `Run` to build the project and install it on a connected device or emulator.

### Usage

1. Launch the app on your Android device.
2. Sign in with your Google account or email-based authentication.
3. Register the device you want to track.
4. View real-time location information on the map.
5. Set up geofence alerts to receive notifications when the device enters or leaves a specific area.

## Project Structure

```bash
Track-Lost-Device/
├── app/                     # Main Android app code
│   ├── java/                # Java files for app logic
│   ├── kotlin/              # Kotlin files for modern Android components
│   ├── res/                 # Resource files (layouts, drawables, etc.)
│   └── google-services.json # Firebase configuration
├── build.gradle             # Project-level Gradle configuration
├── settings.gradle          # Settings for the project
└── README.md                # This file
```

## Contributing

Contributions are welcome! If you would like to contribute:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature-name`.
3. Commit your changes: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature/your-feature-name`.
5. Open a pull request for review.
