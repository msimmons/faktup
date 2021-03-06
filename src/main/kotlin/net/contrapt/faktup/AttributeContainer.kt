package net.contrapt.faktup

import java.util.*
import kotlin.concurrent.getOrSet

abstract class AttributeContainer(val name: String = "", params: Map<String, Any?> = mapOf()) {

    private val data = mutableMapOf<String, Any?>().withDefault {
        null
    }

    private val attributes = mutableMapOf<String, Attribute>()

    init {
        data.putAll(params)
    }

    fun <V> simpleAttribute() : SimpleAttribute<V> {
        return SimpleAttribute<V>()
    }

    fun <V: AttributeContainer> objectAttribute(factory: (String, Map<String, Any?>) -> V) : ObjectAttribute<V> {
        return ObjectAttribute<V>(factory)
    }

    fun <V: AttributeContainer> objectList(factory: (String, Map<String, Any?>) -> V) : CollectionAttribute<V> {
        return CollectionAttribute<V>(factory)
    }

    override fun toString(): String {
        return attributes.toString()
    }

    fun addAttribute(path: String, attribute: Attribute) = attributes.put(path, attribute)

    operator fun get(key: String) = data[key]

    companion object {
        private val listeners = ThreadLocal<Stack<AttributeListener>>()

        fun pushListener(listener: AttributeListener) {
            listeners.getOrSet { Stack() }.apply {
                push(listener)
            }
        }

        fun popListener(listener: AttributeListener) {
            listeners.getOrSet { Stack() }.apply {
                if (!isEmpty()) {
                    pop()
                }
            }
        }

        fun addDependency(input: String) {
            listeners.getOrSet { Stack() }.apply {
                if (!isEmpty()) {
                    peek().addDependency(input)
                }
            }
        }

        fun addMissing(missing: String) {
            listeners.getOrSet { Stack() }.apply {
                if (!isEmpty()) {
                    peek().addMissing(missing)
                }
            }
        }
    }
}