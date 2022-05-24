plugins {
    id("soulsession.android.library")
    id("soulsession.android.library.jacoco")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("soulsession.spotless")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    propertiesFileName = "local.properties"
    defaultPropertiesFileName="secrets.defaults.properties"
}

dependencies {

    implementation(project(":core-common"))
    implementation(project(":core-model"))

    testImplementation(project(":core-testing"))


    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)

    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}