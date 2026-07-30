package ir.parham099.season.yamlframework.annotations

/**
 * Marks a class or object as a YAML configuration.
 *
 * The annotated class is processed by the configuration processor and mapped
 * to the YAML file specified by [filePath].
 *
 * @property filePath the path of the YAML configuration file.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Config(
    val filePath: String
)
