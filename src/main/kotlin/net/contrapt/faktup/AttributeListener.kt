package net.contrapt.faktup

interface AttributeListener {

    fun addDependency(input: String)
    fun addMissing(missing: String)
}