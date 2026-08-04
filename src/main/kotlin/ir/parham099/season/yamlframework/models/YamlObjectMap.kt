package ir.parham099.season.yamlframework.models

/**
 * Represents a collection of objects mapped to a YAML section.
 *
 * Each key under [yamlPath] is associated with an instance of [T].
 * Instances are created using the first declared constructor of [clazz],
 * with constructor arguments populated from the corresponding YAML fields.
 *
 * The YAML source is provided when loading or saving, allowing multiple
 * YAML-backed objects to operate on the same [SeasonYaml] instance.
 *
 * @param T the type of objects stored in this map
 * @param yamlPath the path inside the YAML structure containing the objects
 * @param clazz the class of objects stored in this map
 */
class YamlObjectMap<T : Any>(
    val yamlPath: String,
    val clazz: Class<T>
) {
    private val items = hashMapOf<String, T>()

    /**
     * Loads all objects from the YAML section specified by [yamlPath].
     *
     * Each YAML entry is converted into an instance of [T] using the first
     * declared constructor of [clazz]. Constructor parameters are populated
     * using fields with matching names from the YAML object.
     *
     * Existing items with the same keys are replaced.
     *
     * @param yaml the YAML source containing the objects
     */
    fun loadAll(yaml: SeasonYaml) {
        val constructor = clazz.declaredConstructors.first()
        val fields = clazz.declaredFields

        val yamlObjects =
            yaml[yamlPath, mutableMapOf<String, LinkedHashMap<String, Any>>()]
                ?: return

        for ((key, yamlObject) in yamlObjects) {
            val invokeArgs = constructor.parameters.mapIndexed { index, _ ->
                val field = fields[index]
                yamlObject[field.name]
            }.toTypedArray()

            @Suppress("UNCHECKED_CAST")
            val instance = constructor.newInstance(*invokeArgs) as T

            items[key] = instance
        }
    }

    /**
     * Saves all objects currently stored in this map to the YAML structure.
     *
     * Each object is written under its associated key and [yamlPath].
     * This method only modifies the provided [yaml] instance; it does not
     * write the YAML data to a file.
     *
     * @param yaml the YAML destination to which the objects are written
     */
    fun saveAll(yaml: SeasonYaml) {
        for ((key, item) in items) {
            save(yaml, key, item)
        }
    }

    /**
     * Saves a single object to the YAML structure under the specified key.
     *
     * Each declared field of the object is written using the field name
     * as the YAML property name.
     *
     * This method only modifies the provided [yaml] instance; it does not
     * write the YAML data to a file.
     *
     * @param yaml the YAML destination to which the object is written
     * @param key the key used to identify the object in the YAML map
     * @param item the object to save
     */
    fun save(yaml: SeasonYaml, key: Any, item: T) {
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            yaml["$yamlPath.$key.${field.name}"] = field.get(item)
        }
    }

    /**
     * Returns the object associated with the specified key.
     *
     * @param key the key of the object
     * @return the associated object, or `null` if no object exists for the key
     */
    operator fun get(key: String): T? {
        return items[key]
    }

    /**
     * Associates the specified object with the specified key.
     *
     * If an object already exists for the specified key, it is replaced.
     *
     * @param key the key to associate with the object
     * @param value the object to store
     */
    operator fun set(key: String, value: T) {
        items[key] = value
    }

    /**
     * Returns all keys currently stored in this map.
     *
     * @return a set containing the keys of all stored objects
     */
    fun getKeys(): Set<String> {
        return items.keys
    }
}