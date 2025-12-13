package com.mascot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mascot.app.ui.MascotApp
import com.mascot.app.ui.theme.MascotTheme
import dagger.hilt.android.AndroidEntryPoint
import com.mascot.app.data.encyclopediadata.initial.DataInitializer

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ⭐⭐⭐ [디버깅 코드 시작] ⭐⭐⭐
        // 앱이 실행될 때, 실제 적용된 패키지명과 Client ID를 강제로 로그에 출력합니다.
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, android.content.pm.PackageManager.GET_META_DATA)
            val myPackageName = packageName
            val myClientId = appInfo.metaData?.getString("com.naver.maps.map.CLIENT_ID")

            android.util.Log.e("NAVER_DEBUG", "====================================")
            android.util.Log.e("NAVER_DEBUG", "1. 실제 실행 패키지명: $myPackageName")
            android.util.Log.e("NAVER_DEBUG", "2. 인식된 Client ID : $myClientId")
            android.util.Log.e("NAVER_DEBUG", "====================================")
        } catch (e: Exception) {
            android.util.Log.e("NAVER_DEBUG", "디버깅 실패: ${e.message}")
        }
        // ⭐⭐⭐ [디버깅 코드 끝] ⭐⭐⭐

        setContent {
            MascotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MascotApp()
                }
            }
        }
    }
}