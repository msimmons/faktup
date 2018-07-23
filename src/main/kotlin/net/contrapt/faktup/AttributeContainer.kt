package net.contrapt.faktup

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class AttributeContainer(val name: String = "", params: Map<String, Any?> = mapOf()) {

    @JsonIgnore
    val data = mutableMapOf<String, Any?>().withDefault {
        AttributeListener.addMissing(it)
        null
    }
    @JsonIgnore
    val attributes = mutableMapOf<String, Attribute>()

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
}