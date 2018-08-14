package net.contrapt.faktup.example

import net.contrapt.faktup.AttributeContainer

class MyObjectValue(name: String, params: Map<String, Any?>) : AttributeContainer(name, params) {

    val id: String? by simpleAttribute()
    val obj: NestedObjectValue? by objectAttribute(::NestedObjectValue)
}