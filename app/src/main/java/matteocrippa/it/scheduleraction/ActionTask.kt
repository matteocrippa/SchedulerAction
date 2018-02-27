package matteocrippa.it.scheduleraction

/**
 * Created by matteocrippa on 25/02/2018.
 */

class ActionTask {
    var id: Long = 0
    var duration: Long = 0
    var at: Long? = null
    var exec: (() -> Unit)? = null
}