package net.contrapt.faktup

import kotlin.reflect.KProperty

abstract class Attribute {

    protected lateinit var path: String
    protected lateinit var parent: AttributeContainer
    protected lateinit var property: KProperty<*>

    fun initialize(owner: AttributeContainer, kprop: KProperty<*>) {
        property = kprop
        parent = owner
        path = if (owner.name.isEmpty()) property.name else parent.name + "." + property.name
        owner.addAttribute(path, this)
    }
}