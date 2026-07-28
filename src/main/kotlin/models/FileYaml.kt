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

    override fun clear() {
        yamlData.clear()
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
        var current: Any? = yamlData

        for (part in path.split('.')) {
            current = when (current) {
                is Map<*, *> -> current[part]

                is List<*> -> {
                    val index = part.toIntOrNull() ?: return null
                    current.getOrNull(index)
                }

                else -> return null
            }
        }

        return current
    }

    private fun setByPath(path: String, value: Any?) {
        val parts = path.split('.')
        var current = yamlData as MutableMap<String, Any?>

        for (i in 0 until parts.lastIndex) {
            val key = parts[i]

            val next = current[key]
            if (next !is MutableMap<*, *>) {
                val newMap = mutableMapOf<String, Any?>()
                current[key] = newMap
                current = newMap
            } else {
                @Suppress("UNCHECKED_CAST")
                current = next as MutableMap<String, Any?>
            }
        }

        current[parts.last()] = value
    }

    private fun removeByPath(path: String): Boolean {
        val parts = path.split('.')
        var current: Any? = yamlData

        for (i in 0 until parts.lastIndex) {
            current = when (current) {
                is MutableMap<*, *> -> current[parts[i]]
                is MutableList<*> -> {
                    val index = parts[i].toIntOrNull() ?: return false
                    current.getOrNull(index)
                }

                else -> return false
            }
        }

        return when (current) {
            is MutableMap<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                (current as MutableMap<String, Any?>).remove(parts.last()) != null
            }

            is MutableList<*> -> {
                val index = parts.last().toIntOrNull() ?: return false

                @Suppress("UNCHECKED_CAST")
                val list = current as MutableList<Any?>
                if (index !in list.indices) return false
                list.removeAt(index)
                true
            }

            else -> false
        }
    }
}