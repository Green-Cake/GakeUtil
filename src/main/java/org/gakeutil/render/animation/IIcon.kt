package org.gakeutil.render.animation

import org.gakeutil.render.texture.Texture
import org.gakeutil.util.IUpdatable

interface IIcon: IUpdatable {

    override fun update()

    /**
     * Should be called on Update every time.
     */
    fun getFrame(): Texture

    fun updateAndGet(): Texture {
        update()
        return getFrame()
    }

}