package net.contrapt.faktup.example

import net.contrapt.faktup.AttributeContainer

class NestedObjectValue(name: String, params: Map<String, Any?>) : AttributeContainer(name, params) {

    val id: String? by scalarValue()
}