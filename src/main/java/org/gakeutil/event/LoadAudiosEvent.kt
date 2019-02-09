package org.gakeutil.event

import org.gakeutil.AppCore
import org.gakeutil.manager.AudioManager

class LoadAudiosEvent(val app: AppCore, val audioManager: AudioManager) : Event() {

    fun load(name: String, path: String) { audioManager[name] = path }

}