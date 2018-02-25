package matteocrippa.it.scheduleraction

/**
 * Created by matteocrippa on 25/02/2018.
 */

class ActionTask {
    var id: Long = 0
    var duration: Long = 0
    var delay: Long = 0
    var exec: (() -> Unit)? = null
}