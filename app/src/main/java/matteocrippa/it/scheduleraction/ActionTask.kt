package matteocrippa.it.scheduleraction

/**
 * Created by matteocrippa on 25/02/2018.
 */


data class ActionTask(
    val at: Long,
    val exec: (() -> Unit)? = null,
    val duration: Long? = null
) {
    var id: Long = 0
}