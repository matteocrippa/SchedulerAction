package matteocrippa.it.scheduleraction

/**
 * Created by matteocrippa on 25/02/2018.
 */

class SchedulerAction(val name: String, private val listener: onSchedulerListener) {

    interface onSchedulerListener {
        fun onProgress(progress: Double)
    }

    enum class SchedulerActionStatus { Play, Pause, Stop }

    var status = SchedulerActionStatus.Stop
    var progress: Double by Delegates.observable(0.0) { _, _, _ ->
        listener.onProgress(progress)
    }
    var isDebug = false

    // private
    private var queue: ArrayList<ActionTask> = arrayListOf()
    private var timer = Timer(name, true)
    private var currentAction by Delegates.observable(0) { _, _, _ ->
        calculateProgress()
    }

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

    fun action(at: Long, duration: Long? = null, exec: (() -> Unit)?) {
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
        // reorder queue
        reorderQueue()
        // then start processing
        for (index in currentAction until queue.count()) {
            val item = queue[index]

            timer.schedule(timerTask {
                item.exec?.invoke()
                currentAction = item.id.toInt()
                if (isDebug) {
                    Log.d("⏲", progress.toString())
                }
            }, calculateRealAt(item.at))
        }
    }

    private fun calculateProgress() {
        progress = (currentAction.toDouble() / queue.count().toDouble())
    }

    private fun calculateRealAt(at: Long): Long {
        return if (currentAction > 0) {
            val item = queue[currentAction]
            at - item.at
        } else
            at
    }

    private fun reorderQueue() {
        queue.sortBy { it.at }
        var index = 0
        queue.forEach {
            it.id = index.toLong()
            index += 1
        }
    }

    private fun calculateWhen(action: ActionTask): Long {
        val currentItemPosition = queue.indexOf(action)
        return (currentAction..currentItemPosition)
            .map { queue[it] }
            .map { it.duration ?: 0 }
            .sum()
    }

    // calculate the closest action
    private fun getClosestAction(): Int {
        return (progress * queue.count().toDouble()).toInt()
    }

}