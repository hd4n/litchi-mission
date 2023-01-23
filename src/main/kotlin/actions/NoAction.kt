package actions

class NoAction : Action() {
    override val typeCode = -1
    private val param = 0

    override fun toString(): String = "$typeCode, $param"
}