import java.io.File
import kotlin.math.min

class LitchiMission {
    private var waypoints = mutableListOf<Waypoint>()

    fun addWaypoint(waypoint: Waypoint) {
        if (waypoints.size == MAX_WAYPOINTS) {
            throw Exception("Cannot add more waypoints, the maximum number of waypoints is $MAX_WAYPOINTS")
        }
        waypoints.add(waypoint)
    }

    fun addAllWaypoints(waypoints: List<Waypoint>) {
        if (this.waypoints.size + waypoints.size > MAX_WAYPOINTS) {
            throw Exception("Cannot add more waypoints, the maximum number of waypoints is $MAX_WAYPOINTS")
        }
        this.waypoints.addAll(waypoints)
    }

    fun getWaypoint(index: Int): Waypoint {
        if (index < 0 || index > waypoints.size - 1) {
            throw IllegalArgumentException("Invalid index '$index', this mission only has ${waypoints.size} waypoints")
        }
        return waypoints[index]
    }

    fun export(filename: String, latLngOnly: Boolean = false) {
        for (i in waypoints.indices) {
            if (waypoints[i].altitudeM > 120) {
                println("[Warning] The altitude for Waypoint $i is ${waypoints[i].altitudeM} meters, this may violate local regulations")
            }
        }

        validateCurveSize()

        var fn = filename
        if (!filename.endsWith(".csv", true)) {
            fn += ".csv"
        }

        val out = File(fn)
        // clear the file
        out.writeText("")
        if (latLngOnly) {
            out.writeText("latitude,longitude\n")
        }else{
            out.writeText(CSV_HEADER + "\n")
        }
        out.appendText(waypoints.joinToString(separator = "\n") { if (latLngOnly) it.simpleFormat() else it.extendedFormat() })
        println("Exported '${out.absolutePath}'")
    }

    private fun validateCurveSize() {
        if (waypoints.first().curveSizeM != 0 || waypoints.last().curveSizeM != 0) {
            throw IllegalArgumentException("Curve size must be 0 for the first and last waypoints")
        }

        // check every waypoint except the first and the last,
        // ensure all curve sizes are within the theoretical limit
        for (i in 1 until waypoints.size) {
            if (waypoints[i].curveSizeM != 0) {
                // every curve must be smaller than distance from the waypoint to it's closest neighbor
                val smallestDist = min(waypoints[i].distance(waypoints[i - 1]), waypoints[i].distance(waypoints[i + 1]))
                val maxCurve = smallestDist.toInt() - 1 // -1 because of Litchi's implementation
                if (waypoints[i].curveSizeM > maxCurve) {
                    throw IllegalArgumentException("The curve size for Waypoint $i must be smaller than $smallestDist meters")
                }
            }
        }

        // ensure all curve sizes are correct
        for (i in 1 until waypoints.size) {
            if (waypoints[i].curveSizeM != 0) {
                // the maximum size for a curve is the minimum of (distance-curveSize) for all neighbors
                val maxCurve = min(
                    waypoints[i].distance(waypoints[i - 1]) - waypoints[i - 1].curveSizeM,
                    waypoints[i].distance(waypoints[i + 1]) - waypoints[i + 1].curveSizeM
                ).toInt() - 1 // -1 because of Litchi's implementation
                if (waypoints[i].curveSizeM > maxCurve) {
                    throw IllegalArgumentException("The maximum curve size allowed for Waypoint $i is $maxCurve meters")
                }
            }
        }
    }

    companion object {
        const val MAX_WAYPOINTS = 99
        const val CSV_HEADER = "latitude,longitude,altitude(m),heading(deg),curvesize(m)," +
                "rotationdir,gimbalmode,gimbalpitchangle,actiontype1,actionparam1,actiontype2,actionparam2," +
                "actiontype3,actionparam3,actiontype4,actionparam4,actiontype5,actionparam5,actiontype6,actionparam6," +
                "actiontype7,actionparam7,actiontype8,actionparam8,actiontype9,actionparam9,actiontype10,actionparam10" +
                ",actiontype11,actionparam11,actiontype12,actionparam12,actiontype13,actionparam13,actiontype14,actionparam14," +
                "actiontype15,actionparam15,altitudemode,speed(m/s),poi_latitude,poi_longitude,poi_altitude(m),poi_altitudemode," +
                "photo_timeinterval,photo_distinterval"
    }
}