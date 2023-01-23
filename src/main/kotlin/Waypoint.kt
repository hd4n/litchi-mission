import actions.Action
import actions.NoAction
import kotlin.math.abs
import kotlin.math.sqrt

class Waypoint {

    var position: LatLng

    var altitudeM: Double = 30.0
        set(value) {
            if (value < -200 || value > 500) {
                throw IllegalArgumentException("Altitude must be between -200 and 500 meters")
            }
            field = value
        }

    var headingDeg: Int = 0
        set(value) {
            if (value < -180 || value > 360) {
                throw IllegalArgumentException("Heading must be between -180 and 360 degrees")
            }
            field = value
        }

    var curveSizeM: Int = 0
        set(value) {
            if (value < 0 || value > 1000) {
                throw IllegalArgumentException("Curve size must be between 0 and 1000 meters")
            }
            field = value
        }

    val rotationDir: Int = 0 // not available in the mission hub

    var gimbalPitchMode: GimbalPitch = GimbalPitch.DISABLED

    // available when gimbalPitchMode is set to INTERPOLATE
    var gimbalPitchAngle: Int = 0
        set(value) {
            if (value < -90 || value > 30) {
                throw IllegalArgumentException("Gimbal pitch angle must be between -90 and 30 degrees")
            }
            field = value
        }

    private val actions: MutableList<Action> = mutableListOf()

    var altitudeMode: AltitudeMode = AltitudeMode.ABOVE_TAKEOFF

    var speedMps: Double = 0.0
        set(value) {
            if (value < 0.0 || value > 15.0) {
                throw IllegalArgumentException("Speed must be between 0 and 15 meters per second")
            }
            field = value
        }

    var poi: LatLng = LatLng(0.0, 0.0)

    var poiAltitudeM: Int = 0
        set(value) {
            if (value < -200 || value > 500) {
                throw IllegalArgumentException("POI altitude must be -200 and 500 meters")
            }
            field = value
        }

    var poiAltitudeMode: AltitudeMode = AltitudeMode.ABOVE_TAKEOFF

    // disabled: -1.0
    var photoTimeIntervalS: Double = -1.0
        set(value) {
            if (value != -1.0 && (value < 0.1 || value > 6000.0)) {
                throw IllegalArgumentException("Time interval must be between 0.1 and 6000.0 seconds or set to -1.0 (disabled)")
            }
            if (photoDistIntervalM != -1.0) {
                throw IllegalArgumentException("Time interval cannot be set while the distance interval is also set. Set 'photoDistIntervalM' to -1.0 to enable the time interval")
            }

            field = value
        }

    // disabled: -1.0
    var photoDistIntervalM: Double = -1.0
        set(value) {
            if (value != -1.0 && (value < 0.1 || value > 6000.0)) {
                throw IllegalArgumentException("Distance interval must be between 0.1 and 6000.0 meters or set to -1.0 (disabled)")
            }
            if (photoTimeIntervalS != -1.0) {
                throw IllegalArgumentException("Distance interval cannot be set while the time interval is also set. Set 'photoTimeIntervalS' to -1.0 to enable the distance interval")
            }

            field = value
        }

    constructor(position: LatLng) {
        this.position = position
    }

    constructor(position: LatLng, altitude: Double) {
        this.position = position
        this.altitudeM = altitude
    }

    fun addAction(action: Action) {
        if (actions.size == MAX_ACTIONS) {
            throw Exception("Cannot add more actions, the maximum number is $MAX_ACTIONS")
        }
        actions.add(action)
    }

    fun getAction(index: Int): Action {
        if (index < 0 || index > actions.size - 1) {
            throw IllegalArgumentException("Index must be between 0 and ${actions.size}")
        }
        return actions[index]
    }

    fun simpleFormat(): String = position.toString()

    fun extendedFormat(): String {
        var out = "$position, $altitudeM, $headingDeg, $curveSizeM, $rotationDir, $gimbalPitchMode, $gimbalPitchAngle, "
        for (action in actions) {
            out += "$action, "
        }

        // Litchi expects 15 actions, fill the missing actions with NoActions
        val noAction = NoAction()
        for (i in actions.size until MAX_ACTIONS) {
            out += "$noAction, "
        }

        out += "$altitudeMode, $speedMps, $poi, $poiAltitudeM, $poiAltitudeMode, $photoTimeIntervalS, $photoDistIntervalM"
        return out
    }

    fun distance(from: Waypoint): Double {
        val horizontal = position.distance(from.position)
        val vertical = abs(altitudeM - from.altitudeM)
        return sqrt(horizontal * horizontal + vertical * vertical)
    }

    companion object {
        const val MAX_ACTIONS = 15
    }

    enum class GimbalPitch(private val value: Int) {
        DISABLED(0),
        FOCUS_POI(1),
        INTERPOLATE(2);

        override fun toString(): String {
            return value.toString()
        }
    }

    enum class AltitudeMode(private val value: Int) {
        ABOVE_TAKEOFF(0),
        ABOVE_GROUND(1);

        override fun toString(): String {
            return value.toString()
        }
    }
}