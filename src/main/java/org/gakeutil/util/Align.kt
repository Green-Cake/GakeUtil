package org.gakeutil.util

import org.gakeutil.posOf

/**
 * For aligning positions.
 * Position without alignment equals Position that is aligned with [BOTTOM_RIGHT].
 *
 * @see Position
 * @see Scale
 * @see org.gakeutil.alignTo
 * @see IGameObj
 */
enum class Align(private inline val offsetFactory: (Scale)->Position) {

    UPPER_RIGHT({ posOf(0.0, it.h) }),
    UPPER_LEFT({ posOf(-it.w, it.h) }),
    BOTTOM_RIGHT({ posOf() }),
    BOTTOM_LEFT({ posOf(-it.w, 0.0) }),
    CENTER({ posOf(-it.w/2, it.h/2) })

    ;

    fun getOffset(scale: Scale) = offsetFactory(scale)

    fun applyTo(position: Position, scale: Scale) = position + getOffset(scale)

}