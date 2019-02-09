package org.gakeutil.render

import com.sun.deploy.util.SyncAccess
import javafx.geometry.VPos
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.GraphicsContext
import javafx.scene.effect.Effect
import javafx.scene.effect.Light
import javafx.scene.effect.Lighting
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Affine
import org.gakeutil.*
import org.gakeutil.util.Angle
import org.gakeutil.util.IUpdatable
import org.gakeutil.util.Position
import org.gakeutil.util.Scale

class Renderer(val app: AppCore, val innerScale: Scale = scaleOf(app.canvas.graphicsContext2D.canvas.width, app.canvas.graphicsContext2D.canvas.height)) {

    val gc: GraphicsContext get() = app.canvas.graphicsContext2D

    var offset = Position.ZERO
        @Deprecated("INVOKE FROM [Renderer] ONLY") set

    val realScale get() = scaleOf(app.primaryStage.scene.width, app.primaryStage.scene.height)

    val innerRange = rangeOf(offset, innerScale)

    val center get() = posOf(innerScale)/2.0

    val magToReal get() = (realScale / innerScale).min()

    val magToInner get() = (innerScale / realScale).min()

    private val canvas get() = gc.canvas

    val currentThreadName get() = Thread.currentThread().name

    val fill = object : RenderMethod(this) {

        override var color: Paint
            get() = gc.fill
            set(value) { gc.fill = value }

        override fun impl_rect(pos: Position, scale: Scale) =
            gc.fillRect(pos.x, pos.y, scale.w, scale.h)

        override fun impl_roundRect(pos: Position, scale: Scale, roundScale: Scale) =
            gc.fillRoundRect(pos.x, pos.y, scale.w, scale.h, roundScale.w, roundScale.h)

        override fun impl_oval(pos: Position, scale: Scale) =
            gc.fillOval(pos.x, pos.y, scale.w, scale.h)

        override fun impl_arc(pos: Position, scale: Scale, startAngle: Angle, arcExtent: Double, closure: ArcType) =
            gc.fillArc(pos.x, pos.y, scale.w, scale.h, startAngle.degrees, arcExtent, closure)

        override fun impl_text(text: String, pos: Position) =
            gc.fillText(text, pos.x, pos.y)

        override fun impl_polygon(points: Array<Position>) =
            gc.fillPolygon(points.map { it.x }.toDoubleArray(), points.map { it.y }.toDoubleArray(), points.size)

    }

    val stroke = object : RenderMethod(this) {

        override var color: Paint
            get() = gc.stroke
            set(value) { gc.stroke = value }

        override fun impl_rect(pos: Position, scale: Scale) =
            gc.strokeRect(pos.x, pos.y, scale.w, scale.h)

        override fun impl_roundRect(pos: Position, scale: Scale, roundScale: Scale) =
            gc.strokeRoundRect(pos.x, pos.y, scale.w, scale.h, roundScale.w, roundScale.h)

        override fun impl_oval(pos: Position, scale: Scale) =
            gc.strokeOval(pos.x, pos.y, scale.w, scale.h)

        override fun impl_arc(pos: Position, scale: Scale, startAngle: Angle, arcExtent: Double, closure: ArcType) =
            gc.strokeArc(pos.x, pos.y, scale.w, scale.h, startAngle.degrees, arcExtent, closure)

        override fun impl_text(text: String, pos: Position) =
            gc.strokeText(text, pos.x, pos.y)

        override fun impl_polygon(points: Array<Position>) =
            gc.strokePolygon(points.map { it.x }.toDoubleArray(), points.map { it.y }.toDoubleArray(), points.size)

    }

    val both = object : RenderMethod(this) {

        @Deprecated("NOT_IN_USE", ReplaceWith("Unit"))
        override fun impl_rect(pos: Position, scale: Scale)  = Unit

        @Deprecated("NOT_IN_USE", ReplaceWith("Unit"))
        override fun impl_roundRect(pos: Position, scale: Scale, roundScale: Scale)  = Unit

        @Deprecated("NOT_IN_USE", ReplaceWith("Unit"))
        override fun impl_oval(pos: Position, scale: Scale) = Unit

        @Deprecated("NOT_IN_USE", ReplaceWith("Unit"))
        override fun impl_arc(pos: Position, scale: Scale, startAngle: Angle, arcExtent: Double, closure: ArcType) = Unit

        @Deprecated("NOT_IN_USE", ReplaceWith("Unit"))
        override fun impl_text(text: String, pos: Position) = Unit

        @Deprecated("NOT_IN_USE", ReplaceWith("Unit"))
        override fun impl_polygon(points: Array<Position>) = Unit

        @Deprecated("NOT_IN_USE")
        override var color: Paint
            get() = Color.BLACK
            @Suppress("UNUSED_PARAMETER")
            set(value) { }

        override fun rect(pos: Position, scale: Scale) {

            fill.rect(pos, scale)
            stroke.rect(pos, scale)

        }

        override fun roundRect(pos: Position, scale: Scale, roundScale: Scale) {

            fill.roundRect(pos, scale, roundScale)
            stroke.roundRect(pos, scale, roundScale)

        }

        override fun oval(pos: Position, scale: Scale) {

            fill.oval(pos, scale)
            stroke.oval(pos, scale)

        }

        override fun arc(pos: Position, scale: Scale, startAngle: Angle, arcExtent: Double, closure: ArcType) {

            fill.arc(pos, scale, startAngle, arcExtent, closure)
            stroke.arc(pos, scale, startAngle, arcExtent, closure)

        }

        override fun text(text: String, pos: Position) {

            fill.text(text, pos)
            stroke.text(text, pos)

        }

        override fun text(text: String, pos: Position, font: Font) {

            fill.text(text, pos, font)
            stroke.text(text, pos, font)

        }

        override fun text(text: String, pos: Position, font: Font, alignment: TextAlignment) {

            fill.text(text, pos, font, alignment)
            stroke.text(text, pos, font, alignment)

        }

        override fun polygon(vararg points: Position, nPoints: Int) {

            fill.polygon(*points, nPoints = nPoints)
            stroke.polygon(*points, nPoints = nPoints)

        }

        override fun regularPolygon(corners: Int, radius: Double, pos: Position, rotationOffset: Angle): Array<Position> {

            fill.regularPolygon(corners, radius, pos, rotationOffset)
            return stroke.regularPolygon(corners, radius, pos, rotationOffset)

        }

    }

    val affineController = this.AffineController()

    fun drawImage(img: Image, pos: Position, scale: Scale) = impl_drawImage(img, convertPosToReal(pos), convertScaleToReal(scale))

    fun drawImage(img: Image, sourceScale: Scale, pos: Position, scale: Scale) = impl_drawImage(img, Position.ZERO, sourceScale, convertPosToReal(pos), convertScaleToReal(scale))

    fun drawImage(img: Image, sourcePos: Position, sourceScale: Scale, pos: Position, scale: Scale) = impl_drawImage(img, sourcePos, sourceScale, convertPosToReal(pos), convertScaleToReal(scale))

    private fun impl_drawImage(img: Image, pos: Position, scale: Scale) = gc.drawImage(img, pos.x, pos.y, scale.w, scale.h)

    private fun impl_drawImage(img: Image, sourcePos: Position, sourceScale: Scale, pos: Position, scale: Scale) =
        gc.drawImage(img, sourcePos.x, sourcePos.y, sourceScale.w, sourceScale.h, pos.x, pos.y, scale.w, scale.h)

    fun strokeLine(start: Position, end: Position) {

        val _start = convertPosToReal(start)

        val _end = convertPosToReal(end)

        gc.strokeLine(_start.x, _start.y, _end.x, _end.y)
    }

    fun strokePolyline(vararg positions: Position, nPoints: Int = positions.size) {

        val _positions = positions.map { convertPosToReal(it) }

        gc.strokePolyline(_positions.map { it.x }.toDoubleArray(), _positions.map { it.y }.toDoubleArray(), nPoints)

    }

/*

    fun beginPath() = gc.beginPath()

    fun closePath() = gc.closePath()

    fun lineTo(target: Position) {

        val _target = convertPosToReal(target)

        gc.lineTo(_target.x, _target.y)

    }
*/

    //renderings end

    fun convertPosToReal(pos: Position) = invertY(pos) * magToReal + offset

    fun convertScaleToReal(scale: Scale) = scale * magToReal

    fun convertPosToInner(pos: Position) = invertY((pos - offset) / magToReal)

    fun convertScaleInner(scale: Scale) = scale * magToInner

    var font: Font
        get() = gc.font
        set(value) { gc.font = value }

    var textAlign: TextAlignment
        get() = gc.textAlign
        set(value) { gc.textAlign = value }

    var textBaseline: VPos
        get() = gc.textBaseline
        set(value) { gc.textBaseline = value }

    var lineWidth: Double
        get() = gc.lineWidth
        set(value) { gc.lineWidth = value }

    val lineDashes: DoubleArray get() = gc.lineDashes

    fun setLineDashes(vararg dashes: Double) = gc.setLineDashes(*dashes)

    val pixelWriter get() = gc.pixelWriter

    fun setTextCenter() {

        textAlign = TextAlignment.CENTER
        textBaseline = VPos.CENTER

    }

    fun clearRect(pos: Position, scale: Scale) = gc.fillRect(pos.x, pos.y, scale.w, scale.h)

    fun clearAll() = clearRect(Position.ZERO, innerScale)

    fun invertY(pos: Position) = posOf(pos.x, innerScale.h - pos.y)

    //

    fun contains(pos: Position) = pos.toPoint() in innerRange

    inline fun withEffect(effect: Effect, block: Renderer.()->Unit) = try {

        checkThread()

        gc.setEffect(effect)

        block()

    } finally {

        gc.setEffect(null)

    }

    inline fun withLightingPoint(scale: Scale, z: Double, color: Color, block: Renderer.()->Unit) = withEffect(Lighting(Light.Point(scale.w/2, scale.h/2, z, color)), block)

    inline fun withAlpha(alpha: Double, block: Renderer.()->Unit) {

        checkThread()

        val prev = gc.globalAlpha

        try {

            gc.globalAlpha = alpha

            block()

        } finally {

            gc.globalAlpha = prev

        }

    }

    fun snapshot(params: SnapshotParameters): Image = canvas.snapshot(params, WritableImage(canvas.width.toInt(), canvas.height.toInt()))

    fun snapshot(): Image = snapshot(SnapshotParameters())

    fun snapshot(init: SnapshotParameters.()->Unit): Image {

        val params = SnapshotParameters()

        params.init()

        return snapshot(params)
    }

    //

    inline operator fun invoke(block: Renderer.()->Unit) {
        checkThread()
        block(this)
    }

    inner class AffineController {

        fun reset() { gc.transform = Affine() }

        @Suppress("DEPRECATION")
        fun resetOffset() { offset = posOf() }

        fun affine(init: Renderer.(Affine)->Unit, block: Renderer.(Affine)->Unit) {

            checkThread()

            val pre = gc.transform.clone()

            val a = gc.transform

            init(a)

            gc.transform = a

            block(a)

            a.setToIdentity()

            gc.transform = pre

        }

        @Suppress("DEPRECATION")
        fun offset(position: Position, block: ()->Unit) {

            checkThread()

            val pre = offset

            try {

                offset = position

                block()

            } finally {

                offset = pre

            }

        }

        fun rotation(angle: Angle, pivot: Position, block: Renderer.(Affine)->Unit) {

            affine({

                val _pivot = convertPosToReal(pivot)

                it.appendRotation(angle.degrees, _pivot.x, _pivot.y)

            }, block)

        }

        operator fun invoke(block: AffineController.()->Unit) {
            checkThread()
            this.(block)()
        }

    }

    fun checkThread() {
        if(currentThreadName != CommonSetting.JAVAFX_THREAD_NAME)
            throw IllegalAccessException("Should call $this from ${CommonSetting.JAVAFX_THREAD_NAME}")
    }

}