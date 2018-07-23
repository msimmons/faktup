package net.contrapt.faktup

import kotlin.reflect.KProperty

abstract class Attribute {

    protected lateinit var path: String
    protected lateinit var parent: AttributeContainer
    protected lateinit var property: KProperty<*>

}