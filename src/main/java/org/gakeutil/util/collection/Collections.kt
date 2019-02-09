package org.gakeutil.util.collection

import org.gakeutil.render.Renderer
import org.gakeutil.util.IGameContent
import java.util.*

class GameContentSet<T : IGameContent> : LinkedHashSet<T>() {

    fun updateAll() = this.forEach { it.update() }

    fun renderAll(renderer: Renderer) = this.forEach { it.render(renderer) }

}

class GameContentList<T : IGameContent> : ArrayList<T>() {

    fun updateAll() = this.forEach { it.update() }

    fun renderAll(renderer: Renderer) = this.forEach { it.render(renderer) }

}

class BufferSet<E>(val set: MutableSet<E> = mutableSetOf()) : MutableSet<E> by set {

    private val _join = mutableSetOf<E>()

    private val _defect = mutableSetOf<E>()

    override fun add(element: E) = _join.add(element)

    override fun addAll(elements: Collection<E>) = _join.addAll(elements)

    override fun remove(element: E) = _defect.add(element)

    override fun removeAll(elements: Collection<E>) = _defect.addAll(elements)

    override fun clear() { _defect.addAll(set) }

    fun update() {

        set.addAll(_join)

        _join.clear()

        set.removeAll(_defect)

        _defect.clear()

    }

}

class BufferMap<K, V>(val map: MutableMap<K, V> = mutableMapOf()) : MutableMap<K, V> by map {

    private val _join = mutableMapOf<K, V>()

    private val _defect = mutableSetOf<Pair<K, V?>>()

    override fun put(key: K, value: V) = _join.put(key, value)

    override fun putAll(from: Map<out K, V>) = _join.putAll(from)

    override fun remove(key: K): V? {
        _defect.add(key to null)
        return map[key]
    }

    override fun clear() {
        map.keys.forEach { remove(it) }
    }

    fun update() {

        map.putAll(_join)

        _join.clear()

        _defect.forEach {

            if(it.second == null)
                map.remove(it.first)

            else
                map.remove(it.first, it.second!!)

        }

        _defect.clear()

    }

}
