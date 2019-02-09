package org.gakeutil.util

import org.gakeutil.alignTo
import org.gakeutil.rangeOf

interface IGameObj {

    var position: Position
    var scale: Scale

    var align: Align

    val alignedPos get() = position.alignTo(align, scale)

    val range get() = rangeOf(alignedPos, scale)

}