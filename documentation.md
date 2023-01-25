# Litchi-mission documentation

---

## LatLng

Holds the latitude and longitude for a position.

### Methods

- `constructor(latitude: Double, longitude: Double)`
- `shift(meters: Double, angleDeg: Double): LatLng` Returns the original coordinates shifted by x meters in a specified direction.
- `distance(from: LatLng): Double` Returns the horizontal distance from a given position in meters.

---

## Waypoint

Holds all parameters for a waypoint. The parameters can be set using the Java setters or the Kotlin properties. When trying to set a value considered invalid by Litchi, an IllegalArgumentException will be thrown.

### Properties

- `position: LatLng`
- `altitudeM: Double` The altitude of the waypoint in meters. Must be between **-200** and **500** meters. Defaults to **30.0**.
- `headingDeg: Int` The heading of the aircraft at a given waypoint. Must be between **-180** and **360** degrees. Defaults to **0** (north).
- `curveSizeM: Int` The radius of the curve at a given waypoint. Must be between **0** and **1000** meters. Defaults to **0**. Further validation when exporting:
    - The curve size cannot be set for the first and the last waypoints
    - The maximum allowed curve size for a waypoint is the minimum of \[the waypoints distance to an adjacent waypoint minus the curve size set for the other waypoint], for each neighbor, minus 1 meters
- `gimbalPitchMode: GimbalPitch` The gimbals operating mode at a waypoint. Defaults to `GimbalPitch.DISABLED`. Modes:
    - `GimbalPitch.FOCUS_POI` Turn the gimbal to a specified point (`poi` and `poiAltitude` properties)
    - `GimbalPich.INTERPOLATE` Gradually pitch the gimbal to a specified angle (`gimbalPitchAngle` property)
- `gimbalPitchAngle: Int` Specifies the pitch of the gimbal when the gimbal pitch mode is set to `GimbalPitch.INTERPOLATE`. Must be between **-90** and **30** degrees. Defaults to **0**.
- `altitudeMode: AltitudeMode` Specifies the reference point for the altitude. Modes:
    - `AltitudeMode.ABOVE_TAKEOFF` Default
    - `AltitudeMode.ABOVE_GROUND`
- `speedMps: Double` The aircraft's speed in meters per second. Must be between **0.0** and **15.0**. Defaults to **0.0** (cruise speed).
- `poi: LatLng` The position of the point of interest when using the gimbal in the `GimbalMode.FOCUS_POI` mode.
- `poiAltitudeM: Int` The altitude of the point of interest when using the gimbal in the `GimbalMode.FOCUS_POI` mode.
- `poiAltitudeMode: AltitudeMode` Same as the regular `altitudeMode` but for the point of interest.
- `photoTimeIntervalS: Double` Take a photo automatically every x seconds. Must be between **0.1** and **6000.0** or set to **-1.0** (disabled). Defaults to **-1.0**. Cannot be set while `photoDistIntervalM` is enabled.
- `photoDistIntervalM: Double` Take a photo automatically every x meters. Must be between **0.1** and **6000.0** or set to **-1.0** (disabled). Defaults to **-1.0**. Cannot be set while `photoTimeIntervalM` is enabled.

### Methods

- `constructor(position: LatLng)`
- `constructor(position: LatLng, altitude: Double)`
- `addAction(action: Action)` Add an action to perform. Up to 15 actions can be added for each Waypoint.
- `getAction(index: Int): Action` Get an already added action.
- `distance(from: Waypoint): Double` Returns the 3 dimensional distance from a given waypoint in meters.

---

## Actions

Up to 15 actions can be added for a Waypoint. Some actions have parameters, these can be set in the constructor or using the setters/properties.

### NoAction

Perform no action. NoActions are added automatically when exporting when no actions are specified.

### RotateAircraft

Rotate the aircraft by a specified degree.

- `degrees: Int` Must be between **-180** and **360**.

### StayFor

Hover at the waypoint for the given duration.

- `durationMs: Int` The duration in milliseconds. Must be between **0** and **65535**.

### TiltCamera

Tilt the camera by a specified degree.

- `degrees: Int` Must be between **0** and **-90**.

### StartRecording

### StopRecording

### TakePhoto

---

## LitchiMission

This class holds all waypoints for a mission. A maximum of **99** waypoints can be added for each mission.

### Methods

- `addWaypoint(waypoint: Waypoint)`
- `addAllWaypoints(waypoints: List<Waypoint>)`
- `getWaypoint(index: Int): Waypoint`
- `export(filename: String, latLngOnly: Boolean = false)` Generate the flight path as a CSV. If `latLngOnly` is set to true only the coordinates will be included in the csv. This can be useful for debugging and is also accepted by the Litchi Mission Hub.