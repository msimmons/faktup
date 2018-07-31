package net.contrapt.faktup.example

import net.contrapt.faktup.boolean
import net.contrapt.faktup.obj
import net.contrapt.faktup.string

class MyInputs(attributes: MyContainer) {

    val mi1 by string { attributes.id }
    val mi2 by boolean { attributes.obj == null }
    val mi3 by obj { attributes.obj?.obj }
}