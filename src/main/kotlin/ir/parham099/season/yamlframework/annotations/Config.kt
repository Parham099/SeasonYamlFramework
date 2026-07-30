package ir.parham099.season.yamlframework.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Config(
    val filePath: String
)
