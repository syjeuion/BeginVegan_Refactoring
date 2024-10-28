import java.io.FileInputStream
import java.util.Properties

plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KAPT)
//    id(Plugins.KSP)
}

android {
    namespace = "com.beginvegan.data"
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION

    val localProperties = Properties()
    localProperties.load(FileInputStream(rootProject.file("local.properties")))

    defaultConfig {
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION

        buildConfigField("String", "BASE_URL", localProperties.getProperty("BASE_URL"))

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
    kotlinOptions {
        jvmTarget = "11"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures{
        buildConfig = true
    }

}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-fcm"))

    // Hilt
    implementation(Dependencies.DAGGER_HILT)
    kapt(Dependencies.DAGGER_HILT_KAPT)

    // Retrofit
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_CONVERTER_MOSHI)

    // Moshi
    implementation(Dependencies.MOSHI)
    implementation(Dependencies.MOSHI_KOTLIN)
    implementation(Dependencies.MOSHI_ADAPTERS)
    kapt(Dependencies.MOSHI_KAPT)


    // Okhttp
    implementation(Dependencies.OKHTTP)
    implementation(Dependencies.OKHTTP_LOGGING_INTERCEPTOR)

    // Room
    implementation(Dependencies.ROOM_RUNTIME)
    implementation(Dependencies.ROOM_KTX)
    kapt(Dependencies.ROOM_KSP)
    implementation(Dependencies.ROOM_PAGING)

    // Timber
    implementation(Dependencies.TIMBER)


    implementation("com.github.skydoves:sandwich:2.0.8")
    implementation("com.github.skydoves:sandwich-retrofit:2.0.8") // For Retrofit (Android)

    //Coroutine
    implementation(Dependencies.KOTLINX_COROUTINES)

    // KaKao Login
    implementation(Dependencies.KAKAO_LOGIN)

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}