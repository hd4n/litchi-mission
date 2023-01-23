package actions

class TakePhoto : Action() {
    override val typeCode: Int = 1
    private val param: Int = 0

    override fun toString(): String = "$typeCode, $param"
}