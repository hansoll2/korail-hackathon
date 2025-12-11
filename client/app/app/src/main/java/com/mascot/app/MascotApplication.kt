// 파일 위치: com.mascot.app.MascotApplication.kt
package com.mascot.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // ⭐️ 이 한 줄이 Hilt를 작동시키는 스위치입니다!
class MascotApplication : Application()