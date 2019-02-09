package org.gakeutil.util.collection

/**
 * BoolArray has no size.
 *
 * @param value initialize value by binary.
 *
 */
data class BoolArray(private var value: Long = 0) {

    companion object {

        fun get(value: Long, index: Int): Boolean {

            val i = (1L shl index)

            return (value and i) == i

        }

    }

    /**
     * copy constructor.
     *
     * @param original original BoolArray
     */
    constructor(original: BoolArray) : this(original.value)

    operator fun set(index: Int, v: Boolean) {

        val i = (1 shl index).toLong()

        if(get(index) && !v) {

            value -= i
            return

        }

        if(!get(index) && v) {

            value += i
            return

        }

    }

    operator fun get(index: Int): Boolean = get(value, index)

}

fun BooleanArray.toBoolArray(): BoolArray {

    val ba = BoolArray()

    this.forEachIndexed { index, b -> ba[index] = b }

    return ba

}

fun Array<Boolean?>.toBoolArray(): BoolArray {

    val ba = BoolArray()

    this.forEachIndexed { index, b -> ba[index] = b == true }

    return ba

}

fun Int.getBoolOf(index: Int) = BoolArray(this.toLong())[index]