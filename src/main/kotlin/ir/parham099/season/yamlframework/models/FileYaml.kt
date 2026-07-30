package ir.parham099.season.yamlframework.models

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.collections.get

/**
 * A file-based implementation of [SeasonYaml].
 *
 * This implementation stores YAML data in memory and persists it to the
 * specified file when [save] is called.
 *
 * Values can be accessed using dot-separated paths (e.g. `"database.host"`).
 *
 * @property yamlFile the YAML file backing this instance.
 */
class FileYaml(val yamlFile: File) : SeasonYaml {
    private var yamlData: MutableMap<String, Any?> = LinkedHashMap<String, Any?>()
    private val options = DumperOptions().apply {
        defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
    }
    private var yaml = Yaml(options)

    init {
        load()
    }

    /**
     * Returns the value stored at the given path.
     *
     * If no value exists, [default] is returned.
     *
     * @param path the dot-separated YAML path.
     * @param default the value to return when the path does not exist.
     * @return the stored value or [default] or null.
     */
    override fun <T> get(path: String, default: T?): T? {
        return (getByPath(path) ?: default) as T?
    }

    /**
     * Stores the given value at the specified path.
     *
     * Missing intermediate sections are created automatically.
     *
     * @param path the dot-separated YAML path.
     * @param new the value to store.
     */
    override fun <T> set(path: String, new: T?) {
        setByPath(path, new)
    }

    /**
     * Removes the value stored at the given path.
     *
     * @param path the dot-separated YAML path.
     */
    override fun remove(path: String) {
        removeByPath(path)
    }

    /**
     * Removes the value stored at the given path.
     *
     * Equivalent to calling [remove].
     *
     * @param path the dot-separated YAML path.
     */
    override fun minusAssign(path: String) {
        remove(path)
    }

    /**
     * Removes all values currently loaded in memory.
     */
    override fun clear() {
        yamlData.clear()
    }

    /**
     * Writes the current YAML data to the backing file.
     */
    override fun save() {
        yamlFile.writeText(yaml.dump(yamlData))
    }

    /**
     * Reloads the YAML data from the backing file,
     * discarding any unsaved changes.
     */
    override fun reload() {
        load()
    }

    /**
     * Loads the YAML file into memory.
     */
    private fun load() {
        yamlData = yaml.load<LinkedHashMap<String, Any?>>(yamlFile.inputStream())
            ?: linkedMapOf()
    }

    /**
     * Returns the value stored at the given path.
     *
     * Supports traversing nested maps and lists using dot-separated paths.
     * List elements can be accessed by their numeric index.
     *
     * @param path the dot-separated YAML path.
     * @return the value at the path, or `null` if it does not exist.
     */
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

    /**
     * Stores a value at the given path.
     *
     * Missing intermediate map sections are created automatically.
     *
     * @param path the dot-separated YAML path.
     * @param value the value to store.
     */
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

    /**
     * Removes the value at the given path.
     *
     * Supports removing values from both maps and lists.
     *
     * @param path the dot-separated YAML path.
     * @return `true` if a value was removed, otherwise `false`.
     */
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