plugins {
    id("soulsession.android.library")
    id("soulsession.android.library.jacoco")
    id("kotlinx-serialization")
    alias(libs.plugins.ksp)
    id("soulsession.spotless")
}

dependencies {

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

}