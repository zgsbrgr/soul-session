plugins {
    id("soulsession.android.library")
    id("soulsession.android.library.jacoco")
    kotlin("kapt")
    id("soulsession.spotless")
}



dependencies {

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(project(":core-testing"))
}