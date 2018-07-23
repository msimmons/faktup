package net.contrapt.faktup.example

import net.contrapt.faktup.AttributeContainer

class MyObjectValue(name: String, params: Map<String, Any?>) : AttributeContainer(name, params) {

    val id: String? by scalarValue()
    val obj: NestedObjectValue? by objectValue(::NestedObjectValue)
}