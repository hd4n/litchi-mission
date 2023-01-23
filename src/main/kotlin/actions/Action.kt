package actions

abstract class Action {
    abstract val typeCode: Int
    abstract override fun toString(): String
}