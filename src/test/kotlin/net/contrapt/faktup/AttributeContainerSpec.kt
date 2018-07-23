package net.contrapt.faktup

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import net.contrapt.faktup.example.MyContainer
import org.junit.Test
import java.lang.IllegalArgumentException

class AttributeContainerSpec {

    @Test
    fun success() {
        val params = mapOf<String, Any?>("id" to "1", "obj" to mapOf<String, Any?>("id" to "2", "name" to "foo"))

        val container = MyContainer(params)
        container.id shouldBe "1"
        container.obj?.id shouldBe "2"
        println(container)
        println(ObjectMapper().writeValueAsString(container))
    }

    @Test
    fun badObject() {
        val params = mapOf<String, Any?>("id" to "1", "obj" to 3)

        val container = MyContainer(params)
        container.id shouldBe "1"
        shouldThrow<IllegalArgumentException> {
            container.obj?.id
        }
    }

    @Test
    fun nullObject() {
        val params = mapOf<String, Any?>("id" to "1", "obj" to null)

        val container = MyContainer(params)
        container.id shouldBe "1"
        container.obj?.id shouldBe null
        println(container)
        println(ObjectMapper().writeValueAsString(container))
    }

    @Test
    fun nestedObject() {
        val params = mapOf<String, Any?>("id" to "1", "obj" to mapOf("id" to "2", "obj" to mapOf("id" to "3")))

        val container = MyContainer(params)
        container.id shouldBe "1"
        container.obj?.id shouldBe "2"
        container.obj?.obj?.id shouldBe "3"
        println(container)
        println(ObjectMapper().writeValueAsString(container))
    }

    @Test
    fun scalarList() {
        val params = mapOf<String, Any?>("id" to "1", "nums" to listOf(1, 2, 3, 4, 5))
        val container = MyContainer(params)
        container.id shouldBe "1"
        container.obj?.id shouldBe null
        container.nums?.size shouldBe 5
        println(container)
        println(ObjectMapper().writeValueAsString(container))
    }
}