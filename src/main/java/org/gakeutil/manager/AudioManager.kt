package org.gakeutil.manager

import javafx.scene.media.AudioClip
import org.gakeutil.AppCore

class AudioManager(val app: AppCore) {

    private val soundEffects = mutableMapOf<String, AudioClip>()

    operator fun set(name: String, path: String) {

        soundEffects[name] = AudioClip(ClassLoader.getSystemResource("${app.assetsPath}/sounds/$path.wav").toURI().toString())

    }

    operator fun get(name: String) = soundEffects[name]

}