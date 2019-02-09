package org.gakeutil.util

import javafx.geometry.Point2D
import org.gakeutil.angleOf
import org.gakeutil.posOf
import java.io.Serializable
import kotlin.math.abs

class IntPos internal constructor (x: Int, y: Int) : Int2d<IntPos>(x, y), Serializable {

    companion object {

        val ZERO = IntPos(0, 0)

    }

    val x get() = f
    val y get() = s

    override fun copy(f1: Int, s1: Int) = IntPos(f1, s1)

    fun toDoublePos() = posOf(this)

    fun toPoint() = Point2D(x.toDouble(), y.toDouble())

    infix fun distance2dTo(other: IntPos) = IntPos(other.x - x, other.y - y)

    infix fun absDistance2dTo(other: IntPos) = IntPos(abs(other.x - x), abs(other.x - x))

    infix fun distanceTo(other: IntPos): Double {

        val tmp0 = other.x - x

        val tmp1 = other.y - y

        return Math.sqrt((tmp0 * tmp0 + tmp1 * tmp1).toDouble())

    }

    infix fun absDistanceTo(other: IntPos) = abs(distanceTo(other))

    /**
     * @param other Target position.
     *
     * @return [Angle] which looking from this to [other].
     */
    infix fun lookAt(other: IntPos): Angle {

        val a = other - this
        return angleOf(Math.toDegrees(Math.atan2(a.x.toDouble(), a.y.toDouble())))

    }

    /**
     * @param others comparing targets. Must be more than 0.
     *
     * @return The nearest one.
     *
     * @throws IllegalArgumentException If `others.isEmpty()`
     */
    @Throws(IllegalArgumentException::class)
    fun getNearest(vararg others: IntPos): IntPos = if(others.isEmpty())
        throw IllegalArgumentException("others must be more than 0.")
    else
        others.sortedBy { this.absDistanceTo(it) }.first()

    /**
     * @param others comparing targets. Must be more than 0.
     *
     * @return The farthest one.
     *
     * @throws IllegalArgumentException If `others.isEmpty()`
     */
    @Throws(IllegalArgumentException::class)
    fun getFarthest(vararg others: IntPos): IntPos = if(others.isEmpty())
        throw IllegalArgumentException("others must be more than 0.")
    else
        others.sortedBy { this.absDistanceTo(it) }.last()

    infix fun plusX(other: Int) = copy(f1 = x + other)
    infix fun minusX(other: Int) = copy(f1 = x - other)
    infix fun timesX(other: Int) = copy(f1 = x * other)
    infix fun divX(other: Int) = copy(f1 = x / other)

    infix fun plusY(other: Int) = copy(s1 = y + other)
    infix fun minusY(other: Int) = copy(s1 = y - other)
    infix fun timesY(other: Int) = copy(s1 = y * other)
    infix fun divY(other: Int) = copy(s1 = y / other)

}