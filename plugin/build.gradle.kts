plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.plugin.plugin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.plugin.plugin"
        minSdk = 26
        targetSdk = 36
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

    dynamicFeatures
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    /*
        implementation配置将依赖项限制在当前模块内部，不会传递给下游模块。
         api配置允许依赖项传递给下游模块
     */
    implementation(project(":stander"))
}