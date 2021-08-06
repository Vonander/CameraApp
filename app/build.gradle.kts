plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.vonander.japancvcameraapp"
        minSdk = 26
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
    packagingOptions {
        exclude ("META-INF/gradle/incremental.annotation.processors")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")

    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.0-rc02")

    implementation("androidx.camera:camera-core:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-lifecycle:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-camera2:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-view:1.0.0-alpha27")

    implementation ("androidx.compose.material:material-icons-extended:${rootProject.extra["compose_version"]}")

    implementation ("com.google.mlkit:face-detection:16.1.2")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.android.support:support-fragment:28.0.0")
    implementation ("com.google.accompanist:accompanist-glide:0.10.0")

    implementation ("com.google.dagger:hilt-android-compiler:${rootProject.extra["hilt_version"]}")
    implementation ("com.google.dagger:hilt-android:${rootProject.extra["hilt_version"]}")
    implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    implementation ("androidx.hilt:hilt-navigation:1.0.0")
    kapt ("com.google.dagger:hilt-compiler:${rootProject.extra["hilt_version"]}")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")

    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha02")

    implementation ("androidx.datastore:datastore:${rootProject.extra["datastore_version"]}")
    implementation ("androidx.datastore:datastore-core:${rootProject.extra["datastore_version"]}")
    implementation ("androidx.datastore:datastore-preferences:${rootProject.extra["datastore_version"]}")

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
}