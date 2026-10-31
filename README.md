# Podcast App

An Android podcast client prototype built with Jetpack Compose and ExoPlayer. It explores podcast discovery through the Listen Notes API and playback through Android media service integration.

## Features represented in the source

- Welcome, discovery, podcast detail, and player screens.
- A Retrofit podcast-search service.
- ExoPlayer media playback, playback notifications, and media session integration.
- Cached search data through DataStore.
- Compose navigation and Listen Notes episode deep links.

## Architecture

- [data/network/](app/src/main/java/com/mobiledv/podcastapp/data/network/): Retrofit service and DTOs.
- [domain/repository/](app/src/main/java/com/mobiledv/podcastapp/domain/repository/): repository abstraction and implementations.
- [data/exoplayer/](app/src/main/java/com/mobiledv/podcastapp/data/exoplayer/): media source and player support.
- [data/service/](app/src/main/java/com/mobiledv/podcastapp/data/service/): playback service and connection.
- [ui/](app/src/main/java/com/mobiledv/podcastapp/ui/): Compose screens and ViewModels.
- [di/](app/src/main/java/com/mobiledv/podcastapp/di/): Hilt providers.

## Local configuration

The app build reads a Listen Notes key from the root `local.properties` file and exposes it as `BuildConfig.API_KEY`:

```properties
apiKey=YOUR_LISTEN_NOTES_API_KEY
```

Keep this local configuration out of version control. Android Studio also uses `local.properties` for the SDK location.

Open the repository in Android Studio and use the checked-in Gradle wrapper. The app currently declares compile SDK 32 and minimum Android API 21; install the matching Android SDK and use a JDK compatible with the project's Android Gradle Plugin.

```sh
./gradlew :app:assembleDebug
```

This command is a build entry point, not a recorded passing build. The project retains its original dependency versions, so Gradle sync or dependency updates may be needed with a current Android Studio installation.

## Current limitations

The source retains package references under `com.fabirt.podcastapp` alongside `com.mobiledv.podcastapp`; these imports need to be reconciled. The search repository also returns a failure after handling an API result, so successful online search should not be assumed. A passing build and playback/API acceptance run have not been established.
