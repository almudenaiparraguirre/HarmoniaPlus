plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.gms.google-services")
    id("org.jetbrains.dokka") version "1.9.10"
}
subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

android {
    namespace = "com.mariana.harmonia"
    compileSdk = 34

    buildFeatures{
        dataBinding = true
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.mariana.harmonia"
        minSdk = 25
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
    //Splash
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")

    //DOKKA
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.10")

    //otors
    implementation ("androidx.activity:activity-ktx:1.3.0")
    implementation ("androidx.fragment:fragment-ktx:1.3.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //TextAnim
    implementation("com.daimajia.androidanimations:library:2.4@aar")

    //KenBurnsView
    implementation("com.flaviofaria:kenburnsview:1.0.7")

    //Default
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.firebaseui:firebase-ui-storage:7.2.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")

    //Firebase Google
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")

    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.palette:palette:1.0.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:0.40")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}