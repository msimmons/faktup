package net.contrapt.faktup

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import net.contrapt.faktup.example.MyContainer
import net.contrapt.faktup.example.MyInputs
import org.junit.Test

class ModelInputSpec {

    @Test
    fun success() {
        val params = mapOf<String, Any?>("id" to "1", "obj" to mapOf<String, Any?>("id" to "2", "name" to "foo"))
        val container = MyContainer(params)
        val mi = MyInputs(container)
        mi.mi1() shouldBe container.id
        mi.mi2() shouldBe false
        !mi.mi3 shouldBe true
        mi.mi3()?.id shouldBe null

        println(ObjectMapper().writeValueAsString(mi))
    }

}