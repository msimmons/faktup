package net.contrapt.faktup

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import kotlin.reflect.KProperty

abstract class ModelInput<V, A>(val type: String, val estimated: Boolean, private val formula: () -> V?) {

    var value: V? = null
        private set

    lateinit var name: String
        private set

    var exception: Throwable? = null

    val dependencies = mutableSetOf<ModelInput<*,*>>()
    val missing = mutableSetOf<String>()

    /**
     * Return the value of this model input
     */
    operator fun invoke() : V? {
        evaluate()
        return value
    }

    /**
     * Return the value of this model input or the provided default if it is null
     */
    operator fun invoke(default: V) : V {
        evaluate()
        return value ?: default
    }

    /**
     * Sugar for 'this MI is null'
     */
    operator fun not() = (this() == null)

    open operator fun provideDelegate(owner: Any, property: KProperty<*>) : ModelInput<V, A> {
        name = property.name
        inputs.put(name, this)
        return this
    }

    open operator fun getValue(owner: Any, property: KProperty<*>) : ModelInput<V, A> {
        evaluate()
        return this
    }

    private fun evaluate() {
        // Record dependency if there is a listener
        AttributeListener.addDependency(this)
        AttributeListener.pushInput(this)
        try {
            value = formula()
        }
        catch (e: Exception) {
            exception = e
        }
        // Remove this listener
        AttributeListener.popInput(this)
    }

    override fun toString(): String {
        return name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
            null -> false
            is ModelInput<*,*> -> other.name == name
            else -> false
        }
    }

    companion object {
        private val inputs = mutableMapOf<String, ModelInput<*,*>>()

        fun getInputs() : Set<ModelInput<*,*>> {
            return inputs.values.onEach { it.evaluate() }.toSet()
        }
    }
}

class StringInput(estimated: Boolean, formula: () -> String?) : ModelInput<String, String>("string", estimated, formula) {
}

class DecimalInput(estimated: Boolean, formula: () -> BigDecimal?) : ModelInput<BigDecimal, Number>("usd", estimated, formula) {
}

class IntegerInput(estimated: Boolean, formula: () -> Int?) : ModelInput<Int, Int>("int", estimated, formula) {
}

class BooleanInput(estimated: Boolean, formula: () -> Boolean?) : ModelInput<Boolean, Boolean>("bool", estimated, formula) {
}

class ObjectInput<T>(estimated: Boolean, formula: () -> T) : ModelInput<T, T>("object", estimated, formula)

fun string(estimated: Boolean = false, formula: () -> String?) : StringInput {
    return StringInput(estimated, formula)
}

fun usd(estimated: Boolean = false, formula: () -> BigDecimal?) : DecimalInput {
    return DecimalInput(estimated, formula)
}

fun boolean(estimated: Boolean = false, formula: () -> Boolean?) : BooleanInput {
    return BooleanInput(estimated, formula)
}

fun int(estimated: Boolean = false, formula: () -> Int?) : IntegerInput {
    return IntegerInput(estimated, formula)
}

fun <T> obj(estimated: Boolean = false, formula: () -> T) : ObjectInput<T> {
    return ObjectInput<T>(estimated, formula)
}

operator fun <T: Number> T?.plus(value: BigDecimal?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.plus(value)
}

operator fun <T: Number> T?.minus(value: BigDecimal?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.minus(value)
}

operator fun <T: Number> T?.times(value: BigDecimal?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.times(value)
}

operator fun <T: Number> T?.div(value: BigDecimal?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.div(value)
}

operator fun BigDecimal?.plus(value: Number?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.add(N%value)
}

operator fun BigDecimal?.minus(value: Number?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.subtract(N%value)
}

operator fun BigDecimal?.times(value: Number?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.multiply(N%value)
}

operator fun BigDecimal?.div(value: Number?) : BigDecimal? = when (value) {
    null -> null
    else -> this?.divide(N%value)
}

object N {
    operator fun rem(number: Number) : BigDecimal =  BigDecimal.valueOf(number.toDouble())
}

object D {
    operator fun rem(string: String) : LocalDate =  LocalDate.parse(string)
}

object M {
    operator fun rem(string: String) : YearMonth =  YearMonth.parse(string)
}

object I {
    operator fun rem(string: String) : Instant =  Instant.parse(string)
}

infix fun Number.EQ(bd: BigDecimal?) : Boolean = BigDecimal.valueOf(this.toDouble()) == bd
infix fun BigDecimal.EQ(n: Number?) : Boolean = this == (if (n == null) null else BigDecimal.valueOf(n.toDouble()))

// No nulls allowed in comparisons because there is no way of signaling a null
operator fun Number.compareTo(bd: BigDecimal) : Int = BigDecimal.valueOf(this.toDouble()).compareTo(bd)
operator fun BigDecimal.compareTo(n: Number) : Int = this.compareTo(BigDecimal.valueOf(n.toDouble()))

operator fun String.compareTo(date: LocalDate) : Int =  LocalDate.parse(this).compareTo(date)
operator fun String.compareTo(date: YearMonth) : Int =  YearMonth.parse(this).compareTo(date)
operator fun String.compareTo(date: Instant) : Int =  Instant.parse(this).compareTo(date)

operator fun LocalDate.compareTo(dateStr: String) : Int =  this.compareTo(LocalDate.parse(dateStr))
operator fun YearMonth.compareTo(dateStr: String) : Int =  this.compareTo(YearMonth.parse(dateStr))
operator fun Instant.compareTo(dateStr: String) : Int =  this.compareTo(Instant.parse(dateStr))