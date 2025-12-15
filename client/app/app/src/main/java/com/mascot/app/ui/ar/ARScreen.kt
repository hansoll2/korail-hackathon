package com.mascot.app.ui.ar

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mascot.app.ui.Screen

@Composable
fun ARScreen(navController: NavHostController) {
    val context = LocalContext.current

    // 타겟 설정
    val targetLat = 36.376710
    val targetLon = 127.388120
    val targetRadiusMeters = 100.0f // 테스트용 1000m

    var myLocation by remember { mutableStateOf<Location?>(null) }
    var distanceToTarget by remember { mutableStateOf(9999f) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        if (hasPermission) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        val res = FloatArray(1)
                        Location.distanceBetween(loc.latitude, loc.longitude, targetLat, targetLon, res)
                        distanceToTarget = res[0]
                        myLocation = loc
                    }
                }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermission = it.containsValue(true)
        if (hasPermission) updateLocation()
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            updateLocation()
        }
    }

    if (!hasPermission) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("위치 권한이 필요합니다.")
        }
    } else if (myLocation == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color(0xFF536DFE))
                Spacer(modifier = Modifier.height(16.dp))
                Text("현재 위치 확인 중...", color = Color.Gray)
            }
        }
    } else if (distanceToTarget > targetRadiusMeters) {
        OutOfRangeScreen(
            currentLat = myLocation!!.latitude,
            currentLon = myLocation!!.longitude,
            targetLat = targetLat,
            targetLon = targetLon,
            currentDist = distanceToTarget,
            onRefreshLocation = { updateLocation() }
        )
    } else {
        // AR 실행 화면
        ARContent(
            // 완료 시 홈 화면으로 이동
            onCollectionFinished = {

                navController.navigate("home?new_mascot=true") {
                    // AR 화면은 스택에서 제거 (뒤로가기 방지)
                    popUpTo(Screen.AR.route) { inclusive = true }
                }
            }
        )
    }
}