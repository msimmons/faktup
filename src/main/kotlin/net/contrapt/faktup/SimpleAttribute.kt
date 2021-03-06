package net.contrapt.faktup

import kotlin.reflect.KProperty

class SimpleAttribute<V> : Attribute() {

    operator fun provideDelegate(owner: AttributeContainer, kprop: KProperty<*>) : SimpleAttribute<V> {
        initialize(owner, kprop)
        return this
    }

    operator fun getValue(owner: AttributeContainer, property: KProperty<*>) : V? {
        val value = owner[property.name] as V?
        if (value == null) {
            AttributeContainer.addMissing(path)
        }
        return value
    }

    override fun toString(): String {
        return property.returnType.toString()
    }
}