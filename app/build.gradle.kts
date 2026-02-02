plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.pzois.uniwa.android.multiplechoicetest"
    compileSdk = 36 // Σκέτο 35, χωρίς παρενθέσεις ή "release"

    defaultConfig {
        applicationId = "org.pzois.uniwa.android.multiplechoicetest"
        minSdk = 25
        targetSdk = 36 // Βάλτο ίδιο με το compileSdk
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
}