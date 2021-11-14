import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Config.compileSdkVersion
    buildToolsVersion = Config.buildTools
    if (project.hasProperty("keystore.properties")) {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        signingConfigs {
            getByName("debug") {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(rootDir.absolutePath + keystoreProperties["storeFile"])
                storePassword = keystoreProperties["storePassword"].toString()
            }
            create("release") {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(rootDir.absolutePath + keystoreProperties["storeFile"])
                storePassword = keystoreProperties["storePassword"].toString()
            }
        }
    }

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName
        testInstrumentationRunner = Config.testInstrumentationRunner

        val API_KEY: String = gradleLocalProperties(rootDir).getProperty("API_KEY")
        val ALGOLIA_API_KEY: String = gradleLocalProperties(rootDir).getProperty("ALGOLIA_API_KEY")
        val ALGOLIA_APP_ID: String = gradleLocalProperties(rootDir).getProperty("ALGOLIA_APP_ID")
        val ALGOLIA_INDEX_NAME: String = gradleLocalProperties(rootDir).getProperty("ALGOLIA_INDEX_NAME")

        buildConfigField("String", "API_KEY", API_KEY)
        buildConfigField("String", "ALGOLIA_API_KEY", ALGOLIA_API_KEY)
        buildConfigField("String", "ALGOLIA_APP_ID", ALGOLIA_APP_ID)
        buildConfigField("String", "ALGOLIA_INDEX_NAME", ALGOLIA_INDEX_NAME)
        buildConfigField("String", "BASE_URL", "\"http://api.openweathermap.org/\"")

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
            correctErrorTypes = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (project.hasProperty("keystore.properties")) {
                signingConfig = signingConfigs.getByName("release")
            }
            isDebuggable = false
        }

        getByName("debug") {
            if (project.hasProperty("keystore.properties")) {
                signingConfig = signingConfigs.getByName("debug")
            }
            isDebuggable = true
        }
    }

    android {
        sourceSets {
            getByName("test").java.srcDir("src/sharedTest/java")
            getByName("androidTest").java.srcDir("src/sharedTest/java")
        }
    }

    hilt {
        enableAggregatingTask = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    testOptions {
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility(Config.javaVersion)
        targetCompatibility(Config.javaVersion)
    }

    tasks.withType().all {
        kotlinOptions {
            jvmTarget = Config.javaVersion.toString()
        }
    }

    packagingOptions {
        resources.excludes.add("**/attach_hotspot_windows.dll")
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Kotlin.stdlib)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.coreKtx)
    implementation(View.constraintLayout)
    implementation(AndroidX.legacySupport)

    // Material Design
    implementation(View.material)

    // Room
    implementation(Database.roomRuntime)
    kapt(Database.roomCompiler)
    implementation(Database.roomKtx)

    // Kotlin Coroutines
    implementation(Kotlin.coroutinesAndroid)

    // Navigation Components
    implementation(Navigation.navigationFragment)
    implementation(Navigation.navigationUi)

    // Retrofit
    implementation(Network.retrofit)
    implementation(Network.gsonConverter)
    implementation(Network.gson)

    // Preferences
    implementation(AndroidX.preferences)

    // Timber
    implementation(Utils.timber)

    // Weather Image
    implementation(Utils.weatherImage)

    // CalenderView
    implementation(Utils.calendarView)

    // Google Play Services
    implementation(Google.googlePlayGms)

    // Algolia Search
    implementation(Utils.algoliaSearch)

    // Lifecycle KTX
    implementation(AndroidX.viewModel)
    implementation(AndroidX.liveData)
    implementation(AndroidX.lifeCycleCommon)

    // Paging Library
    implementation(AndroidX.paging)

    // Elastic view
    implementation(Utils.elasticViews)

    // WorkManager
    implementation(AndroidX.workManager)

    // Dagger-Hilt
    implementation(Dagger.daggerHilt)
    kapt(Dagger.hiltCompiler)

    // OKHttp Logging Interceptor
    implementation(Network.okhttpInterceptor)

    // Chuck
    debugImplementation(Network.chuck)
    releaseImplementation(Network.chuckNoOp)

    // Firebase BoM, Crashlytics, Analytics
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.crashlytics)
    implementation(Firebase.analytics)

    // AndroidX Test - JVM testing
    testImplementation(AndroidX.testExt)
    testImplementation(AndroidX.coreKtxTest)
    testImplementation(AndroidX.archCoreTesting)
    testImplementation(UnitTest.junit)
    testImplementation(UnitTest.roboelectric)
    testImplementation(UnitTest.hamcrest)
    testImplementation(Kotlin.coroutineTest)
    testImplementation(UnitTest.mockitoCore)

    // AndroidX Test - Instrumented testing
    androidTestImplementation(UnitTest.mockitoCore)
    androidTestImplementation(AndroidX.testExt)
    androidTestImplementation(AndroidTest.espresso)
    androidTestImplementation(AndroidTest.espressoContrib)
    androidTestImplementation(AndroidTest.espressoIntent)
    androidTestImplementation(AndroidX.archCoreTesting)
    androidTestImplementation(AndroidX.coreKtxTest)
    androidTestImplementation(AndroidX.testRules)
    androidTestImplementation(Kotlin.coroutineTest)

    // Until the bug at https://issuetracker.google.com/128612536 is fixed
    debugImplementation(AndroidX.fragmentTesting)
    implementation(AndroidTest.idlingResource)
}
