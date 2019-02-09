package org.gakeutil.util

import org.gakeutil.close

object Timers {

    fun new(max: Int, onUpdate: ()->Unit = {}, onFinished: ()->Unit = {}) = Timer(max, onUpdate, onFinished)

    fun new(max: Int, vararg schedules: SchedulePart, onUpdate: ()->Unit = {}, onFinished: ()->Unit = {}) = Timer(max, onUpdate, onFinished).apply { schedule(*schedules) }

    fun newInterval(frequency: Int, interval: Int, origin: Int = 0, process: Timer.()->Unit) = new(interval * frequency + origin).apply { scheduleInterval(frequency, interval, origin, process) }

}

data class SchedulePart(val from: Int, val end: Int, val process: (Timer.()->Unit)?, val first: (Timer.()->Unit)? = null) {

    val timeRange = from..end

}

class Timer(max: Int, val onUpdate: ()->Unit, val onFinished: ()->Unit) : IUpdatable {

    var count = 0.close(max)

    var isFinished = false
        private set

    val schedules = mutableSetOf<SchedulePart>()

    val passedSec get() = count.value / 60

    override fun update() {

        if(isFinished)
            return

        ++count

        onUpdate()

        schedules.filter { count.value in it.timeRange }.forEach {

            if(count.value == it.from)
                it.first?.invoke(this)

            it.process?.invoke(this)
        }

        if(count.isCountStopped) {

            isFinished = true

            onFinished()

            return

        }

    }

    fun schedule(vararg parts: SchedulePart) {

        schedules.addAll(parts)

    }

    fun schedule(from: Int, end: Int, process: (Timer.()->Unit)? = null, first: (Timer.()->Unit)? = null) {

        schedules.add(SchedulePart(from, end, process, first))

    }


    fun scheduleAll(vararg objs: Pair<IntRange, Timer.()->Unit>) {

        objs.forEach {
            schedule(it.first.first, it.first.last, it.second, null)
        }

    }

    fun scheduleFirstAll(vararg objs: Pair<Int, Timer.()->Unit>) {

        val starts = objs.map { it.first }

        objs.forEachIndexed { index, it ->
            schedule(starts[index], starts.getOrElse(index + 1) { count.MAX_VALUE }, null, it.second)
        }

    }

    fun scheduleInterval(frequency: Int, interval: Int, origin: Int, process: Timer.()->Unit) {

        schedule(origin, origin + interval * frequency, fun Timer.() {

            if((count.value - origin) % interval == 0)
                process()

        })

    }

    fun scheduleInterval(frequency: Int, interval: Int, process: Timer.()->Unit) = scheduleInterval(frequency, interval, count.value, process)

}