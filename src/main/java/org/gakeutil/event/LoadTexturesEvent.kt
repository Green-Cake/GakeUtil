package org.gakeutil.event

import javafx.scene.image.Image
import org.gakeutil.AppCore
import org.gakeutil.render.texture.Texture
import org.gakeutil.render.texture.TextureMap
import org.gakeutil.scaleOf

class LoadTexturesEvent(val app: AppCore, val textureMap: TextureMap) : Event() {

    fun load(prefix: String, path: String) = textureMap.load("$prefix/$path", Texture.new(app, "$prefix/$path"))

    fun loadAll(prefix: String) {

        ClassLoader.getSystemResources("${app.assetsPath}/textures/prefix").iterator().forEach {

            val uriPath = it.toURI().path

            val img = Image(uriPath, 0.0, 0.0, false, false)

            textureMap.load("$prefix/${uriPath.replace('\\', '/').split('.').lastOrNull()}", Texture(img, scaleOf(img.width, img.height)))

        }

    }

}