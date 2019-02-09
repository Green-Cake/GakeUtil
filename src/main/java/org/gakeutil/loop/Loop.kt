package org.gakeutil.loop

import org.gakeutil.AppCore

import javafx.animation.AnimationTimer

/**
 *
 *
 * @see AppCore.loop
 */
open class Loop internal constructor(val process: (Long) -> Unit) : AnimationTimer() {

    companion object {

        fun new(process: (Long)->Unit) = Loop(process)

    }

    var isActive = false
        private set

    override fun start() {
        isActive = true
        super.start()
    }

    override fun stop() {
        super.stop()
        isActive = false
    }

    override fun handle(now: Long) = process(now)

}