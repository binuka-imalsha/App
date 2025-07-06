# Donkey Player - Android App for Donkey.to

A modern Android application that provides an optimized mobile experience for the Donkey.to website, featuring ad blocking, subtitle upload support, and a beautiful Material 3 design following Android 15 principles.

## Features

### 🎬 Core Functionality
- **Full WebView Integration** - Seamless access to Donkey.to website
- **Online Web Player** - Full support for video streaming and playback
- **Subtitle Upload** - Upload subtitle files (.srt, .vtt, .ass) directly from your device
- **Mobile Optimized** - Responsive design that adapts to phone displays

### 🛡️ Ad Blocking & Privacy
- **Built-in Ad Blocker** - Blocks popup ads and unwanted content automatically
- **Domain-based Filtering** - Blocks known ad networks and tracking domains
- **CSS Injection** - Removes ad elements from the webpage
- **Privacy Protection** - No tracking or data collection

### 🎨 Modern UI/UX
- **Material 3 Design** - Following Google's latest design guidelines
- **Android 15 Styling** - Modern look and feel matching Android 15
- **Dynamic Theming** - Supports both light and dark themes
- **Edge-to-Edge Display** - Immersive full-screen experience
- **Smooth Animations** - Fluid transitions and loading animations

### 📱 Mobile Features
- **Swipe to Refresh** - Pull down to reload the page
- **Back Navigation** - Hardware back button support
- **Loading Progress** - Visual progress indicator while pages load
- **File Upload Support** - Choose files from device storage
- **Responsive Layout** - Optimized for all screen sizes

## Installation

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK API level 24 or higher
- Kotlin 1.9.10+
- Gradle 8.2+

### Building the App

1. **Clone or download** this project to your local machine

2. **Set up Android SDK path** in `local.properties`:
   ```properties
   sdk.dir=/path/to/your/android/sdk
   ```

3. **Open the project** in Android Studio

4. **Sync Gradle** dependencies (Android Studio should prompt you)

5. **Build and run** the app:
   - Connect an Android device or start an emulator
   - Click the "Run" button or use `Ctrl+R` (Windows/Linux) or `Cmd+R` (Mac)

### Direct APK Installation
You can also build the APK directly using Gradle:

```bash
./gradlew assembleDebug
```

The APK will be generated in `app/build/outputs/apk/debug/`

## Usage

### First Launch
1. **Permissions** - The app will request storage permissions for file uploads
2. **Loading** - The app automatically loads the Donkey.to website
3. **Navigation** - Use the toolbar buttons to navigate

### Navigation Controls
- **Back Button** - Navigate back through page history
- **Home Button** - Return to Donkey.to homepage  
- **Refresh Button** - Reload the current page
- **Swipe Down** - Pull-to-refresh gesture

### Uploading Subtitles
1. Navigate to a video page on Donkey.to
2. Look for file upload options on the website
3. Tap the upload button
4. Select subtitle files from your device (.srt, .vtt, .ass formats supported)
5. The file will be uploaded to the website

### Ad Blocking
The app automatically:
- Blocks requests to known ad domains
- Hides ad elements using CSS injection
- Removes popup overlays
- Filters unwanted content

## Technical Details

### Architecture
- **Target SDK**: Android 34 (Android 14)
- **Minimum SDK**: Android 24 (Android 7.0)
- **Language**: Kotlin
- **UI Framework**: Material Design 3
- **Web Engine**: Android WebView with androidx.webkit

### Key Components
- **WebView**: Custom implementation with ad blocking
- **File Provider**: Secure file sharing for uploads
- **Material Toolbar**: Modern navigation interface
- **SwipeRefreshLayout**: Pull-to-refresh functionality
- **Progress Indicators**: Loading animations

### Permissions
- `INTERNET` - Required for web browsing
- `ACCESS_NETWORK_STATE` - Check network connectivity
- `READ_EXTERNAL_STORAGE` - Access files for upload (Android 12 and below)
- `READ_MEDIA_VIDEO` - Access media files (Android 13+)

## Customization

### Changing Colors
Edit `app/src/main/res/values/colors.xml` to customize the app's color scheme:

```xml
<color name="primary">#6750A4</color>
<color name="on_primary">#FFFFFF</color>
```

### Modifying Ad Blocking
Update the ad domain list in `MainActivity.kt`:

```kotlin
private fun isAdUrl(url: String): Boolean {
    val adDomains = listOf(
        "your-ad-domain.com",
        // Add more domains here
    )
    return adDomains.any { url.contains(it, ignoreCase = true) }
}
```

### Adding Features
The app is built with extensibility in mind. You can easily add:
- Download manager integration
- Bookmark functionality
- History tracking
- Settings screen
- Custom user agents

## Troubleshooting

### Common Issues

**App won't load the website:**
- Check internet connection
- Verify Donkey.to is accessible in your region
- Try refreshing the page

**File upload not working:**
- Ensure storage permissions are granted
- Check if the file format is supported
- Try using a different file manager

**Videos not playing:**
- Enable JavaScript in WebView (already enabled by default)
- Check if the device supports the video format
- Ensure stable internet connection

**Ads still showing:**
- The ad blocker targets common ad networks
- Some ads may still appear if they use different domains
- You can update the ad domain list in the code

## Development

### Project Structure
```
app/
├── src/main/
│   ├── java/com/donkeyplayer/app/
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   ├── values/
│   │   ├── drawable/
│   │   └── xml/
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is for educational and personal use only. Please respect the terms of service of Donkey.to and any applicable laws in your jurisdiction.

## Disclaimer

This app is not affiliated with or endorsed by Donkey.to. It's a third-party client that provides a mobile interface to the website. Users are responsible for complying with the website's terms of service and any applicable laws.

## Support

For issues and questions:
- Check the troubleshooting section above
- Review the Android Studio build logs
- Ensure all dependencies are properly installed
- Test on different devices and Android versions

---

**Enjoy your enhanced mobile streaming experience!** 🎬📱