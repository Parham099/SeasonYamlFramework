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
        `class`.declaredFields.forEach {
            if (it.getAnnotation(ConfigField::class.java) != null) {
                it.isAccessible = true
                it.set(`class`, yaml[yamlPath + it.name])
            }
        }

        for (subClass in `class`.declaredClasses) {
            if (subClass.isSynthetic) continue
            subClass.getAnnotation(SubConfigObject::class.java) ?: continue
            loadClassFields(
                `class` = subClass,
                yaml = yaml,
                yamlPath = "${subClass.simpleName}."
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
        // save object and it subclasses
        saveClassFields(
            `class` = `class`,
            yaml = yaml
        )
        // save the yml in file
        yaml.save()
    }

    private fun saveClassFields(`class`: Class<*>, yaml: SeasonYaml, yamlPath: String = "") {
        `class`.declaredFields.forEach {
            if (it.getAnnotation(ConfigField::class.java) != null) {
                it.isAccessible = true
                yaml[yamlPath + it.name] = it.get(`class`)
            }
        }

        for (subClass in `class`.declaredClasses) {
            if (subClass.isSynthetic) continue
            subClass.getAnnotation(SubConfigObject::class.java) ?: continue
            saveClassFields(
                `class` = subClass,
                yaml = yaml,
                yamlPath = "${subClass.simpleName}."
            )
        }
    }
}