package org.gakeutil.util.collection

class BoolMap<K>(vararg pairs: Pair<K, Boolean>) {

    val boolArray = BoolArray()

    val keyMap = mutableMapOf<K, Int>()

    val size get() = keyMap.size

    init {

        pairs.forEachIndexed { index, pair ->

            boolArray[index] = pair.second

            keyMap[pair.first] = index

        }

    }

    operator fun set(key: K, value: Boolean) {

        boolArray[keyMap[key] ?: size] = value

        if(!keyMap.containsKey(key))
            keyMap[key] = size

    }

    operator fun  get(key: K) = if(keyMap.containsKey(key)) boolArray[keyMap[key]!!] else false

    fun forEach(process: (K, Boolean)->Unit) {

        keyMap.forEach { t, u -> process(t, boolArray[u]) }

    }

}