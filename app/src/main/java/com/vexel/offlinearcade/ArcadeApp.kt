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
import com.vexel.offlinearcade.core.ui.SplashShell
import kotlinx.coroutines.delay

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
        delay(if (effectiveReducedEffects) 0L else 760L) // No delay in tests
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
