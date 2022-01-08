// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Plugins.kotlinGradlePlugin)
        classpath(Plugins.gradleAndroid)
        classpath(Plugins.safeArgs)
        classpath(Plugins.crashlyticsPlugin)
        classpath(Dagger.hiltGradlePlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version ("10.2.1")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

tasks.register("clean").configure {
    delete("build")
}
