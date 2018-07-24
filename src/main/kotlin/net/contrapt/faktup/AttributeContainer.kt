package net.contrapt.faktup

abstract class AttributeContainer(val name: String = "", params: Map<String, Any?> = mapOf()) {

    private val data = mutableMapOf<String, Any?>().withDefault {
        null
    }

    private val attributes = mutableMapOf<String, Attribute>()

    init {
        data.putAll(params)
    }

    fun <V> scalarValue() : ScalarAttribute<V> {
        return ScalarAttribute<V>()
    }

    fun <V: AttributeContainer> objectValue(factory: (String, Map<String, Any?>) -> V) : ObjectAttribute<V> {
        return ObjectAttribute<V>(factory)
    }

    override fun toString(): String {
        return attributes.toString()
    }

    fun addAttribute(path: String, attribute: Attribute) = attributes.put(path, attribute)

    operator fun get(key: String) = data[key]

}