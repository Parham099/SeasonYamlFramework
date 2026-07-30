package ir.parham099.season.yamlframework.annotations

/**
 * Marks a field as a configuration entry.
 *
 * Fields annotated with this annotation are automatically loaded from and
 * saved to the associated YAML configuration.
 *
 * If [customPath] is empty, the field name is used as the YAML path.
 *
 * @property customPath the custom YAML path of the field.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigField(
    val customPath: String = ""
)
