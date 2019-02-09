package org.gakeutil.util

/**
 * A common abstract class of classes that has two int value.
 * Provides the four arithmetic operation mainly.
 * @see IntPos
 * @see IntScale
 */
abstract class Int2d<T : Int2d<T>> internal constructor (val f: Int, val s: Int) {

    abstract fun copy(f1: Int = f, s1: Int = s): T

    operator fun plus(other: Int2d<*>) = copy(f + other.f, s + other.s)

    operator fun minus(other: Int2d<*>) = copy(f - other.f, s - other.s)

    operator fun times(other: Int2d<*>) = copy(f * other.f, s * other.s)

    operator fun div(other: Int2d<*>) = copy(f / other.f, s / other.s)

    operator fun plus(other: Int) = copy(f + other, s + other)

    operator fun minus(other: Int) = copy(f - other, s - other)

    operator fun times(other: Int) = copy(f * other, s * other)

    operator fun div(other: Int) = copy(f / other, s / other)

    operator fun unaryMinus() = copy(-f, -s)

    override operator fun equals(other: Any?) = f == (other as? Int2d<*>)?.f && s == other.s

    override fun hashCode() = 31 * f + s

    fun min() = Math.min(f, s)

    fun max() = Math.max(f, s)

    override fun toString() = "${this::class.simpleName}($f, $s)"

}