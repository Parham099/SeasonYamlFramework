package processors

import annotations.Config
import annotations.ConfigField
import annotations.SubConfigObject
import models.FileYaml
import models.SeasonYaml
import java.io.File

object ConfigProcessor {
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

    private fun loadClassFields(`class`: Class<*>, yaml: SeasonYaml, yamlPath: String = "") {
        for (it in `class`.declaredFields) {
            val configField = it.getAnnotation(ConfigField::class.java) ?: continue
            var path = configField.customPath
            if (path.isEmpty()) {
                path = yamlPath + it.name
            }
            it.isAccessible = true
            it.set(`class`, yaml[path])
        }

        for (subClass in `class`.declaredClasses) {
            if (subClass.isSynthetic) continue
            val subConfigAnnotation = subClass.getAnnotation(SubConfigObject::class.java) ?: continue
            var yamlPath = subClass.simpleName
            if (!subConfigAnnotation.name.isEmpty()) {
                yamlPath = subConfigAnnotation.name
            }
            loadClassFields(
                `class` = subClass,
                yaml = yaml,
                yamlPath = "$yamlPath."
            )
        }
    }

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

    private fun saveClassFields(`class`: Class<*>, yaml: SeasonYaml, yamlPath: String = "") {
        for (it in `class`.declaredFields) {
            val configField = it.getAnnotation(ConfigField::class.java) ?: continue
            var path = configField.customPath
            if (path.isEmpty()) {
                path = yamlPath + it.name
            }
            it.isAccessible = true
            yaml[path] = it.get(`class`)
        }

        for (subClass in `class`.declaredClasses) {
            if (subClass.isSynthetic) continue
            val subConfigAnnotation = subClass.getAnnotation(SubConfigObject::class.java) ?: continue
            var yamlPath = subClass.simpleName
            if (!subConfigAnnotation.name.isEmpty()) {
                yamlPath = subConfigAnnotation.name
            }
            saveClassFields(
                `class` = subClass,
                yaml = yaml,
                yamlPath = "$yamlPath."
            )
        }
    }
}