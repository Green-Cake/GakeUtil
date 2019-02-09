package org.gakeutil.util.number


/**
 *
 * @constructor
 *
 * @param value the initialize value.
 */
class OverflowableClosedInt(value: Int, range: IntRange) : ClosedInt(
    overflow(
        value,
        range
    ), range) {

    companion object {

        fun overflow(value: Int, range: IntRange) = when {
            value in range -> value
            value < range.endInclusive -> range.endInclusive + 1 + (value % range.endInclusive)
            else -> range.start - 1 + (value % range.endInclusive)
        }

    }

    override operator fun plus(other: Number) = copy(value + other.toInt())

    override operator fun minus(other: Number) = copy(value - other.toInt())

    override operator fun times(other: Number) = copy(value * other.toInt())

    override operator fun div(other: Number) = copy(value / other.toInt())

    override operator fun rem(other: Number) = copy(value % other.toInt())

    override operator fun inc() = copy(this.value + 1)

    override operator fun dec() = copy(this.value - 1)


    override fun copy(v: Int) = OverflowableClosedInt(v, range)

}