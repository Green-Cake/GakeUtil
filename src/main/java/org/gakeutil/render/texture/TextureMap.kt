package org.gakeutil.render.texture

class TextureMap {

    data class TextureSet(val id: String, val texture: Texture)

    private val map = mutableMapOf<String, Texture>()

    fun loadAll(vararg pairs: Pair<String, Array<TextureSet>>) {

        pairs.forEach { pair ->

            pair.second.forEach { set ->

                load(pair.first + "/" + set.id, set.texture)

            }

        }

    }

    fun loadAll(vararg sets: TextureSet) {

        sets.forEach {

            map[it.id] = it.texture

        }

    }

    fun load(id: String, texture: Texture) { map[id] = texture }

    fun load(set: TextureSet) = load(set.id, set.texture)

    operator fun get(id: String) = map[id]!!

    fun getOrDefault(id: String, default: Texture) = map[id] ?: default

}