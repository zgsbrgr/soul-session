/*
 * Copyright 2022 zgsbrgr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    id("soulsession.android.application")
    id("soulsession.android.application.compose")
    id("soulsession.android.application.jacoco")
    kotlin("kapt")
    id("jacoco")
    id("dagger.hilt.android.plugin")
    id("soulsession.spotless")
}

hilt {
    enableAggregatingTask = true
}

android {
    defaultConfig {
        applicationId = "com.zgsbrgr.soulsession"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.zgsbrgr.soulsession.core.testing.SoulSessionTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        val benchmark by creating {
            initWith(release)
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
            proguardFiles("benchmark-rules.pro")
        }
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(project(":core-ui"))
    implementation(project(":core-testing"))
    implementation(project(":core-network"))
    implementation(project(":core-navigation"))

    implementation(project(":feature-episodes"))
    implementation(project(":feature-episode"))



    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.activity.compose)
    debugApi(libs.androidx.compose.ui.tooling)


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)

    implementation(libs.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)

    implementation(libs.coil.kt)


    // androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
    configurations.configureEach {
        resolutionStrategy {
            force(libs.junit4)
            // Temporary workaround for https://issuetracker.google.com/174733673
            force("org.objenesis:objenesis:2.6")
        }
    }

}