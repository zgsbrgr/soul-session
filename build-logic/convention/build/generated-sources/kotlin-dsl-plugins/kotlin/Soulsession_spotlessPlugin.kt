/**
 * Precompiled [soulsession.spotless.gradle.kts][Soulsession_spotless_gradle] script plugin.
 *
 * @see Soulsession_spotless_gradle
 */
class Soulsession_spotlessPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Soulsession_spotless_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
