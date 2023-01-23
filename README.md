# Litchi-mission library for Java/Kotlin

## Generate Litchi flight paths for DJI drones

### Features

- Export flight paths as CSV files that can be imported into the Litchi Mission Hub
- Validation for waypoint and action properties 
- Methods for working with coordinates

### Example

```kotlin
// simple curve with an action 

// create a mission to hold the waypoints
val mission = LitchiMission()

// initialize waypoints with coordinates
val wp1 = Waypoint(LatLng(46.303625, 18.736444))
val wp2 = Waypoint(LatLng(46.303247, 18.739722))
val wp3 = Waypoint(LatLng(46.300621, 18.739779))

// add curvature
wp2.curveSizeM = 254

// add a stay for 1 second action
wp2.addAction(StayFor(1000))

// add the waypoints to the mission
mission.addWaypoint(wp1)
mission.addWaypoint(wp2)
mission.addWaypoint(wp3)

// export to example.csv
mission.export("example")
```