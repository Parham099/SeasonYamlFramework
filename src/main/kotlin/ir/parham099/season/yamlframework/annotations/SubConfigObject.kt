package ir.parham099.season.yamlframework.annotations

/**
 * Marks a nested class or object as a sub-configuration section.
 *
 * The annotated class is processed recursively as part of its parent
 * configuration.
 *
 * If [name] is empty, the simple name of the class is used as the YAML section
 * name.
 *
 * @property name the YAML section name.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubConfigObject(
    val name: String = ""
)
