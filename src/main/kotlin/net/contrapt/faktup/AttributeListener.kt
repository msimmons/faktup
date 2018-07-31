package net.contrapt.faktup

interface AttributeListener {

    fun addDependency(input: ModelInput<*,*>)
    fun addMissing(missing: String)
}