package org.gakeutil.util

import javafx.geometry.Point2D
import org.gakeutil.angleOf
import java.io.Serializable
import java.util.*
import kotlin.math.abs

open class Position internal constructor (x: Double, y: Double) : Double2d<Position>(x, y), Serializable {

    companion object {

        private val rand = Random()

        val ZERO = Position(0.0, 0.0)

        fun randomIn(range: Range) = Position(rand.nextInt((range.maxX - range.minX).toInt()) + range.minX, rand.nextInt((range.maxY - range.minY).toInt()) + range.minY)

    }

    val x get() = f
    val y get() = s

    override fun copy(f1: Double, s1: Double) = Position(f1, s1)

    fun toPoint() = Point2D(x, y)

    infix fun distance2dTo(other: Position) = Position(other.x - x, other.y - y)

    infix fun absDistance2dTo(other: Position) = Position(abs(other.x - x), abs(other.x - x))

    infix fun distanceTo(other: Position): Double {

        val tmp0 = other.x - x

        val tmp1 = other.y - y

        return Math.sqrt(tmp0 * tmp0 + tmp1 * tmp1)

    }

    infix fun absDistanceTo(other: Position) = abs(distanceTo(other))

    /**
     * @param other Target position.
     *
     * @return [Angle] which looking from this to [other].
     */
    infix fun lookAt(other: Position): Angle {

        val a = other - this
        return angleOf(Math.toDegrees(Math.atan2(a.x, a.y)))

    }

    /**
     * @param others comparing targets. Must be more than 0.
     *
     * @return The nearest one.
     *
     * @throws IllegalArgumentException If `others.isEmpty()`
     */
    @Throws(IllegalArgumentException::class)
    fun getNearest(vararg others: Position): Position = if(others.isEmpty())
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
    fun getFarthest(vararg others: Position): Position = if(others.isEmpty())
        throw IllegalArgumentException("others must be more than 0.")
    else
        others.sortedBy { this.absDistanceTo(it) }.last()

    infix fun plusX(other: Double) = copy(f1 = x + other)
    infix fun minusX(other: Double) = copy(f1 = x - other)
    infix fun timesX(other: Double) = copy(f1 = x * other)
    infix fun divX(other: Double) = copy(f1 = x / other)

    infix fun plusY(other: Double) = copy(s1 = y + other)
    infix fun minusY(other: Double) = copy(s1 = y - other)
    infix fun timesY(other: Double) = copy(s1 = y * other)
    infix fun divY(other: Double) = copy(s1 = y / other)

}