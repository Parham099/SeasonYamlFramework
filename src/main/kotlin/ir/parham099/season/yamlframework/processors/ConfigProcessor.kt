package ir.parham099.season.yamlframework.processors

import ir.parham099.season.yamlframework.annotations.Config
import ir.parham099.season.yamlframework.annotations.ConfigField
import ir.parham099.season.yamlframework.annotations.SubConfigObject
import ir.parham099.season.yamlframework.models.FileYaml
import ir.parham099.season.yamlframework.models.SeasonYaml
import java.io.File

/**
 * Processes configuration objects annotated with [Config].
 *
 * This processor is responsible for loading values from YAML files into
 * configuration objects and saving configuration objects back to YAML files.
 *
 * It recursively traverses nested configuration objects annotated with
 * [SubConfigObject] and processes all fields annotated with [ConfigField].
 */
object ConfigProcessor {
    /**
     * Loads the values of the specified configuration object from its YAML file.
     *
     * The target class must be annotated with [Config]. If the configuration
     * file does not exist, it will be created automatically.
     *
     * @param class the configuration object class to load.
     */
    fun loadYamlObject(`class`: Class<*>) {
        val configAnnotation = `class`.getAnnotation(Config::class.java) ?: return
        val filePath = configAnnotation.filePath
        val ymlFile = File(filePath)
        if (!ymlFile.exists()) {
            ymlFile.createNewFile()
        }
        val yaml = FileYaml(ymlFile)
        // save object and it subclasses
        loadClassFields(
            `class` = `class`,
            yaml = yaml
        )
    }

    /**
     * Recursively loads all configuration fields of the given class and its
     * nested configuration objects.
     *
     * Only fields annotated with [ConfigField] are processed. Nested classes
     * annotated with [SubConfigObject] are traversed recursively.
     *
     * @param class the current configuration class being processed.
     * @param yaml the YAML source.
     * @param yamlPath the current YAML path prefix.
     */
    private fun loadClassFields(`class`: Class<*>, yaml: SeasonYaml, yamlPath: String = "") {
        for (it in `class`.declaredFields) {
            val configField = it.getAnnotation(ConfigField::class.java) ?: continue
            val path = yamlPath + configField.customPath.ifEmpty {
                it.name
            }
            it.isAccessible = true
            it.set(`class`, yaml[path])
        }

        for (subClass in `class`.declaredClasses) {
            if (subClass.isSynthetic) continue
            val subConfigAnnotation = subClass.getAnnotation(SubConfigObject::class.java) ?: continue
            val nextYamlPath = yamlPath + subConfigAnnotation.name.ifEmpty {
                subClass.simpleName
            }

            loadClassFields(
                `class` = subClass,
                yaml = yaml,
                yamlPath = "$nextYamlPath."
            )
        }
    }

    /**
     * Saves the values of the specified configuration object into its YAML file.
     *
     * The target class must be annotated with [Config]. If the configuration
     * file does not exist, it will be created automatically.
     *
     * Existing YAML content is cleared before writing the current configuration.
     *
     * @param class the configuration object class to save.
     */
    fun saveYamlObject(`class`: Class<*>) {
        val configAnnotation = `class`.getAnnotation(Config::class.java) ?: return
        val filePath = configAnnotation.filePath
        val ymlFile = File(filePath)
        if (!ymlFile.exists()) {
            ymlFile.createNewFile()
        }
        val yaml = FileYaml(ymlFile)
        // remove old objects
        yaml.clear()
        // save object and it subclasses
        saveClassFields(
            `class` = `class`,
            yaml = yaml
        )
        // save the yml in file
        yaml.save()
    }

    /**
     * Recursively saves all configuration fields of the given class and its
     * nested configuration objects.
     *
     * Only fields annotated with [ConfigField] are written. Nested classes
     * annotated with [SubConfigObject] are traversed recursively.
     *
     * @param class the current configuration class being processed.
     * @param yaml the destination YAML object.
     * @param yamlPath the current YAML path prefix.
     */
    private fun saveClassFields(`class`: Class<*>, yaml: SeasonYaml, yamlPath: String = "") {
        for (it in `class`.declaredFields) {
            val configField = it.getAnnotation(ConfigField::class.java) ?: continue
            val path = yamlPath + configField.customPath.ifEmpty {
                it.name
            }
            it.isAccessible = true
            yaml[path] = it.get(`class`)
        }

        for (subClass in `class`.declaredClasses) {
            if (subClass.isSynthetic) continue
            val subConfigAnnotation = subClass.getAnnotation(SubConfigObject::class.java) ?: continue
            val nextYamlPath = yamlPath + subConfigAnnotation.name.ifEmpty {
                subClass.simpleName
            }

            saveClassFields(
                `class` = subClass,
                yaml = yaml,
                yamlPath = "$nextYamlPath."
            )
        }
    }
}