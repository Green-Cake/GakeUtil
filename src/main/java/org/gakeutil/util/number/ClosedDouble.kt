package org.gakeutil.util.number

import org.gakeutil.fitIn
import java.io.Serializable

open class ClosedDouble(value: Double, range: ClosedFloatingPointRange<Double>) : ClosedNumber<Double>, Serializable {

    final override val value = value fitIn range

    override val MIN_VALUE = range.start

    override val MAX_VALUE = range.endInclusive

    val range get() = MIN_VALUE..MAX_VALUE

    val isCountStopped get() = value == MAX_VALUE

    val isCountStoppedLower get() = value == MIN_VALUE

    //

    open operator fun plus(other: Number) = copy(value + other.toInt())

    open operator fun minus(other: Number) = copy(value - other.toInt())

    open operator fun times(other: Number) = copy(value * other.toInt())

    open operator fun div(other: Number) = copy(value / other.toInt())

    open operator fun rem(other: Number) = copy(value % other.toInt())

    open operator fun inc() = copy(this.value + 1)

    open operator fun dec() = copy(this.value - 1)

    open operator fun compareTo(other: ClosedInt) = this.value.compareTo(other.value)

    override fun equals(other: Any?) = value == when(other) {
        is Number -> other
        is ClosedNumber<*> -> other.value
        else -> null
    }

    open fun copy(v: Double) = ClosedDouble(v, range)

    override fun toString() = value.toString()

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + MIN_VALUE.hashCode()
        result = 31 * result + MAX_VALUE.hashCode()
        return result
    }

}