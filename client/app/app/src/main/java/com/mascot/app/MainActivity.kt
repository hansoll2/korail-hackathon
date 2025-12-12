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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MascotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MascotApp()   // ← 앱 전체 네비게이션이 여기 들어와야 함
                }
            }
        }
    }
}
