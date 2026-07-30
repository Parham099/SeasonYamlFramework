package ir.parham099.season.yamlframework.models

/**
 * Represents a YAML document that supports reading, writing and managing values
 * using dot-separated paths.
 *
 * Implementations are responsible for handling persistence and serialization.
 */
interface SeasonYaml {

    /**
     * Returns the value stored at the given path.
     *
     * If no value exists, [default] is returned.
     *
     * @param path the dot-separated YAML path.
     * @param default the value to return when the path does not exist.
     * @return the stored value, or [default] if no value exists.
     */
    operator fun <T> get(path: String, default: T? = null): T?

    /**
     * Stores a value at the given path.
     *
     * Intermediate sections may be created automatically if required.
     *
     * @param path the dot-separated YAML path.
     * @param new the value to store.
     */
    operator fun <T> set(path: String, new: T?)

    /**
     * Removes the value stored at the given path.
     *
     * @param path the dot-separated YAML path.
     */
    fun remove(path: String)

    /**
     * Removes the value stored at the given path.
     *
     * Equivalent to calling [remove].
     *
     * @param path the dot-separated YAML path.
     */
    operator fun minusAssign(path: String)

    /**
     * Removes all values from this YAML document.
     */
    fun clear()

    /**
     * Persists all current changes to the underlying storage.
     */
    fun save()

    /**
     * Reloads the YAML document from the underlying storage,
     * discarding any unsaved changes.
     */
    fun reload()
}