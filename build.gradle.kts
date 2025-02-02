buildscript {
    extra.apply {
        set("kotlin_version", "1.9.0")
        set("compose_compiler_version", "1.5.1")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")  // Back to the working version
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    kotlin("android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}