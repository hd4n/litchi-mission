package actions

class StayFor : Action {
    override val typeCode: Int = 0
    var durationMs: Int
        set(value) {
            if (value < 0 || value > 65535) {
                throw IllegalArgumentException("The duration must between 0 and 65535")
            }

            field = value
        }

    constructor(durationMs: Int) {
        this.durationMs = durationMs
    }

    override fun toString(): String = "$typeCode, $durationMs"
}