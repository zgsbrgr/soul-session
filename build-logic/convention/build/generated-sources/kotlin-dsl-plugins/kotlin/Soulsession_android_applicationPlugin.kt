/**
 * Precompiled [soulsession.android.application.gradle.kts][Soulsession_android_application_gradle] script plugin.
 *
 * @see Soulsession_android_application_gradle
 */
class Soulsession_android_applicationPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Soulsession_android_application_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
