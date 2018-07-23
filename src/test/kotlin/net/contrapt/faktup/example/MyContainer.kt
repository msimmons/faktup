package net.contrapt.faktup.example

import net.contrapt.faktup.AttributeContainer

class MyContainer(params: Map<String, Any?>) : AttributeContainer(name = "input", params = params) {

    val id by scalarValue<String>()
    val nums by scalarValue<List<Int>>()
    val obj by objectValue(::MyObjectValue)
}