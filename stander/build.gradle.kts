
/**
 *  1.apply plugin:代表着应用某个插件。
 *  其中com.android.application 表示是一个应用程序模块，
 *  com.android.library 表示是一个库模块，
 *  区别如下：com.android.application：可以直接运行
 *
 *  如果这里不配置成library，会提示不支持 dynamicFeatures
 *
 *  当 Android Studio 创建功能模块时，它会向基本模块的 build.gradle 文件添加 android.dynamicFeatures 属性，以
 *  使该功能模块对基本模块可见。
 */


plugins {
    id("com.android.library")
}



android {
    namespace = "com.plugin.stander"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        targetSdk = 36

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



}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}