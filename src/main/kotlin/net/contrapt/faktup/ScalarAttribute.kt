package net.contrapt.faktup

import kotlin.reflect.KProperty

class ScalarAttribute<V> : Attribute() {

    operator fun provideDelegate(owner: AttributeContainer, kprop: KProperty<*>) : ScalarAttribute<V> {
        property = kprop
        parent = owner
        path = if (owner.name.isEmpty()) property.name else parent.name + "." + property.name
        owner.attributes.put(path, this)
        return this
    }

    operator fun getValue(owner: AttributeContainer, property: KProperty<*>) : V? {
        val value = owner.data[property.name] as V?
        if (value == null) {
            AttributeListener.addMissing(path)
        }
        return value
    }

    override fun toString(): String {
        return property.returnType.toString()
    }
}