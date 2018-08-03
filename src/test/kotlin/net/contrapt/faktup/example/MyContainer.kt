package net.contrapt.faktup.example

import net.contrapt.faktup.AttributeContainer
import net.contrapt.faktup.AttributeListener

class MyContainer(params: Map<String, Any?>) : AttributeContainer(name = "input", params = params), AttributeListener {

    val dependencies = mutableSetOf<String>()
    val missing = mutableSetOf<String>()

    override fun addDependency(input: String) {
        dependencies.add(input)
    }

    override fun addMissing(missing: String) {
        this.missing.add(missing)
    }

    init {
        AttributeContainer.pushListener(this)
    }

    val id by scalarValue<String>()
    val nums by scalarValue<List<Int>>()
    val obj by objectValue(::MyObjectValue)
}