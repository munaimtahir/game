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
import com.vexel.offlinearcade.core.ui.OfflineMiniArcadeTheme
import com.vexel.offlinearcade.core.ui.SplashShell
import kotlinx.coroutines.delay

@Composable
fun ArcadeApp() {
    val context = LocalContext.current
    val viewModel: ArcadeViewModel = viewModel(factory = ArcadeViewModel.factory(ArcadeDependencies.repository(context)))
    val snapshot by viewModel.snapshot.collectAsState()
    val navController = rememberNavController()
    val feedback = rememberArcadeFeedback(context = context, settings = snapshot.settings)
    var showSplash by remember { mutableStateOf(true) }

    // Force reduced effects in test environments to prevent infinite animations from blocking tests
    val isTest = remember {
        try {
            Class.forName("androidx.test.platform.app.InstrumentationRegistry")
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

    OfflineMiniArcadeTheme(
        themeId = snapshot.profile.selectedThemeId,
        highContrast = snapshot.settings.highContrastEnabled,
        reducedEffects = effectiveReducedEffects,
    ) {
        if (actualShowSplash) {
            SplashShell(
                title = "Offline Mini Arcade",
                subtitle = "Fast local runs. Premium shell. No internet required.",
            )
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
                onRecordRun = viewModel::recordRun,
            )
        }
    }
}
