/**
 * Precompiled [soulsession.android.application.compose.gradle.kts][Soulsession_android_application_compose_gradle] script plugin.
 *
 * @see Soulsession_android_application_compose_gradle
 */
class Soulsession_android_application_composePlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Soulsession_android_application_compose_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
