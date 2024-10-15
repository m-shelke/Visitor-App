plugins {
    alias(libs.plugins.android.application)

    // Add the Google services Gradle plugin
    apply { ("com.google.gms.google-services") }
}

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
    implementation ("com.airbnb.android:lottie:4.0.0")

//    Circular imageview Dependency
    implementation ("de.hdodenhof:circleimageview:3.1.0")

//    Firebase Realtime Database Dependency

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    // Adding the dependencies for Firebase

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

//    Google Authentication via Firebase
    implementation("com.google.firebase:firebase-auth:23.0.0")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")


}