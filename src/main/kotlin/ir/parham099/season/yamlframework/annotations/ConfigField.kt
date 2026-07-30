package ir.parham099.season.yamlframework.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigField(
    val customPath: String = ""
)
