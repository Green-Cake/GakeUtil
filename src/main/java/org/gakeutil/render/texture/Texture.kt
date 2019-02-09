package org.gakeutil.render.texture

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import org.gakeutil.AppCore
import org.gakeutil.scaleOf
import org.gakeutil.util.Scale

open class Texture internal constructor(val img: Image, val scale: Scale) {

    companion object {

        fun new(img: Image, scale: Scale = scaleOf(img.width, img.height)) = Texture(img, scale)

        fun new(app: AppCore, path: String?): Texture {

            if(path.isNullOrEmpty())
                return NULL_TEXTURE

            val _img = app.loadImage(path)
            return new(_img, scaleOf(_img.width, _img.height))
        }

        fun new(app: AppCore, path: String, scale: Scale) = new(app.loadImage(path), scale)

        val NULL_IMAGE: Image = WritableImage(2, 2).apply {
            pixelWriter.setColor(0, 0, Color.RED)
            pixelWriter.setColor(0, 1, Color.GREEN)
            pixelWriter.setColor(1, 0, Color.GREEN)
            pixelWriter.setColor(1, 1, Color.RED)
        }

        val NULL_TEXTURE = Texture(NULL_IMAGE, scaleOf(2.0))

    }

}