package com.mascot.app.util

import android.location.Location

fun getDistanceMeter(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double
): Float {
    val result = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, result)
    return result[0]
}
