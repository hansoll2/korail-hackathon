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

    // 내 위치 정보 상태
    var myLocation by remember { mutableStateOf<Location?>(null) }
    var distanceToTarget by remember { mutableStateOf(9999f) }

    // 권한 상태
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // 위치 갱신 함수
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

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermission = it.containsValue(true)
        if (hasPermission) updateLocation()
    }

    // 앱 시작 시 실행
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            updateLocation()
        }
    }

    if (!hasPermission) {
        // 1. 권한이 없을 때
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("위치 권한이 필요합니다.")
        }
    } else if (myLocation == null) {
        // 2. 권한은 있는데 아직 GPS 값을 못 받았을 때 (로딩)
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
        // 3. 위치 확인 완료 & 범위 밖일 때 -> 지도 표시
        OutOfRangeScreen(
            currentLat = myLocation!!.latitude,
            currentLon = myLocation!!.longitude,
            targetLat = targetLat,
            targetLon = targetLon,
            currentDist = distanceToTarget,
            onRefreshLocation = { updateLocation() }
        )
    } else {
        // 4. 위치 확인 완료 & 범위 안일 때 -> AR 실행
        ARContent(
            // 완료 시 튜토리얼 화면으로 이동
            onCollectionFinished = {
                navController.navigate("tutorial_start") {
                    popUpTo(Screen.AR.route) { inclusive = true }
                }
            }
        )
    }
}