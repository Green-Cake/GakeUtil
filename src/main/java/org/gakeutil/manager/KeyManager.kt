package org.gakeutil.manager

import javafx.application.Platform
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.gakeutil.AppCore
import org.gakeutil.util.IUpdatable

class KeyManager(val app: AppCore) : IUpdatable {

    private val keyMap = mutableMapOf<KeyCode, Int>()

    private val join_keyMap = mutableSetOf<KeyCode>()

    private val destroy_keyMap = mutableSetOf<KeyCode>()

    internal fun onKeyPressed(event: KeyEvent) {

        if(event.code == KeyCode.F12) {

            app.onExit()

            Platform.exit()

        }

        else
            join_keyMap.add(event.code)

    }

    internal fun onKeyReleased(event: KeyEvent) {

        destroy_keyMap.add(event.code)

    }

    fun isKeyPressing(code: KeyCode) = keyMap.containsKey(code)

    fun isKeyPressingFirst(code: KeyCode) = keyMap[code] == 0

    operator fun contains(code: KeyCode) = isKeyPressing(code)

    override fun update() {

        join_keyMap.forEach {

            keyMap[it] = (keyMap[it] ?: -2) + 1

        }

        join_keyMap.clear()

        destroy_keyMap.forEach { keyMap.remove(it) }
        destroy_keyMap.clear()

        keyMap.keys.forEach {

            keyMap[it] = (keyMap[it] ?: return@forEach) + 1

        }
    }

    fun getPressingKeys() = keyMap.keys.toTypedArray()

    fun getPressingFirstKeys() = keyMap.filter { it.value == 0 }.keys.toTypedArray()

    operator fun invoke(block: KeyManager.()->Unit) = block()

}