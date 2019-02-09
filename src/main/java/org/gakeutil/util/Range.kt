package org.gakeutil.util

import javafx.geometry.Rectangle2D

data class Range internal constructor(val from: Position, val scale: Scale) : Rectangle2D(from.x, from.y, scale.w, scale.h) {

    fun contains(pos: Position) = super.contains(pos.toPoint())

}