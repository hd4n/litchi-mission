package actions

class RotateAircraft : Action {
    override val typeCode: Int = 4
    var degrees: Int
        set(value) {
            if (value < -180 || value > 360) {
                throw IllegalArgumentException("Rotation must be between -180 and 360 degrees")
            }
            field = value
        }

    constructor(deg: Int) {
        degrees = deg
    }

    override fun toString(): String = "$typeCode, $degrees"
}