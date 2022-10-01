# Add project specific ProGuard rules here.

-keepattributes SourceFile,LineNumberTable

-keepclassmembernames class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# Keep Project Data classes
-keep class com.app.general.pixage.images.domain.entity.** { *; }
-keep class com.app.general.pixage.images.data.local.model.** { *; }
-keep class com.app.general.pixage.images.data.remote.model.** { *; }

# Retrofit
-keepclasseswithmembers class * {
    @retrofit2.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclassmembers class * extends androidx.lifecycle.EmptyActivityLifecycleCallbacks { *; }
-keepclassmembers class androidx.lifecycle.ReportFragment$** { *; }
-keep class androidx.lifecycle.** { *; }