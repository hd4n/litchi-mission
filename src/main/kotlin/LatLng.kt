import kotlin.math.*

data class LatLng(
    val latitude: Double, val longitude: Double
) {
    fun shift(meters: Double, angleDeg: Double): LatLng {
        val angularDistance = meters / (EARTH_RADIUS_KM * 1000)
        val latRad = Math.toRadians(latitude)
        val lngRad = Math.toRadians(longitude)
        val trueCourse = Math.toRadians(angleDeg)

        val newLat = asin(sin(latRad) * cos(angularDistance) + cos(latRad) * sin(angularDistance) * cos(trueCourse))
        val dLon = atan2(
            sin(trueCourse) * sin(angularDistance) * cos(latRad), cos(angularDistance) - sin(latRad) * sin(newLat)
        )
        val newLng = (lngRad - dLon + PI) % (2 * PI) - PI

        return LatLng(Math.toDegrees(newLat), Math.toDegrees(newLng))
    }

    fun distance(from: LatLng): Double {
        val phi1 = Math.toRadians(from.latitude)
        val phi2 = Math.toRadians(latitude)
        val dPhi = Math.toRadians(latitude - from.latitude)
        val dLambda = Math.toRadians(longitude - from.longitude)

        val a = sin(dPhi / 2) * sin(dPhi / 2) + cos(phi1) * cos(phi2) * sin(dLambda / 2) * sin(dLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * 1000 * c
    }

    override fun toString(): String = "$latitude, $longitude"

    companion object {
        const val EARTH_RADIUS_KM = 6378.137
    }
}
