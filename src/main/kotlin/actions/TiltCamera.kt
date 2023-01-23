package actions

class TiltCamera : Action {
    override val typeCode: Int = 5
    var degrees: Int
        set(value) {
            if (value > 0 || value < -90) {
                throw IllegalArgumentException("The angle of camera tilt must be between 0 and -90")
            }
            field = value
        }

    constructor(deg: Int) {
        degrees = deg
    }

    override fun toString(): String = "$typeCode,$degrees"
}
