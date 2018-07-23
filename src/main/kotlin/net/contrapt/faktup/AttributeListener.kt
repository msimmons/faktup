package net.contrapt.faktup

import java.util.*
import kotlin.concurrent.getOrSet

class AttributeListener() {

    private val inputs = Stack<ModelInput<*, *>>()

    companion object {
        private val listeners = ThreadLocal<AttributeListener>()

        fun pushInput(input: ModelInput<*,*>) {
            listeners.getOrSet { AttributeListener() }.apply {
                inputs.push(input)
            }
        }

        fun popInput(input: ModelInput<*,*>) {
            listeners.getOrSet { AttributeListener() }.apply {
                if (!inputs.isEmpty()) {
                    inputs.pop()
                }
            }
        }

        fun addDependency(input: ModelInput<*,*>) {
            listeners.getOrSet { AttributeListener() }.apply {
                if (!inputs.isEmpty()) {
                    inputs.peek().dependencies.add(input)
                }
            }
        }

        fun addMissing(missing: String) {
            println("$missing is missing")
            listeners.getOrSet { AttributeListener() }.apply {
                if (!inputs.isEmpty()) {
                    inputs.peek().missing.add(missing)
                }
            }
        }
    }
}