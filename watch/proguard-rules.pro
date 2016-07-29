# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wangxu/environment/android-sdk-macosx/tools/proguard/proguard-android.txt
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

# 混淆时,如果需要手机和wear进行通信,必须保证Google提供的通讯模块不被混淆
-keep class com.google.android.gms.wearable.**{*;}
-keep class com.google.android.gms.common.**{*;}