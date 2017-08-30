-dontwarn java.awt.**,javax.security.**,java.beans.**,javax.swing.**
-dontwarn junit.**
-dontwarn android.test.**
-dontwarn com.squareup.**
-dontwarn org.w3c.dom.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn sun.misc.Unsafe

# We keep line nunmbers for easier debugging
-keepattributes LineNumberTable

-keepattributes *Annotation*,EnclosingMethod,Signature

#Keep all resources references
-keep class **.R$* { <fields>; }

#Do not obfuscate the Support Library
-keepnames class android.support.v4.app.** { *; }
-keepnames interface android.support.v4.app.** { *; }

#Do not obfuscate Glide
-keepnames class com.bumptech.** { *; }
-keepnames interface com.bumptech.** { *; }
#keep all the Glide modules, since they are only referenced in the manifest
-keep public class * implements com.bumptech.glide.module.GlideModule

#RxJava
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keepclassmembers class rx.internal.util.unsafe.** {
    long producerIndex;
    long consumerIndex;
}

# autovalue
-dontwarn javax.lang.**
-dontwarn javax.tools.**
-dontwarn javax.annotation.**
-dontwarn autovalue.shaded.com.**
-dontwarn com.google.auto.value.**

# autovalue gson extension
-keep class **.AutoParcelGson_*
-keepnames @auto.parcelgson.AutoParcelGson class *

#Dagger
-keep class javax.inject.*
-keep class dagger.*
-dontwarn com.google.errorprone.annotations.*

#Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#Keep Exceptions
-keepnames class * extends java.lang.Exception { *; }

