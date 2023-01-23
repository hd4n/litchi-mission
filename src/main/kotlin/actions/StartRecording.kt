package actions

class StartRecording : Action() {
    override val typeCode: Int = 2
    private val param: Int = 0

    override fun toString(): String = "$typeCode, $param"
}