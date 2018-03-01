package matteocrippa.it.scheduleraction

import android.util.Log
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Created by matteocrippa on 25/02/2018.
 */

class SchedulerAction(val name: String) {

    enum class SchedulerActionStatus { Play, Pause, Stop }

    var status = SchedulerActionStatus.Stop
    var progress = 0.0
    var isDebug = false

    // private
    private var queue: ArrayList<ActionTask> = arrayListOf()
    private var timer = Timer(name, true)
    private var currentAction = 0

    // Public Functions
    fun start() {

        // generate new timer
        timer = Timer(name, true)

        // update status
        status = SchedulerActionStatus.Play

        // generate the items
        generate()
    }

    fun stop() {
        // pause
        pause()
        // rewind
        rewind()
        // set stop
        status = SchedulerActionStatus.Stop
    }

    fun pause() {
        status = SchedulerActionStatus.Pause
        timer.cancel()
        timer.purge()
    }

    fun rewind() {
        // reset to first action
        currentAction = 0

        // force restore initial situation, so empty the queue
        queue.clear()

    }

    fun next() {
        if(currentAction < queue.count() - 1) {
            currentAction += 1
        }
        pause()
        // set stop
        status = SchedulerActionStatus.Stop
    }

    fun prev() {
        if(currentAction > 0) {
            currentAction -= 1
        }
        pause()
        // set stop
        status = SchedulerActionStatus.Stop
    }

    fun seek(newProgress: Double) {
        progress = newProgress
        currentAction = getClosestAction()
    }

    fun action(duration: Long, at: Long? = null, exec: (() -> Unit)?) {
        val id = queue.count().toLong()

        val action = ActionTask()
        action.id = id
        action.duration = duration
        action.exec = exec
        action.at = at

        queue.add(action)
    }

    // Private functions
    private fun generate() {
        for (index in currentAction until queue.count()) {
            val item = queue[index]
            var time = calculateWhen(item)

            item.at?.let {
                time = it
            }

            timer.schedule(timerTask {
                item.exec?.invoke()
                currentAction = item.id.toInt()
                progress = (currentAction.toDouble() / queue.count().toDouble())
                if (isDebug) {
                    Log.d("⏲", progress.toString())
                }
            }, time)
        }
    }

    private fun calculateWhen(action: ActionTask): Long {
        val currentItemPosition = queue.indexOf(action)
        return (currentAction..currentItemPosition)
                .map { queue[it] }
                .map { it.duration }
                .sum()
    }

    // calculate the closest action
    private fun getClosestAction(): Int {
        return (progress * queue.count().toDouble()).toInt()
    }

}