package net.contrapt.faktup.example

import net.contrapt.faktup.boolean
import net.contrapt.faktup.string

class ModelInputs(attributes: MyContainer) {

    val mi1 by string { attributes.id }
    val mi2 by boolean { attributes.obj == null }
}