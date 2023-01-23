package actions

class StopRecording : Action() {
    override val typeCode: Int = 3
    private val param: Int = 0

    override fun toString(): String = "$typeCode, $param"
}