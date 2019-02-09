package org.gakeutil.util.number

interface ClosedNumber<T> where T : Number, T : Comparable<T> {

    val value: T

    val MIN_VALUE: T

    val MAX_VALUE: T

    fun toDouble() = value.toDouble()

    fun toFloat() = value.toFloat()

    fun toLong() = value.toLong()

    fun toInt() = value.toInt()

    fun toChar() = value.toChar()

    fun toShort() = value.toShort()

    fun toByte() = value.toByte()

    operator fun compareTo(other: T) = this.value.compareTo(other)

}