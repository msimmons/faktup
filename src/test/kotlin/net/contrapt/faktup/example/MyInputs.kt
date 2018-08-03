package net.contrapt.faktup.example

import net.contrapt.faktup.*
import java.time.YearMonth

class MyInputs(attributes: MyContainer) {

    val mi1 by string { attributes.id }
    val mi2 by boolean { attributes.obj == null }
    val mi3 by obj { attributes.obj?.obj }
    val mi4 by string { mi1() }
    val mi5 by usd { N[34.10 / 80, 5] }
    val mi6 by boolean { YM["2018-08"] == YearMonth.now() }

    val r1 by obj {
        when (mi6()) {
            true -> MyObj("hello", mi5()?.toInt() ?: 0)
            else -> MyObj("goodbye", 0)
        }
    }

    data class MyObj(
        val foo: String,
        val bar: Int
    )
}