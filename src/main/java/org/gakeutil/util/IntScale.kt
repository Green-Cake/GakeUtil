package org.gakeutil.util

import org.gakeutil.scaleOf
import java.io.Serializable

class IntScale internal constructor (w: Int, h: Int) : Int2d<IntScale>(w, h), Serializable {

    companion object {

        val ZERO = IntScale(0, 0)

    }

    val w get() = f
    val h get() = s

    override fun copy(f1: Int, s1: Int) = IntScale(f1, s1)

    fun toDoubleScale() = scaleOf(this)

}