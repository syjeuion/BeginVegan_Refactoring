import java.io.FileInputStream
import java.util.Properties

plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KAPT)
    id(Plugins.SECRETS_GRADLE_PLUGIN)
    id(Plugins.SAFEARGS)
    id(Plugins.PARCELIZE)
    id(Plugins.DAGGER_HILT)
//    id(Plugins.KSP)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

val kakaoApiKey: String = localProperties.getProperty("KAKAO_API_KEY") ?: ""
val kakaoApiKeyTest: String = localProperties.getProperty("KAKAO_API_KEY_TEST") ?: ""


android {
    namespace = "com.beginvegan.presentation"
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION



    defaultConfig {
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["KAKAO_API_KEY"] = kakaoApiKey
        manifestPlaceholders["KAKAO_API_KEY_TEST"] = kakaoApiKeyTest

        buildConfigField("String", "KAKAO_API_KEY", "$kakaoApiKey")
        buildConfigField("String", "KAKAO_API_KEY_TEST", "$kakaoApiKeyTest")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
    composeOptions{
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}
//FCM
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.5")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core-fcm"))

    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.APP_COMPAT)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.CONSTRAINT_LAYOUT)
    implementation("androidx.activity:activity:1.8.0")

    // Retrofit
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_CONVERTER_MOSHI)

    // Moshi
    implementation(Dependencies.MOSHI)
    implementation("org.chromium.net:cronet-embedded:119.6045.31")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    kapt(Dependencies.MOSHI_KAPT)

    // Okhttp
    implementation(Dependencies.OKHTTP)
    implementation(Dependencies.OKHTTP_LOGGING_INTERCEPTOR)

    // Lifecycle
    implementation(Dependencies.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Dependencies.LIFECYCLE_RUNTIME_KTX)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL_KTX)

    // Coroutine
    implementation(Dependencies.COROUTINE_CORE)
    implementation(Dependencies.COROUTINE_ANDROID)

    // Coil
    implementation(Dependencies.COIL)

    // Recyclerview
    implementation(Dependencies.RECYCLERVIEW)

    // Navigation
    implementation(Dependencies.NAVIGATION_FRAGMENT_KTX)
    implementation(Dependencies.NAVIGATION_UI_KTX)

    // Room
    implementation(Dependencies.ROOM_RUNTIME)
    implementation(Dependencies.ROOM_KTX)
    kapt(Dependencies.ROOM_KSP)
    implementation(Dependencies.ROOM_PAGING)

    // Kotlin serialization
    implementation(Dependencies.KOTLIN_SERIALIZATION)

    // DataStore
    implementation(Dependencies.PREFERENCES_DATASTORE)

    // Paging
    implementation(Dependencies.PAGING)

    // WorkManager
    implementation(Dependencies.WORKMANGER)

    // Hilt
    implementation(Dependencies.DAGGER_HILT)
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    kapt(Dependencies.DAGGER_HILT_KAPT)

    // ViewModel delegate
    implementation(Dependencies.ACTIVITY_KTX)
    implementation(Dependencies.FRAGMENT_KTX)


    // Splash Screen
    implementation(Dependencies.SPLASH_SCREEN)

    // Lottie
    implementation(Dependencies.LOTTIE)

    // Timber
    implementation(Dependencies.TIMBER)

    // KaKao Login
    implementation(Dependencies.KAKAO_LOGIN)

    // Circle ImageView
    implementation(Dependencies.CIRCLE_IMAGEVIEW)

    // Rating bar
    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")

    // KAKAO MAP
    implementation("com.kakao.maps.open:android:2.9.5")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")


    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // indicator
    implementation("me.relex:circleindicator:2.1.6")

    // Image Cropper
    implementation("com.github.takusemba:cropme:2.0.8")

    //Coroutine
    implementation(Dependencies.KOTLINX_COROUTINES)

    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-video:1.3.4")

    implementation("androidx.camera:camera-view:1.3.4")
    implementation("androidx.camera:camera-extensions:1.3.4")

    //Compose
    implementation(Dependencies.COMPOSE_UI)
    implementation(Dependencies.COMPOSE_MATERIAL)
    implementation(Dependencies.COMPOSE_TOOLING)
    implementation(Dependencies.COMPOSE_ACTIVITY)
    implementation(Dependencies.COMPOSE_TOOLING_PREVIEW)
    implementation(Dependencies.COMPOSE_VIEWMODEL)
    implementation(platform(Dependencies.COMPOSE_BOM))
    debugImplementation(Dependencies.COMPOSE_TOOLING)
    debugImplementation(Dependencies.COMPOSE_MANIFEST)
}