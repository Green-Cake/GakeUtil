package org.gakeutil.util

import org.gakeutil.posOf
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin

/**
 * For express an angle.
 *
 * @constructor
 *
 * @param value angle by **DEGREE**
 */
class Angle internal constructor (value: Double) : Serializable {

    companion object {

        val the0 = Angle(0.0)
        val the90 = Angle(90.0)
        val the180 = Angle(180.0)
        val the270 = Angle(270.0)
        val the360 = Angle(360.0)

    }

    private val value: Double

    init {

        val tmp0 = value % 360

        this.value = if(tmp0 < 0) 360 + tmp0 else tmp0

    }

    /**
     * The Angle value by degrees
     */
    val degrees = this.value

    /**
     * The Angle value by radians
     */
    val radians = Math.toRadians(degrees)

    /**
     * Mirror on the x-coordinate axes
     */
    val mirrorX get() = the180 - this

    /**
     * Mirror on the y-coordinate axes
     */
    val mirrorY get() = -this

    /**
     * Equals `posOf(sin(radians), cos(radians))`
     */
    val coordinate get() = posOf(sin(radians), cos(radians))

    operator fun plus(other: Angle) = Angle(value + other.value)
    operator fun plus(other: Double) = Angle(value + other)

    operator fun minus(other: Angle) = Angle(value - other.value)
    operator fun minus(other: Double) = Angle(value - other)

    operator fun times(other: Angle) = Angle(value * other.value)
    operator fun times(other: Double) = Angle(value * other)

    operator fun div(other: Angle) = Angle(value / other.value)
    operator fun div(other: Double) = Angle(value / other)

    operator fun rem(other: Angle) = Angle(value % other.value)
    operator fun rem(other: Double) = Angle(value % other)

    operator fun unaryMinus() = Angle(-value)

    operator fun compareTo(other: Angle) = degrees.compareTo(other.degrees)

    operator fun compareTo(other: Double) = degrees.compareTo(other)

    override fun equals(other: Any?) = (other as? Angle)?.value?.equals(value) == true

    override fun hashCode() = value.hashCode()

    /**
     * Forward angle value for 0.
     */
    fun walkTo0(v: Double) = this + if(degrees > 180) v else -v

    /**
     * @return self.degree .. other.degree
     */
    operator fun rangeTo(other: Angle) = degrees..other.degrees

}