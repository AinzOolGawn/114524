import com.android.build.gradle.internal.api.BaseVariantOutputImpl

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

val appVersionName = "1.1.0"
val appVersionCode = 7

android {
    namespace = "me.rhunk.snapenhance"
    compileSdk = 33

    buildFeatures {
        aidl = true
    }

    defaultConfig {
        applicationId = "me.rhunk.snapenhance"
        minSdk = 28
        //noinspection OldTargetApi
        targetSdk = 33

        versionCode = appVersionCode
        versionName = appVersionName
        multiDexEnabled = true
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    flavorDimensions += "abi"

    productFlavors {
        create("armv8") {
            ndk {
                abiFilters.add(name)
            }

            dimension = "abi"
        }

        create("armv7") {
            ndk {
                abiFilters.add(name)
            }
            packaging {
                jniLibs {
                    excludes += "**/*_neon.so"
                }
            }
            dimension = "abi"
        }
    }

    applicationVariants.all {
        outputs.map { it as BaseVariantOutputImpl }.forEach { variant ->
            variant.outputFileName = "app-${appVersionName}-${variant.name}.apk"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly(files("libs/LSPosed-api-1.0-SNAPSHOT.jar"))
    implementation(libs.coroutines)
    implementation(libs.kotlin.reflect)
    implementation(libs.recyclerview)
    implementation(libs.gson)
    implementation(libs.ffmpeg.kit)
    implementation(libs.osmdroid.android)
    implementation(libs.okhttp)
    implementation(libs.androidx.documentfile)

    implementation(project(":mapper"))
}


afterEvaluate {
    properties["debug_assemble_task"]?.let { tasks.named(it.toString()) }?.orNull?.doLast {
        runCatching {
            val apkDebugFile = android.applicationVariants.find { it.buildType.name == "debug" && it.flavorName == properties["debug_flavor"] }?.outputs?.first()?.outputFile ?: return@doLast
            exec {
                commandLine("adb", "shell", "am", "force-stop", "com.snapchat.android")
            }
            exec {
                commandLine("adb", "install", "-r", "-d", apkDebugFile.absolutePath)
            }
            exec {
                commandLine("adb", "shell", "am", "start", "com.snapchat.android")
            }
        }
    }
}