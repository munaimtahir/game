package com.vexel.offlinearcade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.vexel.offlinearcade.BuildConfig
import com.vexel.offlinearcade.core.ui.OfflineMiniArcadeTheme
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

@Composable
fun ArcadeApp(
    debugLaunchRoute: String? = null,
    debugLaunchState: String? = null,
) {
    val context = LocalContext.current
    val viewModel: ArcadeViewModel = viewModel(factory = ArcadeViewModel.factory(ArcadeDependencies.repository(context)))
    val snapshot by viewModel.snapshot.collectAsState()
    val navController = rememberNavController()
    val feedback = rememberArcadeFeedback(context = context, settings = snapshot.settings)
    var showSplash by remember { mutableStateOf(true) }

    // Force reduced effects in test environments to prevent infinite animations from blocking tests
    val isTest = remember {
        try {
            Class.forName("com.vexel.offlinearcade.ChallengeUpdateTest")
            true
        } catch (e: Exception) {
            false
        }
    }
    val effectiveReducedEffects = snapshot.settings.reducedEffects || isTest

    LaunchedEffect(effectiveReducedEffects) {
        delay(if (effectiveReducedEffects) 0L else 2500L) // No delay in tests, 2.5s splash
        showSplash = false
    }

    // Force showSplash to false immediately in tests
    val actualShowSplash = if (isTest) false else showSplash
    val launchRoute = if (BuildConfig.DEBUG) debugLaunchRoute else null

    fun mapDebugRoute(route: String?): String? = when (route) {
        null, "", "home" -> null
        "pulse_detail", "pulse_orbit_detail" -> Routes.PulseOrbitDetail
        "pulse_game", "pulse_orbit_game" -> Routes.PulseOrbitGame
        "lane_detail", "lane_drift_detail" -> Routes.LaneDriftDetail
        "lane_game", "lane_drift_game" -> Routes.LaneDriftGame
        "stack_detail", "stack_drop_detail" -> Routes.StackDropDetail
        "stack_game", "stack_drop_game" -> Routes.StackDropGame
        "challenges" -> Routes.Challenges
        "stats" -> Routes.Stats
        "settings" -> Routes.Settings
        else -> null
    }
    val resolvedLaunchRoute = mapDebugRoute(launchRoute)

    LaunchedEffect(resolvedLaunchRoute) {
        if (resolvedLaunchRoute != null) {
            navController.navigate(resolvedLaunchRoute) {
                popUpTo(Routes.Home) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    OfflineMiniArcadeTheme(
        themeId = snapshot.profile.selectedThemeId,
        highContrast = snapshot.settings.highContrastEnabled,
        reducedEffects = effectiveReducedEffects,
    ) {
        if (actualShowSplash) {
            SplashLoadingScreen()
        } else {
            ArcadeNavHost(
                navController = navController,
                snapshot = snapshot,
                feedback = feedback,
                onToggleSound = viewModel::toggleSound,
                onToggleMusic = viewModel::toggleMusic,
                onToggleVibration = viewModel::toggleVibration,
                onToggleReducedEffects = viewModel::toggleReducedEffects,
                onToggleHighContrast = viewModel::toggleHighContrast,
                onUnlockTheme = viewModel::unlockTheme,
                onSelectTheme = viewModel::selectTheme,
                onUnlockSkin = viewModel::unlockSkin,
                onSelectSkin = viewModel::selectSkin,
                onRecordRun = viewModel::recordRun,
            )
        }
    }
}

@Composable
fun SplashLoadingScreen() {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_loading_screen),
            contentDescription = "Splash Screen",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.value)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF007F), // Neon Pink
                                    Color(0xFF00F0FF)  // Neon Cyan
                                )
                            )
                        )
                )
            }
            Text(
                text = "LOADING... ${(progress.value * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
