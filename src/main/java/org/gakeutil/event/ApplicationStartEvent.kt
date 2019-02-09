package org.gakeutil.event

import javafx.stage.Stage
import org.gakeutil.AppCore

/**
 * Received on [AppCore.onStart] only.
 *
 * @param app The [AppCore].
 *
 * @see AppCore.onStart
 */
class ApplicationStartEvent internal constructor(val app: AppCore) : Event() {

    /**
     * Shows the window.
     */
    fun show() = app.primaryStage.show()

    /**
     * references [AppCore.primaryStage]
     */
    val stage get() = app.primaryStage

    /**
     * @see Stage.isFullScreen
     */
    var isFullScreen
        get() = app.primaryStage.isFullScreen
        set(value) { app.primaryStage.isFullScreen = value }

    /**
     * @see Stage.isResizable
     */
    var isResizable
        get() = app.primaryStage.isResizable
        set(value) { app.primaryStage.isResizable = value }

}