package org.gakeutil.doc.sample

import javafx.scene.paint.Color
import org.gakeutil.alignCenter
import org.gakeutil.render.IRenderable
import org.gakeutil.render.Renderer
import org.gakeutil.scaleOf

class RendererSample0 : IRenderable {

    val scale0 = scaleOf(100.0)

    override fun render(renderer: Renderer) = renderer {//this = renderer


        fill *= Color.BLACK
        /* equals,
        fill += Color.BLACK
        fill.rectAll()
        */

        fill += Color.GRAY
        /* equals,
        fill.color = Color.GRAY
        */

        stroke += Color.WHITE
        /* equals,
        stroke.color = Color.GRAY
        */

        both {//this = both

            rect(center.alignCenter(scale0), scale0)
            /*
            equals,
            val p = center.alignTo(Align.CENTER, scale0)
            fill.rect(p, scale0)
            both.rect(p, scale0)
            */

        }

    }

}