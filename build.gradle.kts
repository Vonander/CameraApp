
buildscript {
    val compose_version by extra("1.0.0-beta09")
    val camerax_version by extra("1.1.0-alpha07")
    val hilt_version by extra("2.37")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.37")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}