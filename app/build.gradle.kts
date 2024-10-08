import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {

    viewBinding {
        enable = true
    }
    namespace = "com.example.expenseease"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expenseease"
        minSdk = 26
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    val room_version = "2.6.1"
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation ("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation ("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.mlkit:text-recognition:16.0.0")
    implementation("androidx.room:room-runtime:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    kapt("androidx.room:room-compiler:$room_version")


        implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


}
