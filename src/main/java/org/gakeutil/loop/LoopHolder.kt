package org.gakeutil.loop

import org.gakeutil.event.ApplicationStartEvent
import org.gakeutil.manager.KeyManager
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

abstract class LoopHolder(val event: ApplicationStartEvent) {

    val app = event.app

    val loop = app.loop { this@LoopHolder.process() }

    val scene = event.stage.scene

    abstract fun process()

    inline fun keyevents(block: KeyManager.()->Unit) = block(app.keyManager)

}