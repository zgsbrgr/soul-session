plugins {
    id("soulsession.android.library")
    kotlin("kapt")
    id("soulsession.spotless")
}


dependencies {

    implementation(project(":core-common"))
    implementation(project(":core-model"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    api(libs.junit4)
    api(libs.androidx.test.core)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)

    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.rules)
    api(libs.androidx.compose.ui.test)
    api(libs.hilt.android.testing)

    debugApi(libs.androidx.compose.ui.testManifest)

    configurations.configureEach {
        resolutionStrategy{
            // Temporary workaround for https://issuetracker.google.com/174733673
            force("org.objenesis:objenesis:2.6")
        }
    }
}