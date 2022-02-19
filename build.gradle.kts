
buildscript {
    val compose_version by extra("1.1.0")
    val hilt_version by extra("2.37")
    val camerax_version by extra("1.1.0-alpha07")
    val datastore_version by extra("1.0.0")
    val retrofit_version by extra("2.9.0")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.39.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}