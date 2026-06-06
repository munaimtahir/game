package com.vexel.arcadetrio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vexel.arcadetrio.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val debugLaunchRoute = if (BuildConfig.DEBUG) intent?.getStringExtra("screenshot_route") else null
        val debugLaunchState = if (BuildConfig.DEBUG) intent?.getStringExtra("screenshot_state") else null
        setContent { ArcadeApp(debugLaunchRoute = debugLaunchRoute, debugLaunchState = debugLaunchState) }
    }
}
