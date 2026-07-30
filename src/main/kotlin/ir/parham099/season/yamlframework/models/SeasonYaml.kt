package ir.parham099.season.yamlframework.models

interface SeasonYaml {
    operator fun <T> get(path: String, default: T? = null): T?
    operator fun <T> set(path: String, new: T?)
    fun remove(path: String)
    operator fun minusAssign(path: String)
    fun clear()
    fun save()
    fun reload()
}