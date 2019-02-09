package org.gakeutil.util

import java.io.Serializable

class Scale internal constructor (w: Double, h: Double) : Double2d<Scale>(w, h), Serializable {

    companion object {

        val ZERO = Scale(0.0, 0.0)

    }

    val w get() = f
    val h get() = s

    override fun copy(f1: Double, s1: Double) = Scale(f1, s1)

}