plugins {
    id("soulsession.android.library")
    id("soulsession.android.library.compose")
    id("soulsession.android.library.jacoco")
    id("soulsession.spotless")
}


dependencies {

    implementation(libs.androidx.core.ktx)

    debugImplementation(libs.androidx.lifecycle.viewModelCompose)
    debugImplementation(libs.androidx.savedstate.ktx)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)

    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)

}