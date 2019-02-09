package org.gakeutil.render.animation

import org.gakeutil.render.texture.Texture

open class StaticIcon(val texture: Texture): IIcon {

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFrame() = texture

}