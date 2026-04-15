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

    LaunchedEffect(snapshot.settings.reducedEffects) {
        delay(if (snapshot.settings.reducedEffects) 420L else 760L)
        showSplash = false
    }

    OfflineMiniArcadeTheme(
        themeId = snapshot.profile.selectedThemeId,
        highContrast = snapshot.settings.highContrastEnabled,
        reducedEffects = snapshot.settings.reducedEffects,
    ) {
        if (showSplash) {
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
