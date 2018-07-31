package net.contrapt.faktup

import kotlin.reflect.KProperty

class ObjectAttribute<V: AttributeContainer>(private val factory: (String, Map<String, Any?>) -> V) : Attribute() {

    operator fun provideDelegate(owner: AttributeContainer, kprop: KProperty<*>) : ObjectAttribute<V> {
        initialize(owner, kprop)
        return this
    }

    operator fun getValue(owner: AttributeContainer, property: KProperty<*>) : V? {
        val value = owner[property.name]
        return when (value) {
            null -> {AttributeContainer.addMissing(path); null}
            is Map<*, *> -> factory(path, value as Map<String, Any?>)
            else -> throw IllegalArgumentException("Expecting a map for '${property.name}', got a '${value::class}'")
        }
    }

    override fun toString(): String {
        return getValue(parent, property).toString()
    }
}