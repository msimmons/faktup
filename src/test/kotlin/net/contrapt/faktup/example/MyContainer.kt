package net.contrapt.faktup.example

import net.contrapt.faktup.AttributeContainer
import net.contrapt.faktup.AttributeListener
import net.contrapt.faktup.ModelInput

class MyContainer(params: Map<String, Any?>) : AttributeContainer(name = "input", params = params), AttributeListener {

    val dependencies = mutableSetOf<ModelInput<*,*>>()
    val missing = mutableSetOf<String>()

    override fun addDependency(input: ModelInput<*, *>) {
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