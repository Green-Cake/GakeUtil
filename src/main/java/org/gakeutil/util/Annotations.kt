package org.gakeutil.util

import org.gakeutil.render.Renderer
import javafx.scene.effect.Effect

/**
 *
 * Presents the information when the function is called about [Renderer]'s current condition.
 *
 * @see Renderer
 * @see Condition
 *
 * @constructor
 *
 * For specify...
 *
 * [Condition.YES] = 1
 *
 * [Condition.NO] = 0
 *
 * [Condition.UNKNOWN] = -1 or others.
 *
 *
 * @param isAffineDefault
 * Condition.YES if renderer's affine will never be the default one.
 *
 * @param isOffsetSet
 * Condition.YES if the offset will never be [Position.ZERO].
 *
 * @param isRotated
 * Condition.YES if the affine will be set any rotations.
 *
 * @param isEffected
 * Condition.YES if any [Effect] will be enabled in [Renderer].
 *
 * @see Condition
 *
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RendererMarker constructor(
    val isAffineDefault: Byte = -1,
    val isOffsetSet: Byte = -1,
    val isRotated: Byte = -1,
    val isEffected: Byte = -1
) {

    enum class Condition(val id: Byte) {

        YES(1),
        NO(0),
        UNKNOWN(-1)
        ;

        companion object {

            fun fromByte(b: Byte) = when(b) {
                1.toByte() -> YES
                0.toByte() -> NO
                else -> UNKNOWN
            }

        }

    }

}

val RendererMarker.conditionAffineDefault
    get() = RendererMarker.Condition.fromByte(isAffineDefault)

val RendererMarker.conditionOffsetSet
    get() = RendererMarker.Condition.fromByte(isOffsetSet)

val RendererMarker.conditionRotated
    get() = RendererMarker.Condition.fromByte(isRotated)

val RendererMarker.conditionEffected
    get() = RendererMarker.Condition.fromByte(isEffected)