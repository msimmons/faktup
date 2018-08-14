package net.contrapt.faktup

import kotlin.reflect.KProperty

class CollectionAttribute<V: AttributeContainer>(private val factory: (String, Map<String, Any?>) -> V) : Attribute() {

    operator fun provideDelegate(owner: AttributeContainer, kprop: KProperty<*>) : CollectionAttribute<V> {
        initialize(owner, kprop)
        return this
    }

    operator fun getValue(owner: AttributeContainer, property: KProperty<*>) : List<V?>? {
        val value = owner[property.name]
        return when (value) {
            null -> {AttributeContainer.addMissing(path); null}
            is Collection<*> -> toAttributeCollection(value as Collection<*>)
            else -> throw IllegalArgumentException("Expecting a collection for '${property.name}', got a '${value::class}'")
        }
    }

    private fun toAttributeCollection(values: Collection<*>) : List<V?>? {
        return values.mapIndexed { ndx, it ->
            when (it) {
                null -> { AttributeContainer.addMissing("$path[$ndx]"); null }
                is Map<*, *> -> factory("$path[$ndx]", it as Map<String, Any?>)
                else -> throw IllegalArgumentException("Expecting a map for '${property.name}', got a '${it::class}'")
            }
        }
    }

    override fun toString(): String {
        return getValue(parent, property).toString()
    }
}