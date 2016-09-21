# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Programming\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class ru.katkalov.android.yamobdev2016.model.Artist { *; }
-keep class ru.katkalov.android.yamobdev2016.model.Cover { *; }

# Picasso
## https://github.com/square/picasso/ ##
-dontwarn com.squareup.okhttp.**

# Retrofit2
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okio.**

# GSON
# class:		 +# Gson uses generic type information stored in a class file when working with fields. Proguard
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }