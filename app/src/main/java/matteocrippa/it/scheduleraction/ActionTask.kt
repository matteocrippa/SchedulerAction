package matteocrippa.it.scheduleraction

/**
 * Created by matteocrippa on 25/02/2018.
 */

class ActionTask {
    var id: Long = 0
    var duration: Long? = null
    var at: Long = 0
    private var realAt: Long = 0
    var exec: (() -> Unit)? = null
}