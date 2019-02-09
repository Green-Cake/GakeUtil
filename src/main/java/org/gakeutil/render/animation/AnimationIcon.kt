package org.gakeutil.render.animation

import org.gakeutil.closeOverflowable
import org.gakeutil.render.texture.Texture
import org.gakeutil.util.IUpdatable

open class AnimationIcon private constructor(val totalTick: Int, val frames: Array<out Pair<IntRange, Texture>>): IIcon {

    companion object {

        fun new(vararg frames: Pair<IntRange, Texture>) {

            AnimationIcon(frames.last().first.last, frames)

        }

    }

    var isRunning = false

    var tick = 0.closeOverflowable(totalTick)

    override fun update() {

        if(isRunning){

            ++tick

        } else {}

    }

    override fun getFrame() = frames.find { tick.value in it.first }!!.second

    fun start() { isRunning = true }

    fun stop() { isRunning = false }

}