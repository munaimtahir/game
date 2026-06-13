package com.vexel.arcadetrio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vexel.arcadetrio.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val debugLaunchRoute = if (BuildConfig.DEBUG) intent?.getStringExtra("screenshot_route") else null
        setContent { ArcadeApp(debugLaunchRoute = debugLaunchRoute) }
    }
}
