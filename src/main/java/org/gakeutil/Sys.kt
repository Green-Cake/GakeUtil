//@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("GakeUtilSys")

package org.gakeutil

import javafx.geometry.Rectangle2D
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.gakeutil.render.Renderer
import org.gakeutil.util.*
import org.gakeutil.util.collection.*
import org.gakeutil.util.number.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

typealias ByteArray2d = Array<ByteArray>

typealias ByteArray3d = Array<Array<ByteArray>>

typealias ShortArray2d = Array<ShortArray>

typealias ShortArray3d = Array<Array<ShortArray>>

typealias IntArray2d = Array<IntArray>

typealias IntArray3d = Array<Array<IntArray>>

typealias LongArray2d = Array<LongArray>

typealias LongArray3d = Array<Array<LongArray>>

typealias FloatArray2d = Array<FloatArray>

typealias FloatArray3d = Array<Array<FloatArray>>

typealias DoubleArray2d = Array<DoubleArray>

typealias DoubleArray3d = Array<Array<DoubleArray>>

typealias BoolArray2d = Array<BoolArray>

typealias BoolArray3d = Array<Array<BoolArray>>

typealias Array2d<T> = Array<Array<T>>

typealias Array3d<T> = Array<Array<Array<T>>>

//constructors---

/**
 * @return [Position.ZERO]
 */
fun posOf() = Position.ZERO

fun posOf(x: Double = 0.0, y: Double = x) = Position(x, y)

fun posOf(x: Number = 0.0, y: Number = 0.0) = posOf(x.toDouble(), y.toDouble())

fun posOf(d2d: Double2d<*>) = posOf(d2d.f, d2d.s)

fun posOf(i2d: Int2d<*>) = posOf(i2d.f, i2d.s)

/**
 * @return position on the specified circumference.
 */
fun posOnCircumference(angle: Angle, radius: Double = 1.0, origin: Position = posOf()) = angle.coordinate * radius + origin

/**
 * @return [Scale.ZERO]
 */
fun scaleOf() = Scale.ZERO

fun scaleOf(w: Double = 0.0, h: Double = w) = Scale(w, h)

fun scaleOf(w: Number = 0.0, h: Number = w) = scaleOf(w.toDouble(), h.toDouble())

fun scaleOf(d2d: Double2d<*>) = scaleOf(d2d.f, d2d.s)

fun scaleOf(i2d: Int2d<*>) = scaleOf(i2d.f, i2d.s)

/**
 * @return [IntPos.ZERO]
 */
fun intPosOf() = IntPos.ZERO

fun intPosOf(x: Int = 0, y: Int = 0) = IntPos(x, y)

fun intPosOf(i2d: Int2d<*>) = IntPos(i2d.f, i2d.s)

fun intScaleOf() = IntScale.ZERO

fun intScaleOf(x: Int = 0, y: Int = 0) = IntScale(x, y)

fun intScaleOf(i2d: Int2d<*>) = intScaleOf(i2d.f, i2d.s)

/**
 * @return [Angle.the0]
 */
fun angleOf() = Angle.the0

fun angleOf(degrees: Double) = Angle(degrees)

fun angleOfRadians(radians: Double) = Angle(Math.toDegrees(radians))

fun rangeOf(position: Position = posOf(), scale: Scale = scaleOf()) = Range(position - posOf(0.0, scale.h), scale)

fun rangeOf(posFrom: Position = posOf(), posTo: Position) = Range(min(posFrom, posTo), scaleOf(posFrom absDistance2dTo posTo))

fun boolArrayOf(vararg elements: Boolean) = elements.toBoolArray()

fun <T : IGameContent> gameContentsSetOf() = GameContentSet<T>()

fun <T : IGameContent> gameContentsSetOf(vararg elements: T): GameContentSet<T> = elements.toCollection(
    GameContentSet()
)

fun <T : IGameContent> gameContentsListOf() = GameContentList<T>()

fun <T : IGameContent> gameContentsListOf(vararg elements: T): GameContentList<T> = elements.toCollection(
    GameContentList()
)

fun <E> bufferSetOf(set: MutableSet<E> = mutableSetOf()) = BufferSet(set)

fun <E> bufferSetOf(vararg elements: E) = bufferSetOf(elements.toMutableSet())

fun <K, V> bufferMapOf(map: MutableMap<K, V> = mutableMapOf()) = BufferMap(map)


//extensions---

infix fun Int.cross(other: Int) = intPosOf(this, other)

infix fun Double.cross(other: Double) = posOf(this, other)

operator fun ClosedFloatingPointRange<Double>.contains(other: Angle) = other.degrees in this

fun <E> MutableSet<E>.toBufferSet() = bufferSetOf(this)

inline val Rectangle2D.minPos get() = posOf(minX, minY)

inline val Rectangle2D.maxPos get() = posOf(maxX, maxY)

inline val Rectangle2D.scale get() = scaleOf(width, height)

val MouseEvent.position get() = posOf(x, y)

fun MouseEvent.positionInnerBy(renderer: Renderer) = renderer.convertPosToInner(position)

infix fun Position.rangeTo(other: Position) = rangeOf(this, other)

fun Position.alignTo(align: Align, scale: Scale) = align.applyTo(this, scale)

fun Position.alignCenter(scale: Scale) = Align.CENTER.applyTo(this, scale)

fun Int.close(range: IntRange, isOverflowable: Boolean) = if(isOverflowable) closeOverflowable(range) else close(range)

fun Int.close(range: IntRange) = closedIntOf(this, range)

fun Int.close(upper: Int) = this.close(0..upper)

fun Int.closeOverflowable(range: IntRange) = overflowableClosedIntOf(this, range)

fun Int.closeOverflowable(upper: Int) = this.closeOverflowable(0..upper)

infix fun Double.close(range: ClosedFloatingPointRange<Double>) = closedDoubleOf(this, range)

infix fun Double.close(upper: Double) = this close 0.0..upper

infix fun Int.fitIn(range: IntRange) = max(range.start, min(range.endInclusive, this))

infix fun Int.fitUnder(upper: Int) = this.fitIn(0..upper)

infix fun Int.fitOn(lower: Int) = this.fitIn(lower..Int.MAX_VALUE)

infix fun Double.fitIn(range: ClosedFloatingPointRange<Double>) = max(range.start, min(range.endInclusive, this))

infix fun Double.fitIn(upper: Double) = this.fitIn(0.0..upper)

infix fun Position.fitIn(range: Range): Position {

    var tmp = this

    if(x < range.minX)
        tmp = tmp.copy(f1 = range.minX)

    else if(x > range.maxX)
        tmp = tmp.copy(f1 = range.maxX)

    if(y < range.minY)
        tmp = tmp.copy(s1 = range.minY)

    else if(y > range.maxY)
        tmp = tmp.copy(s1 = range.maxY)

    return tmp

}

//

inline fun <reified T> Set<*>.filterType() = filter { it is T }.map { it as T }.toSet()

inline fun <reified T> List<*>.filterType() = filter { it is T }.map { it as T }.toList()

//

operator fun <T> Array2d<T>.get(indexX: Int, indexY: Int) = this[indexX][indexY]

operator fun <T> Array2d<T>.set(indexX: Int, indexY: Int, t: T) { this[indexX][indexY] = t }

operator fun <T> Array3d<T>.get(indexX: Int, indexY: Int, indexZ: Int) = this[indexX][indexY][indexZ]

operator fun <T> Array3d<T>.set(indexX: Int, indexY: Int, indexZ: Int, t: T) { this[indexX][indexY][indexZ] = t }

operator fun <T> Array2d<T>.get(index: IntPos) = this[index.x][index.y]

operator fun <T> Array2d<T>.set(index: IntPos, t: T) { this[index.x][index.y] = t }

//---extensions

inline fun <reified T> array2dOf(sizeX: Int, sizeY: Int, init: (x: Int, y: Int)->T) = Array(sizeX) { x -> Array(sizeY) { y -> init(x, y) } }

inline fun <reified T> array3dOf(sizeX: Int, sizeY: Int, sizeZ: Int, init: (x: Int, y: Int, z: Int)->T) =
    Array(sizeX) { x -> Array(sizeY) { y -> Array(sizeZ) { z -> init(x, y, z) } } }

//primitives---

fun closedIntOf(value: Number, range: IntRange) = ClosedInt(value.toInt(), range)

fun overflowableClosedIntOf(value: Number, range: IntRange) =
    OverflowableClosedInt(value.toInt(), range)

fun closedDoubleOf(value: Number, range: ClosedFloatingPointRange<Double>) = ClosedDouble(value.toDouble(), range)

//---primitives

fun <T> tryEachWhileNull(default: T, vararg values: ()->T?): T {

    values.forEach {

        return it() ?: return@forEach

    }

    return default

}

object CollectionUtil {

    fun <F, S> pairsIntoMap(map: MutableMap<F, S>, pairs: Array<out Pair<F, S>>) = pairs.forEach {

        map[it.first] = it.second

    }

}

object FontUtil {

    var family = "arial"

    fun font(name: String, size: Double): Font = Font.font(name, size)

    fun font(size: Double): Font = font(family, size)

}

object PaintUtil {

    fun alphaColor(base: Color, alpha: Double) = Color.color(base.red, base.green, base.blue, alpha.fitIn(0.0..1.0))

    fun Color.toAlpha(alpha: Double) = alphaColor(this, alpha)

    fun random(rand: Random = Random) = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())

}
