package org.gakeutil.util

import java.io.Serializable

/**
 * A common abstract class of classes that has two double value.
 * Provides the four arithmetic operation mainly.
 * @see Position
 * @see Scale
 */
abstract class Double2d<T : Double2d<T>> protected constructor (val f: Double, val s: Double): Serializable {

    abstract fun copy(f1: Double = f, s1: Double = s): T

    operator fun plus(other: Double2d<*>) = copy(f + other.f, s + other.s)

    operator fun minus(other: Double2d<*>) = copy(f - other.f, s - other.s)

    operator fun times(other: Double2d<*>) = copy(f * other.f, s * other.s)

    operator fun div(other: Double2d<*>) = copy(f / other.f, s / other.s)

    operator fun rem(other: Double2d<*>) = copy(f % other.f, s % other.s)

    operator fun plus(other: Double) = copy(f + other, s + other)

    operator fun minus(other: Double) = copy(f - other, s - other)

    operator fun times(other: Double) = copy(f * other, s * other)

    operator fun div(other: Double) = copy(f / other, s / other)

    operator fun rem(other: Double) = copy(f % other, s % other)

    operator fun unaryMinus() = copy(-f, -s)

    override fun equals(other: Any?) = f == (other as? Double2d<*>)?.f && s == other.s

    override fun hashCode() = 31 * f.hashCode() + s.hashCode()

    fun min() = Math.min(f, s)

    fun max() = Math.max(f, s)

    override fun toString() = "${this::class.simpleName}($f, $s)"

    fun toSimpleString() = "($f, $s)"

}