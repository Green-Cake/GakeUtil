package org.gakeutil.render

import javafx.scene.paint.Paint
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import org.gakeutil.*
import org.gakeutil.util.Angle
import org.gakeutil.util.Position
import org.gakeutil.util.Scale
import java.lang.IllegalArgumentException
import kotlin.contracts.contract

abstract class RenderMethod(val renderer: Renderer) {

    abstract var color: Paint

    open fun rect(pos: Position, scale: Scale) =
            impl_rect(renderer.convertPosToReal(pos), renderer.convertScaleToReal(scale))

    open fun roundRect(pos: Position, scale: Scale, roundScale: Scale) =
            impl_roundRect(renderer.convertPosToReal(pos), renderer.convertScaleToReal(scale), renderer.convertScaleToReal(roundScale))

    open fun oval(pos: Position, scale: Scale) =
        impl_oval(renderer.convertPosToReal(pos), renderer.convertScaleToReal(scale))

    open fun arc(pos: Position, scale: Scale, startAngle: Angle, arcExtent: Double, closure: ArcType) =
        impl_arc(renderer.convertPosToReal(pos), renderer.convertScaleToReal(scale), startAngle, arcExtent, closure)

    open fun text(text: String, pos: Position) =
        impl_text(text, renderer.convertPosToReal(pos))

    open fun text(text: String, pos: Position, font: Font) {
        renderer.font = font
        text(text, pos)
    }

    open fun text(text: String, pos: Position, font: Font, alignment: TextAlignment) {
        renderer.textAlign = alignment
        text(text, pos, font)
    }

    open fun polygon(vararg points: Position, nPoints: Int = points.size) =
        impl_polygon(points.map { renderer.convertPosToReal(it) }.subList(0, nPoints).toTypedArray())

    fun getPositionsOnCircumference(corners: Int, radius: Double, origin: Position = posOf(), rotationOffset: Angle = angleOf()): Array<Position> {

        if(corners < 3)
            throw IllegalArgumentException("corners=$corners must be more than three.")

        val baseAngle = angleOf(360.0 / corners)

        return Array(corners) { index -> posOnCircumference(baseAngle * index.toDouble() + rotationOffset, radius, origin) }

    }

    open fun regularPolygon(corners: Int, radius: Double, pos: Position, rotationOffset: Angle = angleOf()) =
        getPositionsOnCircumference(corners, radius, pos, rotationOffset).also { polygon(*it) }

    protected abstract fun impl_rect(pos: Position, scale: Scale)

    protected abstract fun impl_roundRect(pos: Position, scale: Scale, roundScale: Scale)

    protected abstract fun impl_oval(pos: Position, scale: Scale)

    protected abstract fun impl_arc(pos: Position, scale: Scale, startAngle: Angle, arcExtent: Double, closure: ArcType)

    protected abstract fun impl_text(text: String, pos: Position)

    protected abstract fun impl_polygon(points: Array<Position>)

    //

    fun rectAll() = impl_rect(Position.ZERO, renderer.realScale)

    operator fun plusAssign(other: Paint) {
        color = other
    }

    operator fun timesAssign(other: Paint) {
        color = other
        rectAll()
    }

    operator fun invoke(block: RenderMethod.()->Unit) {
        renderer.checkThread()
        this.(block)()
    }

}