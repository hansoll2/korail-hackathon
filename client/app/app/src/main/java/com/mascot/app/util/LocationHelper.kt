package com.mascot.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices

object LocationHelper {

    private const val TAG = "LocationHelper"

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (lat: Double, lng: Double) -> Unit
    ) {
        Log.d(TAG, "📍 getCurrentLocation() 호출됨")

        val fusedClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(
                        TAG,
                        "✅ 위치 수신 성공 → lat=${location.latitude}, lng=${location.longitude}"
                    )
                    onResult(location.latitude, location.longitude)
                } else {
                    Log.e(TAG, "❌ location == null (GPS 꺼짐 / 위치 기록 없음)")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "❌ 위치 요청 실패", e)
            }
    }
}
