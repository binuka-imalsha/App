# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# WebView
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# WebView JavaScript interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep WebView classes
-keep class android.webkit.** { *; }
-keep class androidx.webkit.** { *; }

# Material Components
-keep class com.google.android.material.** { *; }

# Keep file upload related classes
-keep class android.net.Uri { *; }
-keep class java.io.File { *; }

# Dongle classes
-keep class com.donkeyplayer.app.** { *; }