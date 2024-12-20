import com.android.tools.r8.internal.id

plugins {
    alias(libs.plugins.android.application)
}

//apply Google Gradle Plugin here
apply { plugin("com.google.gms.google-services") }

//apply{ plugin ("com.android.application") }

android {
    namespace = "com.example.visitorapp"
    compileSdk = 34

//    Enabling ViewBinding here
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.visitorapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(kotlin("script-runtime"))

//    Lottie animation Dependency
    implementation("com.airbnb.android:lottie:4.0.0")

//    Circular imageview Dependency
    implementation("de.hdodenhof:circleimageview:3.1.0")

//    Firebase Realtime Database Dependency

    // Adding the dependencies for Firebase

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

//    Google Authentication via Firebase
    implementation("com.google.firebase:firebase-auth")

//    Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")

//    Firebase firestore dependency
    implementation("com.google.firebase:firebase-firestore:25.1.0")

    //    Authentication with Credential Manager

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

//    Glide Library dependency
    implementation("com.github.bumptech.glide:glide:4.14.2")
//     Skip this if you don't want to use integration libraries or configure Glide.
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

//    GIF dependency
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")

//    KProgressHUD Dependency
    implementation ("io.github.rupinderjeet:kprogresshud:1.0.0")

//    Google AdMob Dependency
    implementation("com.google.android.gms:play-services-ads:23.3.0")

//    Check Kotlin Version Compatibility
    implementation ("org.jetbrains.kotlin:kotlin-script-runtime:2.0.21")


}
