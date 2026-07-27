package models

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File

class FileYaml(val yamlFile: File) : SeasonYaml {
    private var yamlData: MutableMap<String, Any?> = LinkedHashMap<String, Any?>()
    private val options = DumperOptions().apply {
        defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
    }
    private var yaml = Yaml(options)

    init {
        load()
    }

    override fun <T> get(path: String, default: T?): T? {
        return (getByPath(path) ?: default) as T?
    }

    override fun <T> set(path: String, new: T?) {
        setByPath(path, new)
    }

    override fun remove(path: String) {
        removeByPath(path)
    }

    override fun minusAssign(path: String) {
        remove(path)
    }

    override fun save() {
        yamlFile.writeText(yaml.dump(yamlData))
    }

    override fun reload() {
        load()
    }

    private fun load() {
        yamlData = yaml.load<LinkedHashMap<String, Any?>>(yamlFile.inputStream())
            ?: linkedMapOf()
    }

    private fun getByPath(path: String): Any? {
        try {
            val pathArray = path.split('.')
            var data: MutableMap<String, Any?> = yamlData
            for ((index, arg) in pathArray.withIndex()) {
                if (index != pathArray.lastIndex) {
                    data = data[arg] as MutableMap<String, Any?>
                } else {
                    return data[arg]
                }
            }
        } catch (exc: Exception) {
            println("Please report this error in project repository issues to fix it!")
            exc.printStackTrace()
        }

        return null
    }

    private fun setByPath(path: String, new: Any?) {
        val pathArray = path.split('.')
        var data: MutableMap<String, Any?> = yamlData
        for ((index, arg) in pathArray.withIndex()) {
            if (index != pathArray.lastIndex) {
                if (!data.containsKey(arg)) {
                    data[arg] = mutableMapOf<String, Any?>()
                }
                data = data[arg] as MutableMap<String, Any?>
            } else {
                data[arg] = new
            }
        }
    }

    private fun removeByPath(path: String) {
        val pathArray = path.split('.')
        var data: MutableMap<String, Any?> = yamlData
        for ((index, arg) in pathArray.withIndex()) {
            if (index != pathArray.lastIndex) {
                data = data[arg] as? MutableMap<String, Any?> ?: return
            } else {
                data.remove(arg)
            }
        }
    }
}