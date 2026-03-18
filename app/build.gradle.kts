plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.plugin.plguindemo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.plugin.plguindemo"
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

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //如果你正在一个应用模块中添加对另一个模块的依赖，并且这个模块是一个库模块，你可以使用以下方式添加
    implementation(project(":stander"))
}

}