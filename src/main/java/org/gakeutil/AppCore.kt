package org.gakeutil

import com.sun.javafx.tk.Toolkit
import javafx.application.Application
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import javafx.stage.Screen
import javafx.stage.Stage
import org.gakeutil.event.ApplicationStartEvent
import org.gakeutil.event.LoadAudiosEvent
import org.gakeutil.event.LoadTexturesEvent
import org.gakeutil.loop.Loop
import org.gakeutil.manager.AudioManager
import org.gakeutil.manager.KeyManager
import org.gakeutil.render.Renderer
import org.gakeutil.render.texture.Texture
import org.gakeutil.render.texture.TextureMap
import org.gakeutil.util.*
import java.util.*
import java.util.logging.Logger
import kotlin.reflect.KClass

/**
 *
 * The base of the Application by Gake Api.
 * This is extending [Application] but You cannot add some [javafx.scene.Node]. This has only one [Canvas].
 *
 * @constructor
 *
 * @param assetsPath specify by packages (separator is '/'). ex.) org.hoge.assets => org/
 * @param title The title of the window. will be set to [Stage.title].
 * @param displayScale The window's scale.
 * @param displayInnerScale The scale that [AppCore.renderer] uses mainly. In [Renderer]'s processing, this is used for specify [Position], [Scale], [Range] etc...
 * [Renderer] interprets them automatically and make them real values.
 * @param logger [Logger] for using on [AppCore].
 * @param defaultTexturePath The path of the image file to be used instead when some specified path is invalid. default value is null but it means [Texture.NULL_TEXTURE] for [Texture.new]
 * @param cursorOnScene The cursor type on the window.
 */
abstract class AppCore constructor(
    val assetsPath: String, val title: String, val displayScale: Scale, val displayInnerScale: Scale = scaleOf(1920.0, 1080.0),
    val logger: Logger,
    //defaults---
    defaultTexturePath: String? = null, val cursorOnScene: Cursor = Cursor.DEFAULT
) : Application() {

    companion object {

        /**
         * wrapper of [Application.launch].
         * @param clazz KClass of Application extending [AppCore]
         */
        fun launch(clazz: KClass<out AppCore>, args: Array<String>) = Application.launch(clazz.java, *args)

    }

    /**
     * For using instead when any specified texture path is not available.
     */
    @Suppress("LeakingThis")
    val defaultTexture = Texture.new(this, defaultTexturePath)

    /**
     * The instance of [Renderer].
     * For all of rendering for the game.
     *
     * @sample org.gakeutil.doc.sample.RendererSample0
     */
    @Suppress("LeakingThis")
    val renderer = Renderer(this, displayInnerScale)

    @Suppress("LeakingThis")
    val keyManager = KeyManager(this)

    @Suppress("LeakingThis")
    val audioManager = AudioManager(this)

    /**
     * The instance of [Random]. (NOT [kotlin.random.Random])
     */
    val rand = Random()

    //privates---

    internal lateinit var primaryStage: Stage

    internal val canvas = Canvas(displayScale.w, displayScale.h)

    private val mainScene = Scene(Pane(canvas))

    private val textureMap = TextureMap()

    //---privates

    final override fun start(primaryStage: Stage) {

        logger.info("current screen scaleOf: ${Screen.getPrimary().bounds.scale}")

        logger.info("screens count: ${Screen.getScreens().size}")

        logger.info("f/s: ${Toolkit.getToolkit().refreshRate}")

        this.primaryStage = primaryStage

        logger.info("GakeUtil's AppCore started on ($this).")

        onInit()

        logger.info("Completed initialization.")

        onLoadTextures(LoadTexturesEvent(this, textureMap))

        logger.info("Completed loading textures.")

        onLoadAudios(LoadAudiosEvent(this, audioManager))

        logger.info("Completed loading audios.")

        primaryStage.scene = mainScene

        primaryStage.title = title

        primaryStage.isFullScreen = true

        primaryStage.fullScreenExitKeyProperty().set(KeyCombination.NO_MATCH)

        primaryStage.scene.cursor = cursorOnScene

        onStart(ApplicationStartEvent(this))

        primaryStage.scene.setOnKeyPressed { _onKeyPressed(it) }

        primaryStage.scene.setOnKeyReleased { _onKeyReleased(it) }

    }

    /**
     * Called between logging "GakeUtil's AppCore started on..." and logging "Completed initialization."
     */
    open fun onInit() = Unit

    /**
     * Called at the end of [AppCore.start]. all primitive initialization is finished when called.
     */
    abstract fun onStart(event: ApplicationStartEvent)

    /**
     * Called when request exit.
     */
    open fun onExit() = Unit

    /**
     * For load textures.
     * @see LoadTexturesEvent
     */
    abstract fun onLoadTextures(event: LoadTexturesEvent)

    /**
     * For load audios.
     * @see LoadAudiosEvent
     */
    abstract fun onLoadAudios(event: LoadAudiosEvent)

    /**
     * For load image file on "[AppCore.assetsPath]/textures/". returns Texture.NULL_IMAGE if failed.
     *
     * @param path image file's path.
     * @param requestedScale scale that is requested.
     * @param smooth Do or not smooth.
     *
     * @return Requested image or [Texture.NULL_IMAGE]
     */
    fun loadImage(path: String, requestedScale: Scale, smooth: Boolean = false): Image = try {

        Image(ClassLoader.getSystemResourceAsStream("$assetsPath/textures/$path.png"), requestedScale.w, requestedScale.h, false, smooth)

    } catch(e: Throwable) {

        logger.warning("$path that AppCore#loadImage loaded does not exist!!")

        Texture.NULL_IMAGE

    }

    /**
     * For load image file on "[AppCore.assetsPath]/textures/". returns [Texture.NULL_IMAGE] if failed.
     * @return Requested image or [Texture.NULL_IMAGE]
     */
    fun loadImage(path: String, smooth: Boolean = false) = loadImage(path, Scale.ZERO, smooth)

    /**
     * For get loaded Texture by specified id.
     *
     * @return The specified texture or [defaultTexture].
     */
    fun getTexture(id: String) = textureMap.getOrDefault(id, defaultTexture)

    /**
     * Returns [Loop] which is optimized for [AppCore].
     * Below is inserted to [Loop]
     * - [KeyManager.update]
     * - automatic resizing canvas scale for fit in the [Scene]
     *
     * @return [Loop] which is optimized for [AppCore].
     */
    inline fun loop(crossinline process: (Long) -> Unit) = Loop.new {

        keyManager.update()

        renderer.gc.canvas.width = renderer.innerScale.w * renderer.magToReal

        renderer.gc.canvas.height = renderer.innerScale.h * renderer.magToReal

        process(it)

    }

    /**
     * Event on any key is pressed.
     *
     * Except for special cases, you don't have to override this.
     *
     * Use [AppCore.keyManager] instead.
     */
    open fun onKeyPressed(event: KeyEvent) = Unit

    /**
     * Event on any key is released.
     *
     * Except for special cases, you don't have to override this.
     *
     * Use [AppCore.keyManager] instead.
     */
    open fun onKeyReleased(event: KeyEvent) = Unit

    private fun _onKeyPressed(event: KeyEvent) {

        keyManager.onKeyPressed(event)

        onKeyPressed(event)

    }

    private fun _onKeyReleased(event: KeyEvent) {

        keyManager.onKeyReleased(event)

        onKeyReleased(event)

    }

}